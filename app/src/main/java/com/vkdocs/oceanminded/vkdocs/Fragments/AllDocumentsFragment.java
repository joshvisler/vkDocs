package com.vkdocs.oceanminded.vkdocs.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKDocsArray;
import com.vkdocs.oceanminded.vkdocs.Adapters.RVAdapter;
import com.vkdocs.oceanminded.vkdocs.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;



public class AllDocumentsFragment extends Fragment {

    private TextView notdosc;
    private List<VKApiDocument> documentslist;
    private RecyclerView documenstListRV;
    private RVAdapter adapter;
    public static int DOCS_PARAMETR = 0;
    public static int DOCS_COUNT = 20;


    public AllDocumentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenta,container, false);
        notdosc =(TextView) view.findViewById(R.id.notdocs_text);
        documenstListRV = (RecyclerView) view.findViewById(R.id.documents_recycleview);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        documenstListRV.setLayoutManager(llm);
        documenstListRV.setHasFixedSize(true);
        documentslist = new ArrayList<>();
        getDocumentFromServer();
        return view;
    }

    public void getDocumentFromServer() {
        //---- Async Request--------------
        List<VKApiDocument> docsList = new ArrayList<VKApiDocument>();
        VKRequest getdocs = new VKRequest("docs.get", VKParameters.from("type", DOCS_PARAMETR,"count",20), VKRequest.HttpMethod.GET, VKDocsArray.class);
        getdocs.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }

            @Override
            public void onComplete(VKResponse response) throws JSONException {
                super.onComplete(response);
                VKDocsArray docsArray = (VKDocsArray) response.parsedModel;
                for (VKApiDocument doc : docsArray) {
                    documentslist.add(doc);
                }
                adapter = new RVAdapter(documentslist);
                documenstListRV.setAdapter(adapter);
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
