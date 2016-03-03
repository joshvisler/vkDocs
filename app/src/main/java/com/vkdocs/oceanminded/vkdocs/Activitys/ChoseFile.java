package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vkdocs.oceanminded.vkdocs.Adapters.ChooserRVAdapter;
import com.vkdocs.oceanminded.vkdocs.Adapters.RVAdapter;
import com.vkdocs.oceanminded.vkdocs.Adapters.RecyclerItemClickListener;
import com.vkdocs.oceanminded.vkdocs.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChoseFile extends AppCompatActivity {
    private String path;
    private String prevPath;
    ListView directoryList;
    private RecyclerView folderRV;
    private ChooserRVAdapter adapter;
    private ArrayList<String> data;

    @Override
    public void onBackPressed() {
        if(path != "storage/") {
            if (path.contains("/")) {
                path = path.substring(0, path.lastIndexOf("/"));
                createData(path);
            } else finish();
        }else finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_file);
        folderRV = (RecyclerView) findViewById(R.id.folder_list);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        folderRV.setLayoutManager(llm);
        data = new ArrayList<>();
        createData("storage/");
        folderRV.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.changeData(data);
                Log.d("CLICK",data.get(position));
                String filename = data.get(position);
                Log.d("Clicked path", data.get(position));
                if (path.endsWith(File.separator)) {
                    filename = path + filename;
                } else {
                    filename = path + File.separator + filename;
                }
                if (new File(filename).isDirectory()) {
                    createData(filename);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("file", filename);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }));
        /*adapter.SetOnItemClickListener(new ChooserRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //createData(adapter.getMpath());
                //adapter = new ChooserRVAdapter(path);
                // setTitle(path);

                String filename = data.get(position);
                Log.d("Clicked path", data.get(position));
                if (path.endsWith(File.separator)) {
                    filename = path + filename;
                } else {
                    filename = path + File.separator + filename;
                }
                if (new File(filename).isDirectory()) {
                    createData(filename);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("file", filename);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });*/
        folderRV.setAdapter(adapter);








        /*directoryList = (ListView)findViewById(R.id.directoty_list);
        path = "/storage";,
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
                }
            }
        });*/
    }


    public class FileManager{
        String title;

        public boolean isFolder() {
            return isFolder;
        }

        public String getTitle() {
            return title;
        }

        boolean isFolder;

        FileManager(String directory, boolean folder){
            title = directory;
            isFolder = folder;
        }

        public String ext(){
            return title.substring(title.lastIndexOf(".")+1);
        }
    }



    public  void createData(String getPath){
        path= getPath;
        setTitle(path);
        adapter = new ChooserRVAdapter(data);
        // Read all files sorted into the values-array
        ArrayList<String> values = new ArrayList();
        File dir = new File(path);
        if (dir.canRead()) {
            String[] list = dir.list();
            if (list != null) {
                for (String file : list) {
                    if (!file.startsWith(".")) {
                        values.add(file);
                    }
                }
            }
        }
        data = values;
        adapter.changeData(values);
        adapter.notifyDataSetChanged();
        folderRV.setAdapter(new ChooserRVAdapter(values));
        folderRV.invalidate();
        //adapter = new ChooserRVAdapter(values);
    }


}
