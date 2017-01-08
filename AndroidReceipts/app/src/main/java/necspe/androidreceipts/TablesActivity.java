package necspe.androidreceipts;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * TablesActivity takes care of displaying created table entries. All the names are displayed.
 * Clicking any of the table entries opens a ReceiptsActivity -view, which contains all
 * receipts of the given table.
 */
public class TablesActivity extends AppCompatActivity {
    GFunctions f;
    private Database db;

    /**
     * onCreate is called whenever an instance of TablesActivity is created. It takes care of, for
     * example, retrieving the layout file, assigning variables and populating the toolbar.
     * @param savedInstanceState a bundle containing information of the current state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f = new GFunctions(this);
        // This allows the language changes to be immediate, no reloading needed.
        f.keepLang();
        // The database -helper class we're using to store and fetch data.
        db = new Database(this);

        // Sets the layout and assigns a toolbar.
        setContentView(R.layout.activity_tables);
        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(R.string.title_activity_tables);
        setSupportActionBar(tb);

        // Display the tables.
        renderTables();
    }

    /**
     * onResume is very similar to onCreate. It takes care of mostly the same things. Only major
     * difference is that onResume is called whenever the user "returns" to this activity, rather
     * than creating a new one. Since, for example, the language may change, the layout needs to
     * be fetched again for the change to take effect, and thus we also need to assign the toolbar
     * again and render the tables. Same when adding new tables, they need to be visible.
     */
    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_tables);
        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(R.string.title_activity_tables);
        setSupportActionBar(tb);
        renderTables();
    }

    //  This creates the toolbar 'buttons' from the definition in menu-folder.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_tables, menu);
        return true;
    }

    //  This assigns event-listeners of sorts to the 'buttons' of the toolbar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            // When "settings" is pressed go to settings...
            case R.id.settings:
                f.toSettings();
                return true;
            // and when "New Table" is pressed open a new CreateTableDialog.
            case R.id.newTable:
                CreateTableDialog d = new CreateTableDialog(TablesActivity.this);
                d.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * renderTables takes care of displaying the tables in TablesActivity. It is called in onCreate
     * and onResume functions. It fetches all found table entries from the database, creates a
     * Button for each table entry and sets their names as the buttons' text. It also assigns
     * the reactions for pushing these buttons.
     */
    private void renderTables(){
        // The LinearLayout to which we wish to add the buttons.
        LinearLayout tl = (LinearLayout) findViewById(R.id.tablesLayout);
        // Fetch the table entries from the database.
        ArrayList<String> tables = db.getAllTables();

        // For each table entry...
        for (int i = 0; i < tables.size(); i++){
            // Create a button...
            Button table = new Button(this);
            // Put the name of the table to the text field of the button...
            table.setText(tables.get(i));
            // Add the layout parameters and background...
            table.setGravity(Gravity.CENTER);
            table.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            table.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            table.setBackgroundResource(R.drawable.border);

            // Keep the name in memory! When this button is clicked, it fetches all the receipts
            // whose "Tablename" -field matches with it.
            final String name = tables.get(i);

            // set the onClickListener...
            table.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // When clicked, go to the ReceiptsActivity of this table.
                    f.toReceipts(name);
                }
            });

            // set the onLongClickListener for deleting the table...
            table.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Display a confirmation dialog for deleting the long-clicked table.
                    RemoveTableDialog dialog = new RemoveTableDialog(TablesActivity.this, name);
                    dialog.show();
                    return true;
                }
            });

            // And finally add the created button to the LinearLayout in TablesActivity layout.
            tl.addView(table);
        }

    }
}
