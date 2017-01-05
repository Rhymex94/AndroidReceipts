package necspe.androidreceipts;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by santeri on 29.12.2016.
 * RemoveTableDialog is a dialog to be displayed whenever the user wishes to remove a specific
 * table -entry from the database. It basically just asks for confirmation for removing the
 * Table the user had LongClicked. Removing a table -entry also removes any and all receipts
 * associated with the given table.
 */
public class RemoveTableDialog extends Dialog {
    Activity activity;
    String name;
    Database db;
    Button yes;
    Button no;

    /**
     * This is the initialization function: it is called from elsewhere the application, namely
     * when the user long-clicks a table.
     * @param c the Activity from which the function is being called.
     * @param name the name of the Table to be removed.
     */
    RemoveTableDialog(Activity c, String name){
        super(c);
        this.activity = c;
        this.name = name;
    }

    /**
     * onCreate is called automatically whenever an instance of RemoveTableDialog is created.
     * It takes care of loading the layout file, assigning variables and assigning the
     * OnClickListeners to the layout's buttons.
     * @param savedInstanceState a bundle containing data about the instance state.
     */
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_remove_table);

        db = new Database(getContext());

        yes = (Button) findViewById(R.id.remove_table_yes);
        no = (Button) findViewById(R.id.remove_table_no);


        // Set the yes-button to remove the table from the database.
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.removeTable(name);
                dismiss();
                // This allows the changes to be immediately visible in the parent view.
                ((TablesActivity) activity).onResume();
            }
        });

        // Set the no-button to simply dismiss the created dialog.
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
