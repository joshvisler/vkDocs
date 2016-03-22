package com.vkdocs.oceanminded.vkdocs.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKDocsArray;
import com.vkdocs.oceanminded.vkdocs.Adapters.RVAdapter;
import com.vkdocs.oceanminded.vkdocs.R;

import java.util.ArrayList;
import java.util.List;


public class ImageFragment extends Fragment {

    private List<VKApiDocument> documentslist;
    private RecyclerView documenstListRV;
    private RVAdapter adapter;
    public  int mdocParametr;
    public static int DOCS_COUNT = 20;
    private static FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView errorText;
    private TextView tryAgain;



    public ImageFragment() {
        mdocParametr = 4;
        documentslist = new ArrayList<>();
        adapter = new RVAdapter();
        Log.d("Constructor", "Hello Constructor");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("onCreate", "Hello onCreate()");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View view = inflater.inflate(R.layout.fragmenta,container, false);
        tryAgain = (TextView) view.findViewById(R.id.try_text);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        errorText = (TextView) view.findViewById(R.id.error_fragment_text);
        progressBar = (ProgressBar)  view.findViewById(R.id.fragment_progresbar);
        progressBar.setVisibility(View.GONE);

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
        final LinearLayoutManager llm = new LinearLayoutManager(getContext());
        documenstListRV.setLayoutManager(llm);
        documenstListRV.setHasFixedSize(true);

        final View fabView = inflater.inflate(R.layout.fragmenta,container, false);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        documenstListRV.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("changed", dx + ""+dy+"");
                if(dy>0)
                    fab.hide();
                else
                    fab.show();

            }
        });
        getDocumentFromServer();
        if(getDocumentFromServer() != null) {
            errorText.setVisibility(View.GONE);
        }
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
            errorText.setText("Ничего не найденно");
            errorText.setVisibility(View.VISIBLE);
        }
        else {
            errorText.setVisibility(View.INVISIBLE);
        }

        return result;
    }

    public void updateData(){
        getDocumentFromServer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

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
    public  boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            tryAgain.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    class GedData extends AsyncTask<Void,Void,ArrayList<VKApiDocument> >{
        @Override
        protected ArrayList<VKApiDocument> doInBackground(Void... params) {
            Log.d("Load Data", "Hello Load Data");
            documentslist.clear();
            VKRequest getdocs = new VKRequest("docs.get", VKParameters.from("type", mdocParametr), VKRequest.HttpMethod.GET, VKDocsArray.class);
            getdocs.executeSyncWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                    super.onProgress(progressType, bytesLoaded, bytesTotal);
                }

                @Override
                public void onComplete(VKResponse response) {
                    VKDocsArray docsArray = (VKDocsArray) response.parsedModel;
                    for (VKApiDocument doc : docsArray) {
                        if(!documentslist.contains(doc))
                        documentslist.add(doc);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    progressBar.setVisibility(View.GONE);
                    documenstListRV.setVisibility(View.VISIBLE);
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

            Log.d("Res Size","size = "+documentslist.size());
            //adapter = new RVAdapter(documentslist);
            return (ArrayList<VKApiDocument>) documentslist;
        }

        @Override
        protected void onPostExecute(ArrayList<VKApiDocument> vkApiDocuments) {
            Log.d("onpost","Fuck post");
            for(VKApiDocument doc:vkApiDocuments){
                Log.d("DjcName",doc.title);
            }
        }
    }

    public ArrayList<VKApiDocument> getDocumentFromServer() {
        if (isOnline(getContext())) {
            GedData data = new GedData();
            ArrayList<VKApiDocument> resultList = new ArrayList<VKApiDocument>(data.doInBackground());
               Log.d("Count",adapter.getItemCount()+"");
            adapter.changeData(resultList);
            documenstListRV.setAdapter(adapter);
            if(adapter.getItemCount()<=0)
            {
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("Документы отсутствуют");
            }
            else {
                errorText.setVisibility(View.GONE);
                tryAgain.setVisibility(View.GONE);
            }


            return resultList;
        }
        else if(adapter.getItemCount()<=0){
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Отсутствует соединение");
            tryAgain.setVisibility(View.VISIBLE);
        }
        else if(adapter.getItemCount()>0)
        {
            Toast.makeText(getContext(),"Отсутствует соединение ",Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
        return null;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
