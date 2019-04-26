package com.sajt.kevin.tuturu.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.sajt.kevin.tuturu.R;
import com.sajt.kevin.tuturu.audio.Alchemy;

import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsActivity extends PreferenceActivity {

    static AtomicBoolean runAlchemy = new AtomicBoolean(false);
    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainSettingsFragment()).commit();

        if (checkPermissionFromDevice()) {

            new Thread(() -> {

                MediaPlayer mp;
                mp = MediaPlayer.create(this, R.raw.tuturu);

                Alchemy alchemy = new Alchemy();

                while (true) {
                    if (runAlchemy.get()) {
                        if (alchemy.start()) {
                            mp.start();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
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

        } else {
            requestPermission();
        }

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SettingsActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    public static class MainSettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            SwitchPreference magicSwitch = (SwitchPreference) findPreference("magic_key");
            if (magicSwitch.isChecked()) {
                runAlchemy.set(magicSwitch.isChecked());
            } else {
                runAlchemy.set(magicSwitch.isChecked());
            }

            magicSwitch.setOnPreferenceChangeListener((arg0, isMagicOnObject) -> {
                boolean isMagicOn = (Boolean) isMagicOnObject;
                if (isMagicOn) {
                    runAlchemy.set(true);
                } else {
                    runAlchemy.set(false);
                }
                return true;
            });
        }
    }

}

