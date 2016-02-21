package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

        Intent intent = new Intent(this, ChoseFile.class);
        startActivityForResult(intent, PICKFILE_RESULT_CODE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

            File pathFile = Environment.getExternalStorageDirectory();
            String fileName = "traveler.jpg";
            File file = new File(pathFile, "traveler.jpg");
            Log.d("File","Name:"+file.getName()+" Path:"+file.getAbsolutePath());
            VKRequest uploadFile;
                uploadFile = VKApi.docs().uploadDocRequest(file);

            uploadFile.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) throws JSONException {
                    super.onComplete(response);
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
           /* final String filePath = data.getStringExtra("file");
            fileName = FilePath.substring(FilePath.lastIndexOf("/") + 1);
            File upfile = new File(filePath, );

            FileName = FilePath.substring(FilePath.lastIndexOf("/") + 1);
            final String folder = FilePath.substring(0, FilePath.lastIndexOf("/"));
            Log.d("Full Path", FilePath);
            Log.d("Folder", folder);
            Log.d("File Name", FileName);*/

        }
    }
    private File getFile() throws IOException {
        try {
            InputStream inputStream = this.getAssets().open("android.jpg");
            File file = new File(this.getCacheDir(), "android.jpg");
            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
            output.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
