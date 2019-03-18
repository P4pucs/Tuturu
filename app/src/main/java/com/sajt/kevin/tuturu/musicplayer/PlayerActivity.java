package com.sajt.kevin.tuturu.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.sajt.kevin.tuturu.R;
import com.sajt.kevin.tuturu.settings.SettingsActivity;

public class PlayerActivity extends Activity {

    Button playBtn, settingsBtn;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        //playBtn = (Button) findViewById(R.id.playButton);
        settingsBtn = findViewById(R.id.settingsButton);
        settingsBtn.setOnClickListener((view) -> {
            startActivity(new Intent(PlayerActivity.this, SettingsActivity.class));
        });

        playBtn = findViewById(R.id.playButton);
        playBtn.setOnClickListener((view) -> {
            Toast.makeText(PlayerActivity.this,"playing", Toast.LENGTH_SHORT).show();
        });

        //Media Player

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
