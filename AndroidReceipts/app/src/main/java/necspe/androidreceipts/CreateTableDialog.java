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
 * CreateTableDialog is a dialog displayed whenever the user intends to create a new table for
 * storing and tracking receipts. The dialog asks for a name, which will then be used to access
 * the table and its receipts. Once the table is added to the database, the dialog dismisses itself.
 */
public class CreateTableDialog extends Dialog  {
    Activity activity;
    Button confirm, cancel;
    Database db;
    EditText et;

    /**
     * CreateTableDialog is the initializer: it is used to create a new CreateTableDialog from
     * elsewhere the app.
     * @param c the activity from which this function is called.
     */
    public CreateTableDialog(Activity c){
        super(c);
        this.activity = c;
    }

    /**
     * onCreate is a function that is called automatically whenever an instance of this dialog is
     * created. It takes care of fetching the layout file, assigning the variables and assigning
     * the onClickListener's to the buttons.
     * @param savedInstanceState a bundle containing information of the instance's state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create_table);

        db = new Database(getContext());
        et = (EditText) findViewById(R.id.table_name_field);
        confirm = (Button) findViewById(R.id.create_table_confirm);
        cancel = (Button) findViewById(R.id.create_table_cancel);

        // Sets the action for clicking the confirm button.
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et.getText().toString().trim();
                // If the name -field isn't empty, create a database entry with that name.
                if (!name.equals("")) {
                    db.addTable(name);
                    // Finally dismiss the dialog.
                    dismiss();
                    // Required to make the changes immediately visible without reloading.
                    ((TablesActivity)activity).onResume();
                } else {
                    // In case the name -field is empty, displays an error.
                    Toast.makeText(getContext(),
                            getContext().getResources().getString(R.string.empty_name_notification),
                                    Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Cancel -button simply dismisses the dialog.
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
