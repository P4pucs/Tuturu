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
    private int samlpleSize = 32000;

    public Zandatsu(String name) {
        recorder = new Recorder(name);
    }

    public Zandatsu() {
        recorder = new Recorder();
    }

    public boolean start() {
        try {
            byte[] audio = readAudioFile();

            int steps = (int) Math.floor(audio.length / samlpleSize);
            if (steps >= 1) {
                for (int i = 0; i < steps; i++) {
                    fileOutputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                            getAbsolutePath() + "/sajt/" + recorder.getName() + "(" + i + ").pcm");
                    byte[] audioChunk = new byte[samlpleSize];
                    int chunkIndex = 0;
                    for (int y = samlpleSize * i; y < samlpleSize * (i + 1); y++) {
                        audioChunk[chunkIndex] = audio[y];
                        chunkIndex++;
                    }
                    fileOutputStream.write(audioChunk, 0, samlpleSize);
                }
                fileOutputStream.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getName() {
        return recorder.getName();
    }

    public void setName(String name) {
        recorder.setName(name);
    }

    private byte[] readAudioFile() throws FileNotFoundException {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                getAbsolutePath() + "/sajt/alma/" + recorder.getName() + ".pcm");

        byte[] byteData = new byte[(int) file.length()];

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(byteData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteData;
    }

    public void playAudio() {
        try {
            recorder.PlayAudioFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/sajt/alma/" + getName() + ".pcm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void recordAudio() {
        recorder.startRecorder();
    }

    public boolean stopRecordAudio() {
        if(recorder.stopRecorder())
            return true;
        else
            return false;
    }

}
