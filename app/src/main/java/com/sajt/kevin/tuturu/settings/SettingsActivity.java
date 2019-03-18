package com.sajt.kevin.tuturu.settings;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.sajt.kevin.tuturu.R;
import com.sajt.kevin.tuturu.audio.Magic;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsActivity extends PreferenceActivity {

    public SwitchPreference magicPref;
    static AtomicBoolean run = new AtomicBoolean(false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainSettingsFragment()).commit();


        new Thread(()-> {

            MediaPlayer mp;
            mp = MediaPlayer.create(this, R.raw.beep);

            while (true) {
                if (run.get()) {
                    if (new Magic().start()) {
                        mp.start();
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //run.set(!run.get());

    }

    public static class MainSettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            SwitchPreference magicSwitch = (SwitchPreference) findPreference("magic_key");

            if (magicSwitch != null) {
                magicSwitch.setOnPreferenceChangeListener((arg0, isMagicOnObject) -> {
                    boolean isMagicOn = (Boolean) isMagicOnObject;
                    System.out.println("isMagicOn: " + isMagicOn);
                    if (isMagicOn) {
                        run.set(isMagicOn);
                    }
                    return true;
                });
            }
        }
    }

}

