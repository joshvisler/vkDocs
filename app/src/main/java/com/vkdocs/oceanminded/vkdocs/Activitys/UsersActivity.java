package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;
import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKDocsArray;
import com.vk.sdk.api.model.VKUsersArray;
import com.vkdocs.oceanminded.vkdocs.Adapters.RecyclerItemClickListener;
import com.vkdocs.oceanminded.vkdocs.Adapters.UsersRAdapter;
import com.vkdocs.oceanminded.vkdocs.R;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.StreamHandler;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<VKApiUser> users;
    private UsersRAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        recyclerView = (RecyclerView) findViewById(R.id.users_recycle);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        users = getData();
        adapter = new UsersRAdapter(users);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int userId = users.get(position).id;
                int ownerId = ownerId = getIntent().getIntExtra("owner_id",166742657);
                int docId = getIntent().getIntExtra("docId",437314313);
                String doc = "doc"+ownerId+"_"+docId;
                VKParameters params = VKParameters.from("user_id", userId, "attachment", doc);
                VKRequest sendRequest = new VKRequest("messages.send", params);
                sendRequest.executeSyncWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Log.d("Send OK",response.toString());
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        Log.d("Send ERROR",error.toString());
                    }
                });
                finish();
            }
        }));
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<VKApiUser> getData(){

        final ArrayList<VKApiUser> result = new ArrayList<>();
        //VKRequest getData = new VKRequest("friends.get", VKParameters.from(VKApiConst.FIELDS,"fields,domain,photo_50"), VKRequest.HttpMethod.GET, VKApiUser.class);
        VKRequest getData = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,"fields,domain,photo_50"));
        getData.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKUsersArray usersArray = (VKUsersArray) response.parsedModel;
                for (VKApiUser user : usersArray) {
                    result.add(user);
                    Log.d("Friend",user.first_name);
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("Friends Error",error.toString());
            }
        });
        Log.d("Friends",result.size()+"");
        return result;
    }
}
