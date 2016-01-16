package com.vkdocs.oceanminded.vkdocs;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKSdkVersion;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiDocs;
import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKDocsArray;
import com.vk.sdk.api.model.VKList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class OneFragment extends Fragment {

    private ListView doscList;
    private TextView notdosc;
    List<VKApiDocument> documentslist;
    ProgressDialog mProgressDialog;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenta,
                container, false);
        //doscList =(ListView) view.findViewById(R.id.docs_list);
        notdosc =(TextView) view.findViewById(R.id.notdocs_text);
        final RecyclerView documenstList = (RecyclerView) view.findViewById(R.id.documents_recycleview);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        documenstList.setLayoutManager(llm);
        documenstList.setHasFixedSize(true);
        /*final String[] arraydocuments;
        final ArrayList<String> documentslist = new ArrayList<>();
        VKRequest getdocs = new VKRequest("docs.get",VKParameters.from(VKApiConst.FIELDS,"1"), VKRequest.HttpMethod.GET, VKDocsArray.class);
        getdocs.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKDocsArray docsArray = (VKDocsArray) response.parsedModel;
                String[] text = new String[docsArray.size()];
                int index = 0;
                for(VKApiDocument doc : docsArray) {

                    documentslist.add(doc.url);
                }

                if(!documentslist.isEmpty()) {
                    notdosc.setVisibility(View.INVISIBLE);
                    doscList.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, documentslist);
                    doscList.setAdapter(arrayAdapter);
                }
                else{
                    doscList.setVisibility(View.INVISIBLE);
                    notdosc.setVisibility(View.VISIBLE);
                }
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
        doscList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// instantiate it within the onCreate method
                Log.i("information", "item click");
                TextView item = (TextView) view;
                Log.i("information", item.getText().toString());
                new DownloadTask().execute(item.getText().toString());

            }
        });*/




        //----------------------query for docs---------------------------------
        documentslist = new ArrayList<>();
        VKRequest getdocs = new VKRequest("docs.get", VKParameters.from(VKApiConst.FIELDS, "1"), VKRequest.HttpMethod.GET, VKDocsArray.class);
        getdocs.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKDocsArray docsArray = (VKDocsArray) response.parsedModel;
                int i = 0;
                for (VKApiDocument doc : docsArray) {

                    documentslist.add(doc);

                    //Log.i("json", response.json.toString());
                    i++;
                }
                RVAdapter adapter = new RVAdapter(documentslist);
                documenstList.setAdapter(adapter);


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
        Log.i("list.size", "" + documentslist.size());
        //----------------------recycleview------------------------------------




        return view;
    }

    private void initializeData(){

    }


}
