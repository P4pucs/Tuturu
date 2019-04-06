package com.sajt.kevin.tuturu.audio;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Zandatsu {

    private Recorder recorder;

    public Zandatsu(String name) {
        recorder = new Recorder(name);

    }

    public void start() {
        byte[] audio = readAudioFile();
        byte[] audioChunk = new byte[28800];

        System.out.println("alive");
    }

    private byte[] readAudioFile() {
        //recorder.setName(name);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/sajt/" + recorder.getName()); //+ ".pcm

        byte[] byteData = new byte[(int) file.length()];
        System.out.println("file length: " + (int) file.length());

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(byteData);
        } catch (FileNotFoundException e ) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteData;
    }

    public void playAudio(){
        recorder.startPlayingRecorder();
    }

}
