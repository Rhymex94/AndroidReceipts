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


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //  When created, we want to present the current settings.
            addPreferencesFromResource(R.xml.preferences);
            ListPreference listPref = (ListPreference) findPreference(LANG);
            if (listPref.getValue() != null){
                listPref.setSummary(listPref.getEntry());
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

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

