package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.vkdocs.oceanminded.vkdocs.Adapters.ViewPagerAdapte;
import com.vkdocs.oceanminded.vkdocs.CircleTransform;
import com.vkdocs.oceanminded.vkdocs.Fragments.AllDocumentsFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.AnimationFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.ArchivsFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.ImagesFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.OtherFragment;
import com.vkdocs.oceanminded.vkdocs.Fragments.TextsFragment;
import com.vkdocs.oceanminded.vkdocs.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CHOOSE_FILE_REQUESTCODE = 1;
    private static final int SELECT_PICTURE = 123;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ImageView userAvatar;
    private NavigationView navigationView;
    private TextView userName;
    private String Fileurl;
    private String FileName;
    private String filemanagerstring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MA","start");
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        //add navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //set user avatar and user name
        getUserData();

    }


    public void repeat(){
        Fragment nowFragment = getFragmentManager().findFragmentById(R.layout.fragmenta);

    }

    public String getUserData() {

        //query for get user Avatar, user First and Last Name
        final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,photo_100"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                //This query return
                VKList<VKApiUser> usersArray = (VKList<VKApiUser>) response.parsedModel;
                for (VKApiUser user : usersArray) {
                    //set user avatar to drawer
                    userAvatar = (ImageView) findViewById(R.id.user_avatar_imageview);
                    userName = (TextView) findViewById(R.id.user_name_textview);
                    /*Glide.with(getApplicationContext()).load(user.photo_100).asBitmap().centerCrop().into(new BitmapImageViewTarget(userAvatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            userAvatar.setImageDrawable(circularBitmapDrawable);
                        }
                    });*/

                    Picasso.with(getApplicationContext()).load(user.photo_100).transform(new CircleTransform()).into(userAvatar);
                    //set user name to drawer
                    userName.setText(user.first_name + " " + user.last_name);
                }

            }


            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }
        });
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_exit_account) {
            VKSdk.logout();
            Intent openLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(openLoginActivity);
        } else if (id == R.id.nav_new_doc) {
            createFileFromSource();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public File createFileFromSource() {

        //1 Open FileManager on Activity
        //2 When user checked file, close activity of file manager and return File Path
        //3 Create File from filePath


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent, "Choose a file");

        startActivityForResult(intent,CHOOSE_FILE_REQUESTCODE);

        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
AllDocumentsFragment oneFragment = (AllDocumentsFragment) getSupportFragmentManager().getBackStackEntryAt(R.layout.fragmenta);
        oneFragment.getDocumentFromServer();
        if(requestCode == CHOOSE_FILE_REQUESTCODE) {
            Log.i("Error", data.getDataString());
            Uri selectedImageUri = data.getData();

//OI FILE Manager
            filemanagerstring = selectedImageUri.getPath();

//MEDIA GALLERY
            String Fpath = selectedImageUri.getPath();
            FileName = data.getData().getLastPathSegment();
            Fileurl = data.getData().getPath().substring(0, data.getData().getPath().lastIndexOf("/"));


            //******************************
            VKRequest request = VKApi.docs().getUploadServer();
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
//----------------------------------send file to vk.com/MyDocuments/---------------------------------
                    JSONObject jo = null;
                    try {
                        jo = response.json.getJSONObject("response");
                        jo.getString("upload_url");

                        //need add, method getPath. This method must open file Manager and return full path of checked file.
                        // Example: sdcard/vkDocs/document1.docx

                        //Get the text file
                        File pathFile = Environment.getExternalStorageDirectory();
                        String fileName = "downloaded.jpg";
                        File file = new File(Fileurl, FileName);
                        VKRequest uploadFile = VKApi.docs().uploadDocRequest(file);
                        uploadFile.executeWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                super.onComplete(response);
                            }

                            @Override
                            public void onError(VKError error) {
                                super.onError(error);
                                Log.i("Error", error.toString());
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //
                }
            });
            Log.i("File Name", FileName);
            Log.i("File Path", Fileurl);

        }
    }
}