package com.sajt.kevin.tuturu.settings;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sajt.kevin.tuturu.R;
import com.sajt.kevin.tuturu.audio.Alchemy;
import com.sajt.kevin.tuturu.audio.Magic;
import com.sajt.kevin.tuturu.audio.Recorder;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecorderActivity extends AppCompatActivity {

    final int REQUEST_PERMISSION_CODE = 1000;

    private Recorder recorder1;
    private static Button btnRecord1, btnPlay1;

    private static EditText nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        if (checkPermissionFromDevice()) {
            //RECORDER 111111111111111111
            recorder1 = new Recorder();

            nameText = findViewById(R.id.nameText);

            btnRecord1 = (Button)findViewById(R.id.recorder1Button);
            btnRecord1.setOnClickListener((view) -> {

                if (nameText.getText().toString().trim().length() > 0) {
                    Toast.makeText(this, "alma" ,Toast.LENGTH_SHORT).show();
                    recorder1.setName(nameText.getText().toString());
                    recorder1.startRecordForX();

                } else {
                    Toast.makeText(this, "give a name" ,Toast.LENGTH_SHORT).show();
                }
                //btnRecord1.setEnabled(false);

            });

            btnPlay1 = (Button)findViewById(R.id.play1Button);
            btnPlay1.setOnClickListener((view) -> {
                recorder1.startPlayingRaw();
            });


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
                    Toast.makeText(RecorderActivity.this,"Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecorderActivity.this,"Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }
}
