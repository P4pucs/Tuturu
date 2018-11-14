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

import com.sajt.kevin.tuturu.audio.Recorder;

public class RecorderActivity extends AppCompatActivity {

    //private Player player = new Player();
    private Recorder recorder;


    final int REQUEST_PERMISSION_CODE = 1000;

    Button btnRecord, btnStopRecord, btnPlay, btnMagic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        if (checkPermissionFromDevice()) {
            recorder = new Recorder();

            btnRecord = (Button)findViewById(R.id.recorderButton);
            btnRecord.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"Recording...", Toast.LENGTH_SHORT).show();
                btnRecord.setEnabled(false);
                btnStopRecord.setEnabled(true);
                recorder.startRecorder();
            });

            btnStopRecord = (Button)findViewById(R.id.stopRecorderButton);
            btnStopRecord.setEnabled(false);
            btnStopRecord.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"stopped", Toast.LENGTH_SHORT).show();
                recorder.stopRecording();
                btnRecord.setEnabled(true);
                btnStopRecord.setEnabled(false);
            });

            btnPlay = (Button)findViewById(R.id.playButton);
            btnPlay.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"playing...", Toast.LENGTH_SHORT).show();
                recorder.startPlaying();
            });

            btnMagic = (Button)findViewById(R.id.magicButton);
            btnMagic.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"MAGIC", Toast.LENGTH_SHORT).show();
                recorder.magic();
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
