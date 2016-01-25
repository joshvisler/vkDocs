/*

*/

package com.vkdocs.oceanminded.vkdocs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vk.sdk.api.model.VKApiDocument;
import com.vkdocs.oceanminded.vkdocs.Activitys.ImageActivity;
import com.vkdocs.oceanminded.vkdocs.R;

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
    public Context context;
    Locale russian = new Locale("ru");
    String[] newMonths = {
            "января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    private Bitmap bitmap;

    public static class DocumentsHolder extends RecyclerView.ViewHolder{
        TextView documentTitle;
        TextView documentInfo;
        ImageView documentIcon;
        TextView documentIconText;
    public DocumentsHolder(View itemView) {
        super(itemView);
        documentTitle = (TextView) itemView.findViewById(R.id.doc_name);
        documentInfo = (TextView) itemView.findViewById(R.id.doc_info);
        documentIcon = (ImageView) itemView.findViewById(R.id.doc_image);
        documentIconText = (TextView) itemView.findViewById(R.id.doc_text_image);


    }
}

    public RVAdapter(List<VKApiDocument> list) {
        this.documentslist = new ArrayList<>(list);
    }

    @Override
    public DocumentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        DocumentsHolder pvh = new DocumentsHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(final DocumentsHolder holder, int position) {


        holder.documentTitle.setText(title(documentslist.get(position).title));
        holder.documentInfo.setText(convertSize(documentslist.get(position).size) + ", " + convertDate(documentslist.get(position).date));


        if(documentslist.get(position).isImage() || documentslist.get(position).isGif()) {
            holder.documentIconText.setText("");
            Glide.with(context)
                    .load(documentslist.get(position).photo_100).centerCrop()
                    .into(holder.documentIcon);
        }
        else{
            holder.documentIconText.setText(documentslist.get(position).ext);
            holder.documentIcon.setImageDrawable(new ColorDrawable(Color.parseColor("#e9ecf1")));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.documentIconText.getText() == "") {
                    Intent open = new Intent(context, ImageActivity.class);
                    context.startActivity(open);
                }
            }
        });
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
