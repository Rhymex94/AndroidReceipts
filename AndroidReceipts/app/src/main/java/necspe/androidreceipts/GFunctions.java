package necspe.androidreceipts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.audiofx.BassBoost;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by santeri on 22.12.2016.
 *
 * This is for all possible functions that are needed across multiple activities.
 */
public class GFunctions {
    Context context;

    /**
     * Via giving the context as a constructor, any Activity can create its own, context specific
     * instantiation of this class for easy use of its functions.
     * @param c the context of the caller.
     */
    public GFunctions(Context c){
        this.context = c;
    }

    /**
     *  Calling this method in every onCreate -method ensures that the correct language
     *  is displayed within that activity.
     */
    public void keepLang(){
        // Fetch the default values from preferences...
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        //  Assign the desired language to the Locale...
        String lang = sharedPref.getString(SettingsActivity.SettingsFragment.LANG, "");
        Locale loc = new Locale(lang);
        //  Set this new Locale as default...
        Locale.setDefault(loc);
        //  And finally include it in a new configuration and make the updates.
        Configuration config = new Configuration();
        config.setLocale(loc);
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }


    /**
     * makeList creates an ArrayList from the data acquired by a Cursor object. Used namely in
     * Database -class' getReceipts method.
     * @param res the cursor -object whose data is to be listed.
     * @param target the target ArrayList into which the data should be put into.
     */
    public void makeList(Cursor res, ArrayList<ArrayList<String>> target){
        res.moveToFirst();
        while(!res.isAfterLast()){
            ArrayList<String> current = new ArrayList<>();
            current.add(res.getString(res.getColumnIndex("sum")));
            current.add(res.getString(res.getColumnIndex("date")));
            current.add(res.getString(res.getColumnIndex("desc")));
            current.add(res.getString(res.getColumnIndex("checks")));
            target.add(current);
            res.moveToNext();
        }
        res.close();
    }

    //  Used to intent from a given view to the SettingsActivity.
    public void toSettings(){
        Intent settings = new Intent(context, SettingsActivity.class);
        context.startActivity(settings);
    }

    // Used to intent from a given view to the TablesActivity.
    public void toTables(){
        System.out.println("toReceipts called!");
        Intent tables = new Intent(context, TablesActivity.class);
        context.startActivity(tables);
    }

    /**
     * Used to intent from a given view to the ReceiptsActivity.
     * @param tablename the name of the table whose receipts are to be shown in ReceiptsActivity.
     */
    public void toReceipts(String tablename){
        Intent receipts = new Intent(context, ReceiptsActivity.class);
        receipts.putExtra("tablename", tablename);
        context.startActivity(receipts);
    }

    /**
     * dateConvert converts the textual presentation of the date into another. Namely, it changes
     * the presentation of YYYY-MM-DD to DD-MM-YYYY.
     * @param date
     * @return
     */
    public String dateConvert(String date){
        String out = date.split("-")[2] + "-" + date.split("-")[1] + "-" + date.split("-")[0];
        return out;
    }

}
