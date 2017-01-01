package necspe.androidreceipts;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by santeri on 29.12.2016.
 * RemoveReceiptDialog is a dialog to be displayed whenever the user wishes to remove a receipt from
 * the database. It basically just asks for a confirmation whether the remove request was
 * intentional or not.
 */
public class RemoveReceiptDialog extends Dialog {
    Activity activity;
    String checks;
    Database db;
    Button yes;
    Button no;

    /**
     * This is the initialization function: it is called from elsewhere the application, namely
     * when the user long-clicks a receipt.
     * @param c the Activity from which the function is called.
     * @param checks is the checksum of the receipt to be removed.
     */
    public RemoveReceiptDialog(Activity c, String checks){
        super(c);
        this.activity = c;
        this.checks = checks;
    }

    /**
     * onCreate is automatically called whenever an instance of RemoveReceiptDialog is created.
     * It takes care of loading the layout file, assigning variables and assigning the
     * onClickListeners to the buttons.
     * @param savedInstanceState a bundle containing data about the instance state.
     */
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_remove_receipt);

        db = new Database(getContext());

        yes = (Button) findViewById(R.id.remove_receipt_yes);
        no = (Button) findViewById(R.id.remove_receipt_no);

        // Sets the "yes" button to remove the receipt from the database. Dismiss the dialog after.
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.removeReceipt(checks);
                dismiss();
                // This allows for the changes to be immediately visible, without reloading.
                ((ReceiptsActivity) activity).onResume();
            }
        });

        // Sets the "no" button to simply dismiss the dialog
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
