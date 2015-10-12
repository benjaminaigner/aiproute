package systems.byteswap.aiproute;

import android.content.Context;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

//TODO: Subclasses of PreferenceActivity must override isValidFragment(String) to verify that the Fragment class is valid! systems.byteswap.aiproute.SettingsActivity has not checked if fragment systems.byteswap.aiproute.SettingsActivity$GeneralPreferenceFragment is valid.


/**
 * Settings activity.
 *
 * Based on Android examples:
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * and <a href="http://developer.android.com/guide/topics/ui/settings.html">
 *

 Copyright (C) 2015  Benjamin Aigner

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return fragmentName.equals("systems.byteswap.aiproute.SettingsActivity$GeneralPreferenceFragment");
    }

    public static boolean isSSIDbasedActivation(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("prefCheckboxSSID", true);
    }

    public static boolean isAutostart(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("prefCheckboxAutostart",true);
    }

    public static String getInterfaceName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("prefDeviceName", "wlan0");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value. String version
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListenerString = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            switch(preference.getKey()) {
                case "prefDeviceName":
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()).edit().putString("prefDeviceName",stringValue);
                    break;
                default:
                    break;
            }

            return true;
        }
    };
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value. Boolean version
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListenerBoolean = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Boolean boolValue = Boolean.parseBoolean(value.toString());
            switch(preference.getKey()) {
                case "prefCheckboxAutostart":
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()).edit().putBoolean("prefCheckboxAutostart", boolValue);
                    break;
                case "prefCheckboxSSID":
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext()).edit().putBoolean("prefCheckboxSSID", boolValue);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     * Type of preference: String
     *
     * @see #sBindPreferenceSummaryToValueListenerString
     */
    private static void bindPreferenceSummaryToValueString(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListenerString);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListenerString.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     * Type of preference: Boolean
     *
     * @see #sBindPreferenceSummaryToValueListenerBoolean
     */
    private static void bindPreferenceSummaryToValueBoolean(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListenerBoolean);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListenerBoolean.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), true));
    }

    /**
     * This fragment shows all general preferences
     */
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            //Bind the preferences to the values
            bindPreferenceSummaryToValueBoolean(findPreference("prefCheckboxAutostart"));
            bindPreferenceSummaryToValueBoolean(findPreference("prefCheckboxSSID"));
            bindPreferenceSummaryToValueString(findPreference("prefDeviceName"));
        }
    }

    /**
     * This fragment shows an "about" dialog
     */
    public static class AboutFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // TODO: 01/10/15 create an about dialog here...
        }
    }
}
