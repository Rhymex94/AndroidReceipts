package necspe.androidreceipts;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by santeri on 23.12.2016.
 */
public class CreateTableDialog extends Dialog  {
    Activity activity;
    Dialog dialog;
    Button confirm, cancel;
    Database db;
    EditText et;

    public CreateTableDialog(Activity c){
        super(c);
        this.activity = c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_table);
        db = new Database(getContext());
        et = (EditText) findViewById(R.id.table_name_field);
        confirm = (Button) findViewById(R.id.create_table_confirm);
        cancel = (Button) findViewById(R.id.create_table_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et.getText().toString().trim();
                if (!name.equals("")) {
                    db.addTable(name);
                    dismiss();
                    ((TablesActivity)activity).onResume();
                } else {
                    Toast.makeText(getContext(),
                            getContext().getResources().getString(R.string.empty_name_notification),
                                    Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
