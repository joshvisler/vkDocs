package com.vkdocs.oceanminded.vkdocs.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.vkdocs.oceanminded.vkdocs.Services.VKDataService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 23.01.16.
 */
public class ArchivsFragment extends Fragment {

    private TextView notdosc;
    private List<VKApiDocument> documentslist;
    private RecyclerView documenstListRV;
    private RVAdapter adapter;
    public static int DOCS_PARAMETR = 2;
    public static final String VKDOCS_LOADING = "Test";
    public static final String ALARM_MESSAGE = "Срочно пришлите кота!";
    private SwipeRefreshLayout swipeRefreshLayout;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenta,container, false);
        notdosc =(TextView) view.findViewById(R.id.notdocs_text);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.vk_color);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });
        documenstListRV = (RecyclerView) view.findViewById(R.id.documents_recycleview);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        documenstListRV.setLayoutManager(llm);
        documenstListRV.setHasFixedSize(true);
        documentslist = new ArrayList<>();
        documentslist = getDocumentFromServer();
        adapter = new RVAdapter(documentslist);
        documenstListRV.setAdapter(adapter);

        return view;
    }

    public ArrayList<VKApiDocument>  search(String searchQueary){
        ArrayList<VKApiDocument> result = new ArrayList<>();

        for(VKApiDocument queryDoc : documentslist){
            if(queryDoc.title.contains(searchQueary)){
                result.add(queryDoc);
            }
        }
        if (result.isEmpty()){
            notdosc.setVisibility(View.VISIBLE);
        }
        else {
            notdosc.setVisibility(View.INVISIBLE);
        }

        return result;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //inflater.inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Search", query);
                adapter.changeData(search(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.changeData(search(newText));
                Log.d("Search", newText);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("Search", "closed");
                adapter.changeData(documentslist);
                return false;
            }
        });
    }

    public void updateData(){

        if(getDocumentFromServer().size() != documentslist.size() ) {
            documentslist = getDocumentFromServer();
            adapter.changeData(documentslist);
        }

    }

    public ArrayList<VKApiDocument> getDocumentFromServer() {
        final ArrayList<VKApiDocument> resultList  = new ArrayList<VKApiDocument>();
        //VKRequest getdocs = new VKRequest("docs.get", VKParameters.from("type", DOCS_PARAMETR,"count",20), VKRequest.HttpMethod.GET, VKDocsArray.class);
        VKRequest getdocs = new VKRequest("docs.get", VKParameters.from("type", DOCS_PARAMETR), VKRequest.HttpMethod.GET, VKDocsArray.class);
        getdocs.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }

            @Override
            public void onComplete(VKResponse response) {
                //resultList = new ArrayList<VKApiDocument>();
                VKDocsArray docsArray = (VKDocsArray) response.parsedModel;
                for (VKApiDocument doc : docsArray) {
                    resultList.add(doc);
                    swipeRefreshLayout.setRefreshing(false);
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
        if (resultList.isEmpty()){
            notdosc.setVisibility(View.VISIBLE);
        }
        else {
            notdosc.setVisibility(View.INVISIBLE);
        }
        return resultList;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
