package com.vkdocs.oceanminded.vkdocs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
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

public class OneFragment extends Fragment {

    private ListView doscList;
    private TextView notdosc;
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
        doscList =(ListView) view.findViewById(R.id.docs_list);
        notdosc =(TextView) view.findViewById(R.id.notdocs_text);
        final String[] arraydocuments;
        VKRequest getdocs = new VKRequest("docs.get",VKParameters.from(VKApiConst.FIELDS, "count,offset"), VKRequest.HttpMethod.GET, VKDocsArray.class);
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
                for(VKApiDocument doc : docsArray)
                {

                    text[index] = doc.title + "\ndownload from: " +doc.url;
                    index++;
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, text);
                doscList.setAdapter(arrayAdapter);
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

        /*--------------------------------------------------------------------------------**/

        /*if(arraydocuments.length >0) {
            notdosc.setVisibility(View.INVISIBLE);
            doscList.setVisibility(View.VISIBLE);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, text[0]);
            doscList.setAdapter(arrayAdapter);
        }
        else
        {
            doscList.setVisibility(View.INVISIBLE);
            notdosc.setVisibility(View.VISIBLE);
        }*/
        return view;
    }

}
