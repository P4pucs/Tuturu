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

import com.sajt.kevin.tuturu.audio.Magic;
import com.sajt.kevin.tuturu.audio.Recorder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RecorderActivity extends AppCompatActivity {


    final int REQUEST_PERMISSION_CODE = 1000;

    private Recorder recorder1, recorder2;
    private static Button btnRecord1, btnStopRecord1, btnPlay1;
    private static Button btnRecord2, btnStopRecord2, btnPlay2;
    private static Button btnMagic;
    private static Button btnLinkStart;
    private boolean alreadyExecuted = false;
    private Thread linkStartThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        if (checkPermissionFromDevice()) {
            //RECORDER 111111111111111111
            recorder1 = new Recorder("audio1");

            btnRecord1 = (Button)findViewById(R.id.recorder1Button);
            btnRecord1.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"Recording...", Toast.LENGTH_SHORT).show();
                btnRecord1.setEnabled(false);
                btnStopRecord1.setEnabled(true);
                recorder1.startRecorder();
            });

            btnStopRecord1 = (Button)findViewById(R.id.stopRecorder1Button);
            btnStopRecord1.setEnabled(false);
            btnStopRecord1.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"stopped", Toast.LENGTH_SHORT).show();
                recorder1.stopRecording();
                btnRecord1.setEnabled(true);
                btnStopRecord1.setEnabled(false);
            });

            btnPlay1 = (Button)findViewById(R.id.play1Button);
            btnPlay1.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"playing...", Toast.LENGTH_SHORT).show();
                recorder1.startPlaying();
            });

            //RECORDER 2222222222222222222

            recorder2 = new Recorder("audio2");

            btnRecord2 = (Button)findViewById(R.id.recorder2Button);
            btnRecord2.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"Recording...", Toast.LENGTH_SHORT).show();
                btnRecord2.setEnabled(false);
                btnStopRecord2.setEnabled(true);
                recorder2.startRecorder();
            });

            btnStopRecord2 = (Button)findViewById(R.id.stopRecorder2Button);
            btnStopRecord2.setEnabled(false);
            btnStopRecord2.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"stopped", Toast.LENGTH_SHORT).show();
                recorder2.stopRecording();
                btnRecord2.setEnabled(true);
                btnStopRecord2.setEnabled(false);
            });

            btnPlay2 = (Button)findViewById(R.id.play2Button);
            btnPlay2.setOnClickListener((view) -> {
                Toast.makeText(RecorderActivity.this,"playing...", Toast.LENGTH_SHORT).show();
                recorder2.startPlaying();
            });

            // MAGIC
            btnMagic = (Button)findViewById(R.id.magicButton);
            btnMagic.setOnClickListener((view) -> {
                //Toast.makeText(RecorderActivity.this,"MAGIC", Toast.LENGTH_SHORT).show();
                //Magic.magic(recorder1.getName(), recorder2.getName());
                if (Magic.magic(recorder1.getName(), recorder2.getName())) {
                    Toast.makeText(RecorderActivity.this,"GOOD", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecorderActivity.this,"NOT GOOD", Toast.LENGTH_SHORT).show();
                }
            });

            //LINK START
            Recorder recorder = new Recorder("loooop");
            Runnable linkStart = new Runnable() {
                public void run() {
                    System.out.println("Hello world");
                }
            };
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

            linkStartThread = new Thread(() -> {
                executor.scheduleAtFixedRate(linkStart, 0, (long) 1.5, TimeUnit.SECONDS);
                alreadyExecuted = true;
            });

            btnLinkStart = (Button)findViewById(R.id.linkStartButton);
            btnLinkStart.setTag(1);
            btnLinkStart.setOnClickListener((view) -> {

                if (alreadyExecuted) {
                    final int status =(Integer) view.getTag();
                    if(status == 1) {
                        btnLinkStart.setText("Link Stop");
                        synchronized (linkStartThread) {
                            try {
                                linkStartThread.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        view.setTag(0); //pause
                    } else {
                        synchronized (linkStartThread) {
                            try {
                                linkStartThread.notifyAll();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        btnLinkStart.setText("Link Start");
                        view.setTag(1); //pause
                    }
                }else {
                    try {
                        linkStartThread.start();
                        btnLinkStart.setText("Link Stop");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
