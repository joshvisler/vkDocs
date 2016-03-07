package com.vkdocs.oceanminded.vkdocs.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiDocs;
import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKDocsArray;
import com.vkdocs.oceanminded.vkdocs.FileOpen;
import com.vkdocs.oceanminded.vkdocs.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by josh on 16.01.16.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.DocumentsHolder> {

    List<VKApiDocument> documentslist;
    public  Context context;
    Locale russian = new Locale("ru");
    String[] newMonths = {
            "января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    private Bitmap bitmap;
    String nameDocument;
    private File file;
    private File folder;


    public  class DocumentsHolder extends RecyclerView.ViewHolder{
        TextView documentTitle;
        TextView documentInfo;
        ImageView documentIcon;
        TextView documentIconText;
        ImageButton documentMenu;

        public DocumentsHolder(View itemView) {
            super(itemView);
            documentTitle = (TextView) itemView.findViewById(R.id.doc_name);
            documentInfo = (TextView) itemView.findViewById(R.id.doc_info);
            documentIcon = (ImageView) itemView.findViewById(R.id.chooser_folder_icon);
            documentIconText = (TextView) itemView.findViewById(R.id.doc_text_image);
            documentMenu = (ImageButton) itemView.findViewById(R.id.doc_menu_button);

        }



    }

    public RVAdapter(List<VKApiDocument> list) {
        this.documentslist = new ArrayList<>(list);
        nameDocument = "";
        folder = new File(Environment.getExternalStorageDirectory() +"/VkDocs");
        if (!folder.exists()) {
            folder.mkdirs();// создаем директорию
        }
    }

    public void changeData(List<VKApiDocument> data){
        documentslist.clear();
        documentslist.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public DocumentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_item, parent, false);
        DocumentsHolder pvh = new DocumentsHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(final DocumentsHolder holder, final int position) {
        holder.documentTitle.setText(title(documentslist.get(position).title));
        holder.documentInfo.setText(convertSize(documentslist.get(position).size) + ", " + convertDate(documentslist.get(position).date));


        if(documentslist.get(position).isImage() || documentslist.get(position).isGif()) {
            holder.documentIconText.setText("");
            Picasso.with(context).load(documentslist.get(position).photo_100).into(holder.documentIcon);
        }
        else{
            holder.documentIconText.setText(documentslist.get(position).ext);
            holder.documentIcon.setImageDrawable(new ColorDrawable(Color.parseColor("#e9ecf1")));
        }
        holder.documentMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docMenuShow(v,position);
                Log.d("Menu","Clicked");
            }
        });






        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameDocument = documentslist.get(position).title;
                file = new File(folder.getAbsolutePath(), nameDocument);
                if (file.exists()) {
                    try {
                        FileOpen.openFile(context, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    DownloadDocument downloadDocument = new DownloadDocument();
                    downloadDocument.execute(documentslist.get(position).url);
                }
            }
        });



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nameDocument = documentslist.get(position).title;
                file = new File(folder.getAbsolutePath(), nameDocument);

                AlertDialog.Builder builderDialog = new AlertDialog.Builder(context);
                builderDialog.setMessage("Заменить файл").setTitle("Файл уже существует");
                builderDialog.setNegativeButton("нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            FileOpen.openFile(context, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                builderDialog.setPositiveButton("да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownloadDocument downloadDocument = new DownloadDocument();
                        downloadDocument.execute(documentslist.get(position).url);
                    }
                });

                AlertDialog dialog = builderDialog.create();
                if (file.exists()) {
                    dialog.show();
                }
                return false;
            }
        });
    }


    public void save(final int position){
        nameDocument = documentslist.get(position).title;
        file = new File(folder.getAbsolutePath(), nameDocument);
        if (file.exists()) {
            nameDocument = documentslist.get(position).title+1;
            DownloadDocument downloadDocument = new DownloadDocument();
            downloadDocument.execute(documentslist.get(position).url);
        } else {
            DownloadDocument downloadDocument = new DownloadDocument();
            downloadDocument.execute(documentslist.get(position).url);
        }
    }




    public void delete(final int position){
        int docId = documentslist.get(position).id;
        int ownrtId = documentslist.get(position).owner_id;
        VKRequest deleteRequest = new VKRequest("docs.delete", VKParameters.from("owner_id",ownrtId,"doc_id",docId));
        deleteRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Toast.makeText(context, "Документ удален", Toast.LENGTH_LONG);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("del Doc Erroe", error.toString());
                Toast.makeText(context, "Не удалось удалить документ", Toast.LENGTH_LONG);
            }
        });
    }

    public void change(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View v = LayoutInflater.from(context).inflate(R.layout.change_dialog, null);
        builder.setView(LayoutInflater.from(context).inflate(R.layout.change_dialog,null));
        final EditText titleEditText = (EditText) v.findViewById(R.id.change_doc_title);
        final EditText pointEditText = (EditText) v.findViewById(R.id.change_doc_point);
        builder.setPositiveButton("сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int docId = documentslist.get(position).id;
                int ownrtId = documentslist.get(position).owner_id;
                String ext = documentslist.get(position).ext;
                String title = titleEditText.getText().toString()+"sad."+ext;
                Log.d("Change",titleEditText.getText()+"");
                Log.d("Change",pointEditText.getText()+"");


                VKRequest changeRequest;
                if(pointEditText.getText().toString() == null) {
                    String tags = pointEditText.getText().toString();
                    changeRequest = new VKRequest("docs.edit", VKParameters.from("owner_id", ownrtId, "doc_id", docId, "title", title));
                }
                else {
                    String tags = pointEditText.getText().toString();
                    changeRequest = new VKRequest("docs.edit", VKParameters.from("owner_id", ownrtId, "doc_id", docId, "title", title, "tags", tags));
                }
                changeRequest.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        Toast.makeText(context,"Документ изменен",Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        Log.d("del Doc Erroe",error.toString());
                        Toast.makeText(context, "Не удалось изменить документ", Toast.LENGTH_LONG);
                    }
                });

                /*if( titleEditText.getText().toString() == null ){
                    dialog.dismiss();
                }*/
            }
        });
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }



    public void docMenuShow(View v, final int position){
        PopupMenu docMenu = new PopupMenu(context,v);
        docMenu.inflate(R.menu.doc_menu);

        docMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_save:
                        save(position);
                        ;
                        return true;
                    case R.id.menu_change:
                        change(position);
                        return true;
                    case R.id.menu_delete:
                        delete(position);
                        ;
                        return true;
                    default:
                        return false;
                }
            }
        });

        docMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        docMenu.show();
    }








    //Класс для загрузки документа
    class DownloadDocument extends AsyncTask<String, String, File> {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        private Exception error = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Открытие файла");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel(true);
                    progressDialog.cancel();
                }
            });

            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                    progressDialog.cancel();
                }
            });
            progressDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected File doInBackground(String... f_url) {
            int count;
            File file = null;
            try {
                file = new File(folder.getAbsolutePath(),nameDocument);
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lenghtFile = connection.getContentLength();
                InputStream in = new BufferedInputStream(url.openStream());
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buf = new byte[512];
                long total =0;
                while ((count = in.read(buf)) != -1) {
                    if(isCancelled()){
                        fos.flush();
                        fos.close();
                        in.close();
                        file.delete();
                    }
                    total += count;
                    publishProgress(""+(int)(total*100)/lenghtFile);
                    fos.write(buf, 0, count);
                }
                fos.flush();
                fos.close();
                in.close();

            } catch (Exception e) {
                error = e;
                Log.i("information", e.toString());
            }
            return file;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
            Log.i("download", progress[0].toString());
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            progressDialog.hide();
            if (error !=null){
                Toast.makeText(context, "Файл не удалось загрузить",Toast.LENGTH_LONG);
            }
            else {
                try {
                    FileOpen.openFile(context,file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  String title(String s){
        if(s.length() >=30){
            s = s.substring(0,30) + "...";
        }
        return s;
    }


    public String convertDate(long unixdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy в H:m", russian);
        Date date = new Date(unixdate * 1000);
        String formatedDate = sdf.format(date);
        return formatedDate;
    }




    public String convertSize(long unixsize)
    {
        String result = ""+unixsize;

        switch (result.length()){
            case 1: result += " Б";;
                break;
            case 2: result += " Б";
                break;
            case 3: result += " Б";
                break;
            case 4: result = result.subSequence(0,1) + " КБ";
                break;
            case 5:  result = result.subSequence(0,2) + " КБ";
                break;
            case 6:  result = result.subSequence(0,3) + " КБ";
                break;
            case 7:  result = result.subSequence(0,1) + " МБ";
                break;
            case 8:  result = result.subSequence(0,2) + " МБ";
                break;
            case 9:  result = result.subSequence(0,3) + " МБ";
                break;
        }
        return result;
    }



    @Override
    public int getItemCount() {
        return documentslist.size();
    }
}