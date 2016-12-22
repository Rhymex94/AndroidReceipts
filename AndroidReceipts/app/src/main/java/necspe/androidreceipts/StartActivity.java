package necspe.androidreceipts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

/**
 * StartActivity is a simple welcome message for the user. It also serves as a placeholder for
 * possible, later implementation of accessing the data in a remote server.
 */

public class StartActivity extends AppCompatActivity {
    Button cont;
    GFunctions f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * This makes sure that the language settings are updated even when just returning to this
         * view via the back-button. Check the class GFunctions for details.
         */
        f = new GFunctions(this);
        f.keepLang();

        //  Fetches the GUI defined in res-folder and assigns the toolbar to it.
        setContentView(R.layout.activity_start);
        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);

        //  Set the click-listener to the continue button in the view.
        cont = (Button) findViewById(R.id.continue_button);
        setOnClickToCont();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_start);
        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        cont = (Button) findViewById(R.id.continue_button);
        setOnClickToCont();
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

    private void setOnClickToCont(){
        cont.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                f.toReceipts();
            }
        });
    }
}
