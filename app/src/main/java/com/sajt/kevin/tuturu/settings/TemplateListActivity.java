package com.sajt.kevin.tuturu.settings;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sajt.kevin.tuturu.R;
import com.sajt.kevin.tuturu.audio.Magic;

import java.io.File;
import java.util.List;

public class TemplateListActivity extends AppCompatActivity {

    private ListView templateListView;
    private MediaPlayer mp;

    private List<File> templates;
    private ArrayAdapter<File> templateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_list);

        templateListView = findViewById(R.id.templateListView);
        templates = new Magic().getFiles();
        templateAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, templates);
        templateListView.setAdapter(templateAdapter);
    }
}
