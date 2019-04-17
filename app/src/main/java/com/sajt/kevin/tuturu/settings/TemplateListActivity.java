package com.sajt.kevin.tuturu.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sajt.kevin.tuturu.R;
import com.sajt.kevin.tuturu.audio.Alchemy;
import com.sajt.kevin.tuturu.audio.Recorder;

import java.io.File;
import java.util.List;

public class TemplateListActivity extends AppCompatActivity {

    private ListView templateListView;

    private List<File> templateFiles;
    private List<String> templateNames;
    private ArrayAdapter<File> templateAdapter;
    private String newName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_list);

        templateListView = findViewById(R.id.templateListView);
        registerForContextMenu(templateListView);

        templateFiles = new Alchemy().getFiles();

        templateAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, templateFiles);

        templateListView.setAdapter(templateAdapter);

        templateListView.setOnItemClickListener((parent, view, position, id) -> {
            new Recorder(templateFiles.get(position).getName()).startPlayingTemplate();

            //System.out.println( "template name: " + templateFiles.get(position).getFileName());
        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_template, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.option_edit:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("New name");

                final EditText input = new EditText(this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    newName = input.getText().toString();
                    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                            getAbsolutePath() + "/sajt/");
                    if(dir.exists()){
                        File from = new File(dir,templateAdapter.getItem(info.position).getName());
                        File to = new File(dir, newName + ".pcm");
                        if(from.exists()) {
                            from.renameTo(to);
                            templateFiles.set(info.position, new File(dir + "/" + to.getName()));
                            templateAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(this, "Renamed to " + newName, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();

                return true;
            case R.id.option_delete:

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).
                        getAbsolutePath() + "/sajt/" + templateAdapter.getItem(info.position).getName());
                boolean isDeleted = file.delete();
                if (isDeleted) {
                    templateAdapter.remove(templateAdapter.getItem(info.position));
                } else {
                    Toast.makeText(TemplateListActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(this, "DELETE", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}
