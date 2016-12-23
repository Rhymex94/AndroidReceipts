package necspe.androidreceipts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ReceiptsActivity extends AppCompatActivity {
    GFunctions f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts);

        f = new GFunctions(this);


        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(R.string.title_activity_receipts);
        setSupportActionBar(tb);

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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
