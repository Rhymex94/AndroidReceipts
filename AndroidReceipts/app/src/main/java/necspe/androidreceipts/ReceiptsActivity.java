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

        formatter = new SimpleDateFormat("dd-MM-yyyy");

        f = new GFunctions(this);
        f.keepLang();
        db = new Database(this);


        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(R.string.title_activity_receipts);
        setSupportActionBar(tb);

    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                f.toSettings();
                return true;

            case R.id.newReceipt:
                CreateReceiptDialog d = new CreateReceiptDialog(this, getIntent().getStringExtra("tablename"));
                d.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
                        st.setText(formatter.format(date.getTime()));

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
    }

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

    public void setSearchButtonListener(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receipts = db.getReceipts(tablename, st.getText().toString(), et.getText().toString());
                renderReceipts(receipts);
            }
        });
    }

    public void renderReceipts(ArrayList<ArrayList<String>> receipts){
        receiptsLayout.removeAllViews();
        double sum = 0.0;
        for (int i = 0; i < receipts.size(); i++){
            LinearLayout receipt = new LinearLayout(this);
            receipt.setOrientation(LinearLayout.HORIZONTAL);
            receipt.setPadding(0, 12, 0, 12);
            TextView sumText = new TextView(this);
            TextView dateText = new TextView(this);
            TextView descText = new TextView(this);

            sumText.setTextSize(20);
            sumText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            dateText.setTextSize(20);
            dateText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            descText.setTextSize(20);
            descText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            sumText.setText(String.format("%.2f", Double.parseDouble(receipts.get(i).get(0))));
            dateText.setText(f.dateConvert(receipts.get(i).get(1)));
            descText.setText(receipts.get(i).get(2));

            final String checks = receipts.get(i).get(3);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            );
            params.setMargins(0,8,0,8);

            sumText.setLayoutParams(params);
            dateText.setLayoutParams(params);
            descText.setLayoutParams(params);

            receipt.addView(dateText);
            receipt.addView(descText);
            receipt.addView(sumText);

            receipt.setGravity(Gravity.CENTER);
            receipt.setBackgroundResource(R.drawable.border);
            sum += Double.parseDouble(receipts.get(i).get(0));


            receipt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    RemoveReceiptDialog dialog = new RemoveReceiptDialog(ReceiptsActivity.this,
                            checks);
                    dialog.show();
                    return true;
                }
            });



            receiptsLayout.addView(receipt);
        }
        sumfield.setText(String.format("%.2f", sum));

    }

}
