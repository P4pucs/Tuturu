package com.sajt.kevin.tuturu.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.SeekBar;

import com.sajt.kevin.tuturu.R;

public class PlayerActivity extends Activity {

    Button playBtn;
    SeekBar positonBar;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        playBtn = (Button) findViewById(R.id.playButton);


        //Media Player
        //mp = MediaPlayer.create(this, R.raw.music)
    }

}
