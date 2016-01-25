package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vkdocs.oceanminded.vkdocs.R;

public class LoginActivity extends AppCompatActivity {
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

            }
        });
        setContentView(R.layout.activity_login);

    }


    private void userLoggedIn()
    {
        Intent openMainActivity = new Intent(this,MainActivity.class);
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
                // User didn't pass Authorization
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
