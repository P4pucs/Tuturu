package com.sajt.kevin.tuturu.audio;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Alchemy {

    private Recorder recorder;

    public Alchemy() { recorder = new Recorder("looop"); }

    public boolean start() {

        boolean match = false;
        record();
        Map<String, Boolean> comparison = compare();
        for (String s : comparison.keySet()) {
            //System.out.println(s + " : " + comparison.get(s));
            if (comparison.get(s)) {
                match = true;
                break;
            }
        }
        return match;
    }

    private Map<String, Boolean> compare() {
        List<File> pcmFiles = getFiles();
        Map<String, Boolean> comparison = new HashMap<>();
        for (File file : pcmFiles) {
            comparison.put(file.getName(), Magic.start(file.getName(), recorder.getName() + ".pcm"));
            //System.out.println("file1: " + file.getName() + " file2: " + recorder.getFileName() + " file2.1: " + recorder.getName());
        }
        return comparison;
    }

    private void record() {
        recorder.startRecorder(2);
    }

    public List<File> getFiles() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                getAbsolutePath() + "/sajt/";

        File directory = new File(path);
        File[] allFiles = directory.listFiles();
        List<File> pcmFiles = new ArrayList<>();

        for (File allFile : allFiles) {
            String[] splFileName = allFile.getName().split("\\.");
            if (splFileName.length > 1) {
                if (splFileName[1].toLowerCase().equals("pcm") && !allFile.getName().equals(recorder.getName() + ".pcm")) {
                    pcmFiles.add(allFile);
                }
            }
        }

        return pcmFiles;
    }

}
