package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vkdocs.oceanminded.vkdocs.R;

public class LoginActivity extends AppCompatActivity {
    private Button noConnectionButton;
    private TextView noConnectionText;
    private ProgressBar mloading;

    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        noConnectionButton = (Button) findViewById(R.id.noConnection_button);
        noConnectionText = (TextView) findViewById(R.id.connection_error_text);
        mloading = (ProgressBar)findViewById(R.id.progressBarLoading);
        mloading.setSystemUiVisibility(View.VISIBLE);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        connection();
        noConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noConnectionButton.setVisibility(View.INVISIBLE);
                noConnectionText.setVisibility(View.INVISIBLE);
                mloading.setVisibility(View.VISIBLE);
                connection();
            }
        });


    }

    private void connection() {
        if (isOnline()) {
            VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
                @Override
                public void onResult(VKSdk.LoginState res) {
                    switch (res) {
                        case LoggedOut:
                            VKSdk.login(LoginActivity.this, sMyScope);
                            break;
                        case LoggedIn:
                            userLoggedIn();
                            break;
                        case Pending:
                            break;
                        case Unknown:
                            break;
                    }
                }

                @Override
                public void onError(VKError error) {
                    Log.e("error", "Error Login " + error.toString());
                    noConnectionButton.setVisibility(View.VISIBLE);
                    noConnectionText.setVisibility(View.VISIBLE);
                    mloading.setVisibility(View.GONE);
                }
            });
        }
    }

    private void userLoggedIn()
    {
        Intent openMainActivity = new Intent(this,MainActivity.class);
        //mloading.setVisibility(View.GONE);
        startActivity(openMainActivity);
        finish();
    }
    public void onClick(View view)
    {
        VKSdk.login(LoginActivity.this, sMyScope);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                //VKSdk.login(LoginActivity.this, sMyScope);
                userLoggedIn();
            }

            @Override
            public void onError(VKError error) {
                finish();
                // User didn't pass Authorization
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public  boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        noConnectionButton.setVisibility(View.VISIBLE);
        noConnectionText.setVisibility(View.VISIBLE);
        mloading.setVisibility(View.GONE);
        return false;
    }
}