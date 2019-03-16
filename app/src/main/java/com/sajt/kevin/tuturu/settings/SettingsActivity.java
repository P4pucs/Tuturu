package com.sajt.kevin.tuturu.settings;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import com.sajt.kevin.tuturu.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainSettingsFragment()).commit();
    }

    public static class MainSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

//            bindSummaryValue(findPreference("key_full_name"));
//            bindSummaryValue(findPreference("key_email"));
//            bindSummaryValue(findPreference("key_sleep_timer"));
//            bindSummaryValue(findPreference("key_music_quality"));
//            bindSummaryValue(findPreference("key_notifications_ringtone"));
        }
    }

    private static void bindSummaryValue(Preference preference) {
        preference.setOnPreferenceChangeListener(listener);
        listener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener listener = (preference, newValue) -> {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            // set the summary to reflect the new value
            preference.setSummary(index >= 0
                    ? listPreference.getEntries()[index]
                    : null);
        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(stringValue);
        } else if (preference instanceof RingtonePreference) {
            if (TextUtils.isEmpty(stringValue)) {
                // no ringtone
                preference.setSummary("Silent");
            } else {
                Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));

                if (ringtone == null) {
                    //clear the summary
                    preference.setSummary("choose notification ringtone");
                } else {
                    String name = ringtone.getTitle(preference.getContext());
                    preference.setSummary(name);
                }
            }
        }
        return true;
    };
}
