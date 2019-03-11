package com.sajt.kevin.tuturu.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.sajt.kevin.tuturu.R;
import com.sajt.kevin.tuturu.RecorderActivity;

public class PlayerActivity extends Activity {

    Button playBtn, settingsBtn;
    SeekBar elapsedTimeSeekBar;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        //playBtn = (Button) findViewById(R.id.playButton);
        settingsBtn = findViewById(R.id.settingsButton);
        settingsBtn.setOnClickListener((view) -> {
            startActivity(new Intent(PlayerActivity.this, RecorderActivity.class));
        });

        playBtn = findViewById(R.id.playButton);
        playBtn.setOnClickListener((view) -> {
            Toast.makeText(PlayerActivity.this,"playing", Toast.LENGTH_SHORT).show();
        });

        //Media Player
        //mp = MediaPlayer.create(this, R.raw.music)
    }



}
