package com.sajt.kevin.tuturu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.sajt.kevin.tuturu.audio.Alchemy;
import com.sajt.kevin.tuturu.audio.Recorder;

public class RecorderActivity extends AppCompatActivity {


    final int REQUEST_PERMISSION_CODE = 1000;

    private Recorder recorder1, recorder2;
    private static Button btnRecord1, btnPlay1;
    private static Button btnRecord2, btnPlay2;
    private static Button btnAlchemy;
    private static Button btnLinkStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        if (checkPermissionFromDevice()) {
            //RECORDER 111111111111111111
            recorder1 = new Recorder("audio1");

            btnRecord1 = (Button)findViewById(R.id.recorder1Button);
            btnRecord1.setOnClickListener((view) -> {
                recorder1.startRecordForX();

            });

            btnPlay1 = (Button)findViewById(R.id.play1Button);
            btnPlay1.setOnClickListener((view) -> {
                recorder1.startPlaying();
            });

            //RECORDER 2222222222222222222
            recorder2 = new Recorder("audio2");

            btnRecord2 = (Button)findViewById(R.id.recorder2Button);
            btnRecord2.setOnClickListener((view) -> {
                recorder2.startRecordForX();
            });

            btnPlay2 = (Button)findViewById(R.id.play2Button);
            btnPlay2.setOnClickListener((view) -> {
                recorder2.startPlaying();
            });

            // Alchemy
            btnAlchemy = (Button)findViewById(R.id.alchemyButton);
            btnAlchemy.setOnClickListener((view) -> {
                if (Alchemy.alchemy(recorder1.getName(), recorder2.getName())) {
                    Toast.makeText(RecorderActivity.this,"GOOD", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecorderActivity.this,"NOT GOOD", Toast.LENGTH_SHORT).show();
                }

            });

            //LINK START
            Recorder recorder = new Recorder("loooop");

            btnLinkStart = (Button)findViewById(R.id.linkStartButton);
            btnLinkStart.setOnClickListener((view) -> {

//                while (isAutoRunning) {
//                    recorder.startRecordForX();
//                    if (recorder.isRecordingDone()) {
//                        if (Alchemy.alchemy(recorder1.getName(), recorder.getName())) {
//                            Toast.makeText(RecorderActivity.this,"GOOD", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(RecorderActivity.this,"NOT GOOD", Toast.LENGTH_SHORT).show();
//                        }
//                        recorder.setRecordingDone(false);
//
//                    }
//                }
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
