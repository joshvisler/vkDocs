package com.vkdocs.oceanminded.vkdocs;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by josh on 16.01.16.
 */
class DownloadTask extends AsyncTask<String, Integer, String> {

    /**
     * Before starting background thread Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        try {

            URL url = new URL(f_url[0]);
            File folder = new File(Environment.getExternalStorageDirectory() +"/VkDocs");
            folder.mkdirs();
            if (!folder.exists()) {

            }
            String folderpath = folder.toString();


            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();
            FileOutputStream fos = new FileOutputStream(new File(folderpath,"file.jpg"));
            byte[] buf = new byte[512];
            while (true) {
                int len = in.read(buf);
                if (len == -1) {
                    break;
                }
                fos.write(buf, 0, len);
            }
            in.close();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.i("information", e.toString());
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        Log.i("download",progress.toString());
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded


    }

}
