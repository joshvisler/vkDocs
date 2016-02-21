package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vkdocs.oceanminded.vkdocs.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChoseFile extends AppCompatActivity {
    private String path;
    private String prevPath;
    ListView directoryList;
    private static final int PICKFILE_RESULT_CODE = 5;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_file);
        directoryList = (ListView)findViewById(R.id.directoty_list);
        path = "/storage";
        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        updateList(path);
        // Read all files sorted into the values-array
        directoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = (String) directoryList.getAdapter().getItem(position);
                if (path.endsWith(File.separator)) {
                    filename = path + filename;
                } else {
                    filename = path + File.separator + filename;
                }
                if (new File(filename).isDirectory()) {
                    updateList(filename);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("file", filename);
                    setResult(RESULT_OK, intent);
                    finish();
                    Toast.makeText(getApplicationContext(), filename + " is not a directory", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateList(String getPath){
        path= getPath;
        setTitle(path);

        // Read all files sorted into the values-array
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".")) {
                    values.add(file);
                }
            }
        }
        //Collections.sort(values);
        // Put the data into the list
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);
        directoryList.setAdapter(adapter);
    }
}
