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
public class RemoveTableDialog extends Dialog {
    Activity activity;
    String name;
    Database db;
    Button yes;
    Button no;

    RemoveTableDialog(Activity c, String name){
        super(c);
        this.activity = c;
        this.name = name;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_remove_table);

        db = new Database(getContext());

        yes = (Button) findViewById(R.id.remove_table_yes);
        no = (Button) findViewById(R.id.remove_table_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.removeTable(name);
                dismiss();
                ((TablesActivity) activity).onResume();
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
