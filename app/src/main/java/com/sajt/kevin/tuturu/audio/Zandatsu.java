package com.sajt.kevin.tuturu.audio;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Zandatsu {

    private Recorder recorder;
    private String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + recorder.getName();



    public Zandatsu() {
        recorder = new Recorder();
    }

    private Byte[] readFile() {
        File file = new File(filePath);
        try {
            FileInputStream fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }
}
