package com.vkdocs.oceanminded.vkdocs.Activitys;

import android.widget.ProgressBar;

import com.squareup.picasso.Callback;

/**
 * Created by josh on 28.01.16.
 */
public class ImageLoadedCallback implements Callback {
    ProgressBar progressBar;

    public  ImageLoadedCallback(ProgressBar progBar){
        progressBar = progBar;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }
}
