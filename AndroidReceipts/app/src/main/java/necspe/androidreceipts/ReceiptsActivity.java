package necspe.androidreceipts;

import android.app.DatePickerDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * ReceiptsActivity takes care of displaying receipts of a given table. Every table entry in the
 * TablesActivity opens its own ReceiptsActivity.
 */
public class ReceiptsActivity extends AppCompatActivity {
    GFunctions f;
    Database db;
    Button sd;
    TextView st;
    Button ed;
    TextView et;
    Button search;
    SimpleDateFormat formatter;
    LinearLayout receiptsLayout;
    String tablename;
    ArrayList<ArrayList<String>> receipts;
    TextView sumfield;

    /**
     * onCreate function is called automatically every time a new instance of ReceiptsActivity is
     * created. It takes care of fetching the layout file, assigning the variables used across
     * the activity and assigning the onClickListeners to the layout's buttons.
     * @param savedInstanceState a bundle containing information about the instance's state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts);

        receipts = new ArrayList<>();

        sd = (Button) findViewById(R.id.start_date_button);
        setStartButtonListener();
        st = (TextView) findViewById(R.id.start_date_text);
        ed = (Button) findViewById(R.id.end_date_button);
        setEndButtonListener();
        et = (TextView) findViewById(R.id.end_date_text);
        search = (Button) findViewById(R.id.search_button);
        setSearchButtonListener();
        receiptsLayout = (LinearLayout) findViewById(R.id.receipts_layout);
        sumfield = (TextView) findViewById(R.id.total_sum_field);

        tablename = getIntent().getStringExtra("tablename");

        // formatter is used to represent DatePicker's dates as a text.
        formatter = new SimpleDateFormat("dd-MM-yyyy");

        // GFunctions provides access to a collection of often used methods.
        f = new GFunctions(this);
        // This allows for instant effect of changing language. Otherwise a reload is required.
        f.keepLang();
        db = new Database(this);


        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(R.string.title_activity_receipts);
        setSupportActionBar(tb);

    }

    /**
     * onResume does basically the same things as the onCreate above, except it is called whenever
     * the application returns to this specific activity. This often is required for displaying
     * possible changes immediately, without having to reload separately.
     */
    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_receipts);
        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(R.string.title_activity_receipts);
        setSupportActionBar(tb);

        sd = (Button) findViewById(R.id.start_date_button);
        setStartButtonListener();
        st = (TextView) findViewById(R.id.start_date_text);
        ed = (Button) findViewById(R.id.end_date_button);
        setEndButtonListener();
        et = (TextView) findViewById(R.id.end_date_text);
        search = (Button) findViewById(R.id.search_button);
        setSearchButtonListener();
        receiptsLayout = (LinearLayout) findViewById(R.id.receipts_layout);
        sumfield = (TextView) findViewById(R.id.total_sum_field);

        receipts = db.getReceipts(tablename, st.getText().toString(), et.getText().toString());
        renderReceipts(receipts);
    }

    //  This creates the toolbar 'buttons' from the definition in menu-folder.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_receipts, menu);
        return true;
    }

    // This assigns the reactions for the buttons in the toolbar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            // if the settings button is clicked, redirect to the settings.
            case R.id.settings:
                f.toSettings();
                return true;

            // if the newReceipt button is clicked, show the CreateReceiptDialog.
            case R.id.newReceipt:
                CreateReceiptDialog d = new CreateReceiptDialog(this, getIntent().getStringExtra("tablename"));
                d.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * setStartButtonListener is a function that sets the onClickListener to the Start -button.
     * Clicking it opens a DatePicker for picking a start date. Having this as a function saves us
     * from the chore of having to write the exact same lines twice, in both onCreate and onResume
     * functions.
     */
    public void setStartButtonListener(){
        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(ReceiptsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    //@Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar date = Calendar.getInstance();
                        date.set(year, monthOfYear, dayOfMonth);
                        // Set the date picked by DatePicker as a text in its TextView.
                        st.setText(formatter.format(date.getTime()));

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
    }

    /**
     * same story as with the setStartButtonListener, except this time for the end date.
     */
    public void setEndButtonListener(){
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(ReceiptsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    //@Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar date = Calendar.getInstance();
                        date.set(year, monthOfYear, dayOfMonth);
                        et.setText(formatter.format(date.getTime()));

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
    }

    /**
     * setSearchButtonListener sets an onClickListener for the search -button, which queries the
     * database for receipts with given date constraints and the table name.
     */
    public void setSearchButtonListener(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receipts = db.getReceipts(tablename, st.getText().toString(), et.getText().toString());
                // Check renderReceipts for details on presenting the receipts.
                renderReceipts(receipts);
            }
        });
    }

    /**
     * renderReceipts creates visual presentations for the receipts to the ReceiptsActivity.
     * @param receipts the list of the data in the receipts to be rendered.
     */
    public void renderReceipts(ArrayList<ArrayList<String>> receipts){
        receiptsLayout.removeAllViews();
        // Sum is displayed in the bottom of the screen, and this contains the value.
        // Upon rendering each receipt, their sums are added to this variable.
        double sum = 0.0;
        for (int i = 0; i < receipts.size(); i++){
            // Create a new linear, horizontal layout.
            LinearLayout receipt = new LinearLayout(this);
            receipt.setOrientation(LinearLayout.HORIZONTAL);
            receipt.setPadding(0, 12, 0, 12);
            // Create three TextViews, one for sum, one for date, and one for description.
            TextView sumText = new TextView(this);
            TextView dateText = new TextView(this);
            TextView descText = new TextView(this);

            // Add the visual options.
            sumText.setTextSize(20);
            sumText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            dateText.setTextSize(20);
            dateText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            descText.setTextSize(20);
            descText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            // Set data as text into the according TextViews.
            sumText.setText(String.format("%.2f", Double.parseDouble(receipts.get(i).get(0))));
            dateText.setText(f.dateConvert(receipts.get(i).get(1)));
            descText.setText(receipts.get(i).get(2));

            // Keep the checksum in memory.
            final String checks = receipts.get(i).get(3);

            // Add the layout params.
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            );
            params.setMargins(0,8,0,8);

            sumText.setLayoutParams(params);
            dateText.setLayoutParams(params);
            descText.setLayoutParams(params);

            // Add the created TextViews to the LinearLayout.
            receipt.addView(dateText);
            receipt.addView(descText);
            receipt.addView(sumText);

            receipt.setGravity(Gravity.CENTER);
            receipt.setBackgroundResource(R.drawable.border);
            // Update the sum of all receipts.
            sum += Double.parseDouble(receipts.get(i).get(0));


            // Set an onLongClickListener to be able to delete this specific receipt when needed.
            receipt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    RemoveReceiptDialog dialog = new RemoveReceiptDialog(ReceiptsActivity.this,
                            checks);
                    dialog.show();
                    return true;
                }
            });



            // Add the receipts data to the activity's view.
            receiptsLayout.addView(receipt);
        }
        // Display the sum of all receipts.
        sumfield.setText(String.format("%.2f", sum));

    }

}
