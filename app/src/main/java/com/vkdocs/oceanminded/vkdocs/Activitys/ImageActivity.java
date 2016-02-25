package com.vkdocs.oceanminded.vkdocs.Activitys;

/*import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.vkdocs.oceanminded.vkdocs.R;

public class ImageActivity extends AppCompatActivity {

    private ImageView image;
    private boolean mshow = false;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        progressBar = (ProgressBar) findViewById(R.id.image_progresbar);
        progressBar.setSystemUiVisibility(View.VISIBLE);
        image = (ImageView) findViewById(R.id.image_content);

        Picasso.with(this).load(getIntent().getStringExtra("url")).into(image,  new ImageLoadedCallback(progressBar) {
            @Override
            public void onSuccess() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        hide();
    }


    public void hide(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }


    public void show(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

}
*/