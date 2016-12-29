package necspe.androidreceipts;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by santeri on 29.12.2016.
 */
public class RemoveReceiptDialog extends Dialog {
    Activity activity;
    String checks;
    Database db;
    Button yes;
    Button no;

    public RemoveReceiptDialog(Activity c, String checks){
        super(c);
        this.activity = c;
        this.checks = checks;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_remove_receipt);

        db = new Database(getContext());

        yes = (Button) findViewById(R.id.remove_receipt_yes);
        no = (Button) findViewById(R.id.remove_receipt_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.removeReceipt(checks);
                dismiss();
                ((ReceiptsActivity) activity).onResume();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
