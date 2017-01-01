package necspe.androidreceipts;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by santeri on 28.12.2016.
 * CreateReceiptDialog is a dialog displayed whenever the user intends to create a new receipt.
 * The dialog asks the user about certain information, checks that the inputs are valid and if so,
 * creates a new Receipt into the database and dismisses itself.
 */
public class CreateReceiptDialog extends Dialog{
    Activity activity;
    String table;
    Database db;
    EditText sumText;
    EditText descText;
    TextView dateText;
    Button dateButton;
    Button createButton;
    Button cancelButton;
    SimpleDateFormat formatter;
    GFunctions f;


    /**
     * The initialization function. Whenever needed, a new dialog is created via calling this
     * function.
     * @param c the Activity from which this function is called.
     * @param table the name of the table to which a new receipt is to be created.
     */
    public CreateReceiptDialog(Activity c, String table){
        super(c);
        this.activity = c;
        this.table = table;
    }

    /**
     * onCreate method is automatically called whenever an instance of this dialog is created.
     * It takes care of fetching the layout files, assigning the variables required across the
     * dialog and assigning the onClickListeners to the buttons.
     * @param savedInstanceState a bundle containing information about the instance's state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_receipt);
        db = new Database(getContext());
        f = new GFunctions(getContext());

        // The formatter takes care of representing dates in text format.
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();


        sumText = (EditText) findViewById(R.id.create_receipt_sum_field);
        descText = (EditText) findViewById(R.id.create_receipt_desc_field);
        dateText = (TextView) findViewById(R.id.create_receipt_date_field);
        dateButton = (Button) findViewById(R.id.create_receipt_date_button);
        createButton = (Button) findViewById(R.id.create_receipt_create_button);
        cancelButton = (Button) findViewById(R.id.create_receipt_cancel_button);
        // Upon creation, the dialog gets the current date as a default.
        dateText.setText(formatter.format(cal.getTime()));

        // Define the reaction for pressing the create button.
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sum = sumText.getText().toString().trim();
                String desc = descText.getText().toString().trim();
                String date = f.dateConvert(dateText.getText().toString().trim());
                /*
                    If the fields aren't empty, create a receipt with the acquired
                    data into the database. Dismiss the dialog afterwards.
                 */
                if (!sum.equals("") && !desc.equals("")) {
                    db.addReceipt(table, sum, date, desc);
                    dismiss();
                    //  Required in order to make the changes immediately visible without reloading.
                    ((ReceiptsActivity) activity).onResume();
                } else {
                    // If the sum field is empty, display an error message.
                    if (sum.equals("")){
                        Toast.makeText(getContext(),
                                getContext().getResources().getString(R.string.create_receipt_sum_empty),
                                Toast.LENGTH_SHORT).show();
                    }
                    // And if the description field is empty, display an error aswell.
                    if(desc.equals("")){
                        Toast.makeText(getContext(),
                                getContext().getResources().getString(R.string.create_receipt_desc_empty),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        // Cancel -button simply dismisses the dialog.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        /*
        Date -button opens a DatePicker, and assigns whichever date picked by the user to a
        TextView in text format.
         */
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar date = Calendar.getInstance();
                        date.set(year, monthOfYear, dayOfMonth);
                        dateText.setText(formatter.format(date.getTime()));

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

    }

}
