package com.sajt.kevin.tuturu.audio;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Magic {

    private Recorder recorder;

    public Magic() { recorder = new Recorder("loooop"); }

    public boolean start() {

        boolean match = false;
        record();
        Map<String, Boolean> comparison = compare();
        for (String s : comparison.keySet()) {
            //System.out.println(s + " : " + comparison.get(s));
            if (comparison.get(s) == true) {
                match = true;
                break;
            }
        }
        return match;
    }

    public Map<String, Boolean> compare() {
        List<File> pcmFiles = getFiles();
        Map<String, Boolean> comparison = new HashMap<>();
        for (File file : pcmFiles) {
            comparison.put(file.getName(), Alchemy.start(file.getName(), recorder.getFileName()));
        }
        return comparison;
    }

    public void record() {
        recorder.startRecordForX();
    }

    public List<File> getFiles() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/sajt";

        File directory = new File(path);
        File[] allFiles = directory.listFiles();
        List<File> pcmFiles = new ArrayList<>();

        for (int i = 0; i < allFiles.length; i++) {
            String[] splFileName = allFiles[i].getName().split("\\.");
            if (splFileName.length > 1) {
                if (splFileName[1].toLowerCase().equals("pcm") && !allFiles[i].getName().equals(recorder.getFileName())) {
                    pcmFiles.add(allFiles[i]);
                }
            }
        }

        return pcmFiles;
    }

}
