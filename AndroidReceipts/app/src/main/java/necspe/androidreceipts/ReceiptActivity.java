package necspe.androidreceipts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ReceiptActivity extends AppCompatActivity {
    GFunctions f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f = new GFunctions(this);
        f.keepLang();

        setContentView(R.layout.activity_receipt);
        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(R.string.title_activity_receipts);
        setSupportActionBar(tb);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_receipt);
        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(R.string.title_activity_receipts);
        setSupportActionBar(tb);
    }

    //  This creates the toolbar 'buttons' from the definition in menu-folder.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    //  This assigns event-listeners of sorts to the 'buttons' of the toolbar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                f.toSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
