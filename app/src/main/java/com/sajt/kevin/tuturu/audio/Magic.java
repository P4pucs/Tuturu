package com.sajt.kevin.tuturu.audio;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.sajt.kevin.tuturu.settings.RecorderActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Magic {

    Recorder recorder;

    public Magic() {
        recorder = new Recorder("loooop");
    }

    public void start() {
        record();
        Map<String, Boolean> comparison = compare();
        for (String s : comparison.keySet()) {
            System.out.println(s + " : " + comparison.get(s));

            if (comparison.get(s) == true) {
                System.out.println("there is a mach:");
                break;
            }

        }
    }

    public Map<String, Boolean> compare() {
        List<File> pcmFiles = getFiles();
        Map<String, Boolean> comparison = new HashMap<>();
        for (File file : pcmFiles) {
            comparison.put(file.getName(), Alchemy.start(file.getName(), recorder.getName()));
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

    public boolean saveFile(Context context, String mytext){
        Log.i("TESTE", "SAVE");
        try {
            FileOutputStream fos = context.openFileOutput("file_name"+".txt",Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(mytext);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
