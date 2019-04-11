package com.sajt.kevin.tuturu.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sajt.kevin.tuturu.R;
import com.sajt.kevin.tuturu.audio.Recorder;
import com.sajt.kevin.tuturu.audio.Zandatsu;

import java.util.Timer;
import java.util.TimerTask;

import static com.sajt.kevin.tuturu.settings.SettingsActivity.runAlchemy;

public class RecorderActivity extends AppCompatActivity {

    private static Button btnRecord1, btnPlay1;
    private static Button btnRecord2, btnPlay2, btnZandatsu;
    private static EditText nameText;
    private static TextView timerText;
    final int REQUEST_PERMISSION_CODE = 1000;
    private Recorder recorderShort;
    private Zandatsu zandatsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        if (checkPermissionFromDevice()) {
            //RECORDER Short
            recorderShort = new Recorder();

            nameText = findViewById(R.id.nameText);

            btnRecord1 = findViewById(R.id.recorder1Button);
            timerText = findViewById(R.id.TimerText);
            btnRecord1.setOnClickListener((view) -> {
                if (!runAlchemy.get()) {

                    if (nameText.getText().toString().trim().length() > 0) {
                        timerText.setText("2");
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            int seconds = 1;

                            @Override
                            public void run() {
                                if (seconds == 0) {
                                    timerText.setText("");
                                    timer.cancel();

                                } else {
                                    timerText.setText(Integer.toString(seconds--));
                                }
                            }
                        };

                        //Toast.makeText(this, "alma" ,Toast.LENGTH_SHORT).show();
                        timer.schedule(timerTask, 1000, 1000);
                        recorderShort.setName(nameText.getText().toString());
                        recorderShort.startRecorder(2);

                    } else {
                        Toast.makeText(this, "give a name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Turn of that red switch thingy", Toast.LENGTH_LONG).show();
                }

            });

            btnPlay1 = findViewById(R.id.play1Button);
            btnPlay1.setOnClickListener((view) -> {
                if (!recorderShort.getName().equals("")) {
                    recorderShort.startPlayingRecorder();
                } else {
                    Toast.makeText(this, "Record has not yet been made", Toast.LENGTH_LONG).show();
                }
            });

            //RECORDER Long

            zandatsu = new Zandatsu();

            btnRecord2 = findViewById(R.id.recorder2Button);
            btnRecord2.setText("Record");
            btnRecord2.setOnClickListener((view) -> {
                if (!runAlchemy.get()) {

                    if (nameText.getText().toString().trim().length() > 0) {
                        zandatsu.setName(nameText.getText().toString().trim());

                        if (btnRecord2.getText().toString().equals("Record")) {
                            btnRecord2.setText("Stop");
                            zandatsu.recordAudio();

                        } else if (btnRecord2.getText().toString().equals("Stop")) {
                            btnRecord2.setText("Record");
                            zandatsu.stopRecordAudio();
                        } else {
                            btnRecord2.setText("Record");
                            Toast.makeText(this, "WTF", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "give a name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Turn of that red switch thingy", Toast.LENGTH_LONG).show();
                }

            });

            btnPlay2 = findViewById(R.id.play2Button);
            btnPlay2.setText("Play");
            btnPlay2.setOnClickListener((view) -> {
                if (!zandatsu.getName().equals("")) {
                    zandatsu.playAudio();
                } else {
                    Toast.makeText(this, "Record has not yet been made", Toast.LENGTH_LONG).show();
                }
            });

            btnZandatsu = findViewById(R.id.zandatsuButton);
            btnZandatsu.setOnClickListener((view) -> {
                Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                zandatsu.start();
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
                    Toast.makeText(RecorderActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecorderActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }
}
