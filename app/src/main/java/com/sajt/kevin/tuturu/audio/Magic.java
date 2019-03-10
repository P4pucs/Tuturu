package com.sajt.kevin.tuturu.audio;

import android.os.Environment;
import android.util.Log;

import com.sajt.kevin.tuturu.audio.Recorder;

import java.io.File;

public class Magic {
Alchemy alchemy = new Alchemy();
Recorder recorder = new Recorder("looop");
    public void magic () {

    }

    public static void getFiles() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            System.out.println("FileName:" + files[i].getName());
        }
    }
}
