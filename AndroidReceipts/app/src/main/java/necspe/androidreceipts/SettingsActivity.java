package necspe.androidreceipts;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

/**
 * SettingsActivity takes care of the application settings. Most of the work is actually done by
 * the SettingsFragment, defined later in this file. SettingsActivity actually only offers the
 * 'template' for the fragment to be inserted into.
 */
public class SettingsActivity extends AppCompatActivity {
    SettingsFragment curr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        /**
         * We need a tag to be able to identify the created fragment, so we can replace it with a
         * new one when, for example, language is changed. Otherwise the changes would become
         * visible only after revisiting the settings.
         */
        curr = new SettingsFragment();
        System.out.println("Placing fragment");
        getFragmentManager().beginTransaction().replace(R.id.framel, curr, "curr").commit();
    }


    /**
     * SettingsFragment is the actual 'GUI' for managing the settings. It is inserted to its
     * parents FrameLayout that inhabits the entire Activity.
     */
    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        public static final String LANG = "language";


        /**
         * onCreate is a function that is automatically called whenever an instance of
         * SettingsFragment is created. It takes care of populating the view with the available
         * options.
         * @param savedInstanceState
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //  When created, we want to present the current settings.
            addPreferencesFromResource(R.xml.preferences);
            ListPreference listPref = (ListPreference) findPreference(LANG);
            // Which language is currently being used? Display that language.
            if (listPref.getValue() != null){
                listPref.setSummary(listPref.getEntry());
            }
        }

        /**
         * onResume works similarly with the onCreate, except it is called whenever the user returns
         * to an instance of SettingsFragment that has been created earlier. It also registers an
         * event listener, that reacts to SharedPreference -changes.
         */
        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        /**
         * Like onCreate and onResume, is called whenever a specific action has been carried out.
         * In this case, whenever the user leaves the SettingsFragment without finishing it.
         * In contrary to onResume, where we register an event listener, here we unregister it.
         */
        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }


        /**
         * This is an event listener for any changes made to the sharedPreferences, i.e. it reacts
         * when any of the settings have been modified. It first checks which setting was modified
         * using its key parameter, so it can react accordingly.
         * @param sharedPreferences is the 'storage' for application settings.
         * @param key is the identifier for which setting was modified.
         */
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
            if (key.equals(LANG)){
                String str = sharedPreferences.getString(key, "");
                setLocale(str);
                ListPreference listPref = (ListPreference) findPreference(LANG);
                listPref.setSummary(listPref.getEntry());
                SettingsFragment temp = (SettingsFragment) getActivity().getFragmentManager().findFragmentByTag("curr");
                // In order to get the changes visible in the SettingsFragment itself, we need to
                // replace it with a new, updated one.
                getFragmentManager().beginTransaction().remove(temp)
                        .replace(R.id.framel, new SettingsFragment(), "curr").commit();
            }
        }

        /**
         * setLocale is a support function for onSharedPreferenceChanged -listener function:
         * it is used to change the locale according to the language the user has picked.
         * @param lang a string containing the information of which language was chosen.
         */
        public void setLocale(String lang){
            Locale loc = new Locale(lang);
            Locale.setDefault(loc);
            Configuration config = new Configuration();
            config.setLocale(loc);
            this.getActivity().getApplication().getResources().updateConfiguration(config, null);

        }
    }

}

