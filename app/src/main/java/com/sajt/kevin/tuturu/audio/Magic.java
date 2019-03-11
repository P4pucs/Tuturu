package com.sajt.kevin.tuturu.audio;

import android.os.Environment;
import android.util.Log;

import com.sajt.kevin.tuturu.audio.Recorder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Magic {

    Recorder recorder;

    public Magic() {
        recorder = new Recorder("loooop");
    }

    public void start() {
        record();
        Map<String, Boolean> comparison = compare();
        for (String s : comparison.keySet()) {
            System.out.println(s + " faszom: " + comparison.get(s));
        }
    }

    public Map<String, Boolean> compare() {
        List<File> pcmFiles = getFiles();
        Map<String, Boolean> comparison = new HashMap<>();
        for (File pedo : pcmFiles) {
            comparison.put(pedo.getName(), Alchemy.alchemy(pedo.getName(), recorder.getName()));
        }
        return comparison;
    }

    public void record() {
        recorder.startRecordForX();
    }

    public List<File> getFiles() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File directory = new File(path);
        File[] allFiles = directory.listFiles();
        List<File> pcmFiles = new ArrayList<>();

        for (int i = 0; i < allFiles.length; i++) {
            String[] splFileName = allFiles[i].getName().split("\\.");
            if (splFileName.length > 1) {
                if (splFileName[1].toLowerCase().equals("pcm") && !allFiles[i].getName().equals(recorder.getName())) {
                    pcmFiles.add(allFiles[i]);
                }
            }
        }

        return pcmFiles;
    }
}
