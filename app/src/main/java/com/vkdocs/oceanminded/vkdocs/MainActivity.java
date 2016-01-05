package com.vkdocs.oceanminded.vkdocs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKUsersArray;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static String sTokenKey = "VK_ACCESS_TOKEN";
    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS
    };

    private Button logout;
    private Button allDocuments;
    private EditText textView;
    private TextView user_textview;
    private VKRequest request;
    private boolean isResumed = true;//Проверка - пользователь не авторизован
    private VKRequestHelper vkRequestHelper;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        logout = (Button)findViewById(R.id.logout_button);
        allDocuments = (Button)findViewById(R.id.alldocuments_button);
        textView = (EditText)findViewById(R.id.textView);
        user_textview = (TextView) findViewById(R.id.user_textview);
        photo = (ImageView) findViewById(R.id.photo);
        textView.setText("sda");
        final MainActivity activity = this;
        //add vk api
        VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {
                    switch (res) {
                        case LoggedOut:
                            logout.setVisibility(View.INVISIBLE);
                            VKSdk.login(activity,sMyScope);
                            break;
                        case LoggedIn:
                            logout.setVisibility(View.VISIBLE);
                            break;
                        case Pending:
                            break;
                        case Unknown:
                            break;
                    }
            }
            @Override
            public void onError(VKError error) {

            }
        });


        View.OnClickListener logoutListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.logout();
            }
        };

        View.OnClickListener allDocumentsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FIELDS = "photo, photo_50, photo_100, photo_200, city, sex";
                request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, FIELDS));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList<VKApiUserFull> list = (VKList<VKApiUserFull>) response.parsedModel;
                        VKApiUserFull user = list.get(0);
                        Log.d("hello", response.json.toString());
                        setResponseText(" "+user.first_name + " " + user.last_name);
                        Picasso.with(getApplicationContext()).load(user.photo_100).into(photo);
                    }
                    @Override
                    public void onError(VKError error){
                            super.onError(error);
                        Log.d("hello", error.toString());
                    }
                });

                VKParameters params = VKParameters.from("domain", "mr_unpredictability", "message", textView.getText().toString());
                VKRequest request = new VKRequest("messages.send", params);
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Log.d("hello", response.json.toString());
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        Log.d("hello", error.toString());
                    }
                });
            }
        };

        logout.setOnClickListener(logoutListener);
        allDocuments.setOnClickListener(allDocumentsListener);




    }

    protected void setResponseText(String text) {
        user_textview.setText(user_textview.getText()+ text);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
