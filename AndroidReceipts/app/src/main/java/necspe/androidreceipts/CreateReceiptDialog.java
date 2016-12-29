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


    public CreateReceiptDialog(Activity c, String table){
        super(c);
        this.activity = c;
        this.table = table;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_receipt);
        db = new Database(getContext());
        f = new GFunctions(getContext());

        formatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();


        sumText = (EditText) findViewById(R.id.create_receipt_sum_field);
        descText = (EditText) findViewById(R.id.create_receipt_desc_field);
        dateText = (TextView) findViewById(R.id.create_receipt_date_field);
        dateButton = (Button) findViewById(R.id.create_receipt_date_button);
        createButton = (Button) findViewById(R.id.create_receipt_create_button);
        cancelButton = (Button) findViewById(R.id.create_receipt_cancel_button);
        dateText.setText(formatter.format(cal.getTime()));

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sum = sumText.getText().toString().trim();
                String desc = descText.getText().toString().trim();
                String date = f.dateConvert(dateText.getText().toString().trim());
                if (!sum.equals("") && !desc.equals("")) {
                    db.addReceipt(table, sum, date, desc);
                    dismiss();
                    ((ReceiptsActivity) activity).onResume();
                } else {
                    if (sum.equals("")){
                        Toast.makeText(getContext(),
                                getContext().getResources().getString(R.string.create_receipt_sum_empty),
                                Toast.LENGTH_SHORT).show();
                    }
                    if(desc.equals("")){
                        Toast.makeText(getContext(),
                                getContext().getResources().getString(R.string.create_receipt_desc_empty),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    //@Override
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
