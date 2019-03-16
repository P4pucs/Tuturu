package com.sajt.kevin.tuturu.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sajt.kevin.tuturu.R;
import com.sajt.kevin.tuturu.audio.Magic;
import com.sajt.kevin.tuturu.audio.Recorder;

import java.io.File;
import java.util.List;

public class TemplateListActivity extends AppCompatActivity {

    private ListView templateListView;

    private List<File> templateFiles;
    private List<String> templateNames;
    private ArrayAdapter<File> templateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_list);

        templateListView = findViewById(R.id.templateListView);
        registerForContextMenu(templateListView);

        templateFiles = new Magic().getFiles();

        templateAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, templateFiles);
        templateListView.setAdapter(templateAdapter);

        templateListView.setOnItemClickListener((parent, view, position, id) -> {
            new Recorder(templateFiles.get(position).getName()).startPlaying();

            //System.out.println( "template name: " + templateFiles.get(position).getName());
        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_template, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_delete:
                Toast.makeText(this, "DELETE", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_edit:
                Toast.makeText(this, "Ki az az edit?", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}