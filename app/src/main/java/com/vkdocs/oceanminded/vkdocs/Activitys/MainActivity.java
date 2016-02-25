package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkdocs.oceanminded.vkdocs.Adapters.ViewPagerAdapte;
import com.vkdocs.oceanminded.vkdocs.Fragments.AllDocumentsFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.AnimationFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.ArchivsFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.ImagesFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.OtherFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.TextsFragment;
import com.vkdocs.oceanminded.vkdocs.R;

import org.json.JSONException;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 5;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MA", "start");
        guiInitialized();//create gui

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapte adapter = new ViewPagerAdapte(getSupportFragmentManager());
        adapter.addFragment(new AllDocumentsFragment(), "ВСЕ");
        adapter.addFragment(new TextsFragment(), "ТЕКСТОВЫЕ");
        adapter.addFragment(new ArchivsFragment(), "АРХИВЫ");
        adapter.addFragment(new AnimationFragment(), "АНИМАЦИЯ");
        adapter.addFragment(new ImagesFragment(), "ИЗОБРАЖЕНИЯ");
        adapter.addFragment(new OtherFragment(), "ПРОЧИЕ");
        adapter.addFragment(new OtherFragment(), "ЗАГРУЖЕННЫЕ");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void guiInitialized() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(

                new View.OnClickListener() {
                   @Override
                    public void onClick(View view) {
                        shareFile();
                    }
                });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
    }

    public void shareFile() {
        Intent intent = new Intent(this, ChoseFile.class);//ChoseFile Activity used for chose file from device memory
        startActivityForResult(intent, PICKFILE_RESULT_CODE);//return Full file path (for example sdcard0/VkDocs/file.jpg)
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_RESULT_CODE && data != null) {
            final String filePath = data.getStringExtra("file");// get path from result
            String path = filePath.substring(0, filePath.lastIndexOf("/"));// get path without name
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);//get file name ( for example myText.txt)
            File uploadFile = new File(path, fileName);

            VKRequest uploadRequest;
            uploadRequest = VKApi.docs().uploadDocRequest(uploadFile); //request for upload file to vk server
            uploadRequest.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response){

                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                }

                @Override
                public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                    super.onProgress(progressType, bytesLoaded, bytesTotal);
                }
            });


        }
    }

}
