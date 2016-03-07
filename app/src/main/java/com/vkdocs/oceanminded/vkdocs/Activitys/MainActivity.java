package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vkdocs.oceanminded.vkdocs.Adapters.ViewPagerAdapte;
import com.vkdocs.oceanminded.vkdocs.Fragments.AllDocumentsFragment;
import com.vkdocs.oceanminded.vkdocs.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 5;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    private static FloatingActionButton fab;
    private static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MA", "start");
        guiInitialized();//create gui
        //viewPager.setVisibility(View.INVISIBLE);
        //tabLayout.setVisibility(View.INVISIBLE);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapte adapter = new ViewPagerAdapte(getSupportFragmentManager());
        adapter.addFragment(new AllDocumentsFragment(0), "ВСЕ");
        adapter.addFragment(new AllDocumentsFragment(1), "ТЕКСТОВЫЕ");
        adapter.addFragment(new AllDocumentsFragment(2), "АРХИВЫ");
        adapter.addFragment(new AllDocumentsFragment(3), "АНИМАЦИЯ");
        adapter.addFragment(new AllDocumentsFragment(4), "ИЗОБРАЖЕНИЯ");
        adapter.addFragment(new AllDocumentsFragment(8), "ПРОЧИЕ");
        adapter.addFragment(new AllDocumentsFragment(8), "ЗАГРУЖЕННЫЕ");
        viewPager.setAdapter(adapter);
    }
    static public void hideViews() {
        fab.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        //FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fab.getLayoutParams();
        int fabBottomMargin = 50;
        fab.animate().translationY(fab.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    static public void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        final MenuItem item = menu.findItem(R.id.action_search);

       /* searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Search",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("Search",newText);
                return false;
            }
        });

       */
        return true;
    }

    public void guiInitialized() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
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

        //add notification
        final NotificationManager nm = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setSmallIcon(android.R.drawable.stat_sys_upload)
                .setWhen(System.currentTimeMillis());
        Notification uploadNotification = builder.getNotification();




        if (requestCode == PICKFILE_RESULT_CODE && data != null) {
            nm.notify(666,uploadNotification);
            final String filePath = data.getStringExtra("file");// get path from result
            String path = filePath.substring(0, filePath.lastIndexOf("/"));// get path without name
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);//get file name ( for example myText.txt)
            File uploadFile = new File(path, fileName);

            VKRequest uploadRequest;
            uploadRequest = VKApi.docs().uploadDocRequest(uploadFile); //request for upload file to vk server



            uploadRequest.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {

                    nm.cancel(666);
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                    Log.d("Upload Error", error.toString());
                    nm.cancel(666);
                    Toast.makeText(getApplicationContext(),"Не удалось загрузить файл",Toast.LENGTH_LONG);
                }

                @Override
                public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                    super.onProgress(progressType, bytesLoaded, bytesTotal);
                    Log.d("progress", "" + bytesLoaded);
                }
            });



        }

    }

}
