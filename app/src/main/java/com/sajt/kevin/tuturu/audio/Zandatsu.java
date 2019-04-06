package com.sajt.kevin.tuturu.audio;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Zandatsu {

    private Recorder recorder;
    private FileOutputStream fileOutputStream;

    public Zandatsu(String name) {
        recorder = new Recorder(name);

    }

    public void start() {
        byte[] audio = null;
        byte[] audioChunk = new byte[14400]; //28800
        try {
            audio = readAudioFile();
            fileOutputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                    getAbsolutePath() + "/sajt/" + recorder.getName() + "cut.pcm");

            for (int i=0;i<audioChunk.length;i++) {
                audioChunk[i] = audio[i];
            }
            fileOutputStream.write(audioChunk, 0, audioChunk.length);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("alive");
    }

    public String getName(){
        return recorder.getName();
    }

    private byte[] readAudioFile() throws FileNotFoundException {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/sajt/" + recorder.getFileName());

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

    public void playAudio() {
        recorder.startPlayingRecorder();
    }

    public void recordAudio() {
        recorder.startRecorder();
    }

    public void stopRecordAudio() {
        recorder.stopRecorder();
    }

    public void setName(String name) {
        recorder.setName(name);
    }

}
