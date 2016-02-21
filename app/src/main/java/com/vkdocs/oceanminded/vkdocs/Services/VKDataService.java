package com.vkdocs.oceanminded.vkdocs.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKDocsArray;

import org.json.JSONException;

import java.util.ArrayList;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class VKDataService extends IntentService {
    private ArrayList<VKApiDocument> data;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    final String SERVICE_LOG = "Service";
    public VKDataService() {

        super("name");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("S", "Started");
    }

     public void pushData(ArrayList<VKApiDocument> datalist){
        data = datalist;
    }

    public void getDocumentFromServer() {
        final ArrayList<VKApiDocument> docsList = new ArrayList<VKApiDocument>();
        VKRequest getdocs = new VKRequest("docs.get", VKParameters.from(VKApiConst.FIELDS, "1"), VKRequest.HttpMethod.GET, VKDocsArray.class);
        getdocs.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }

            @Override
            public void onComplete(VKResponse response) throws JSONException {
                super.onComplete(response);
                VKDocsArray docsArray = (VKDocsArray) response.parsedModel;
                for (VKApiDocument doc : docsArray) {
                    docsList.add(doc);
                }

                Log.i("list.size", "" + docsList.size());
                //pushData(docsList);


            }


            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });


    }




    @Override
    protected void onHandleIntent(Intent intent) {
        //getDocumentFromServer();
        Log.i("message", intent.getStringExtra("send"));
        intent.putExtra("back","work!!!");



        Intent vkServiceIntent = new Intent();
        vkServiceIntent.putExtra("get", "get Data");
        vkServiceIntent.setAction("StartService");
        vkServiceIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(vkServiceIntent);
    }

}
