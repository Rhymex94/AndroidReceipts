package necspe.androidreceipts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.audiofx.BassBoost;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

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

    //  Used to intent from a given view to the SettingsActivity.
    public void toSettings(){
        Intent settings = new Intent(context, SettingsActivity.class);
        context.startActivity(settings);
    }

    public void toReceipts(){
        System.out.println("toReceipts called!");
        Intent receipts = new Intent(context, ReceiptActivity.class);
        context.startActivity(receipts);
    }



}