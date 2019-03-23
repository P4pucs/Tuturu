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
import com.sajt.kevin.tuturu.audio.Magic;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsActivity extends PreferenceActivity {

    final int REQUEST_PERMISSION_CODE = 1000;

    static AtomicBoolean run = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainSettingsFragment()).commit();

        if (checkPermissionFromDevice()) {
        //RECORDER 111111111111111111

        new Thread(()-> {

            MediaPlayer mp;
            mp = MediaPlayer.create(this, R.raw.beep);

            Magic magic = new Magic();

            while (true) {
                if (run.get()) {
                    if (magic.start()) {
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

        } else {
            requestPermission();
        }

    }

    public static class MainSettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            SwitchPreference magicSwitch = (SwitchPreference) findPreference("magic_key");

            if (magicSwitch.isEnabled()) {
                run.set(true);
                System.out.println("TRUE Blyat");
            } else {
                System.out.println(" FALSE Blyat");
                run.set(false);
            }

            if (magicSwitch != null) {
                magicSwitch.setOnPreferenceChangeListener((arg0, isMagicOnObject) -> {
                    boolean isMagicOn = (Boolean) isMagicOnObject;
                    System.out.println("isMagicOn: " + isMagicOn);
                    if (isMagicOn) {
                        run.set(true);
                    } else {
                        run.set(false);
                    }
                    return true;
                });
            }
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
                    Toast.makeText(SettingsActivity.this,"Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this,"Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

}

