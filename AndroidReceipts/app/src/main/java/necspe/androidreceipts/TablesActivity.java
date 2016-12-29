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

public class TablesActivity extends AppCompatActivity {
    GFunctions f;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f = new GFunctions(this);
        f.keepLang();
        db = new Database(this);

        setContentView(R.layout.activity_tables);
        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(R.string.title_activity_tables);
        setSupportActionBar(tb);

        renderTables();
    }

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
            case R.id.settings:
                f.toSettings();
                return true;
            case R.id.newTable:
                CreateTableDialog d = new CreateTableDialog(TablesActivity.this);
                d.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void renderTables(){
        LinearLayout tl = (LinearLayout) findViewById(R.id.tablesLayout);
        ArrayList<String> tables = db.getAllTables();

        for (int i = 0; i < tables.size(); i++){
            Button table = new Button(this);
            table.setText(tables.get(i));
            table.setGravity(Gravity.CENTER);
            table.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            table.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            table.setBackgroundResource(R.drawable.border);
            final String name = tables.get(i);

            table.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f.toReceipts(name);
                }
            });

            table.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    RemoveTableDialog dialog = new RemoveTableDialog(TablesActivity.this, name);
                    dialog.show();
                    return true;
                }
            });

            tl.addView(table);
        }

    }
}
