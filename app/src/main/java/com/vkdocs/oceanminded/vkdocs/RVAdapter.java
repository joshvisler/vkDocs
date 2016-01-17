/*

*/

package com.vkdocs.oceanminded.vkdocs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiDocument;

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
    Context context;


    public static class DocumentsHolder extends RecyclerView.ViewHolder{
        TextView documentTitle;
        TextView documentInfo;
        ImageView documentIcon;

    public DocumentsHolder(View itemView) {
        super(itemView);
        documentTitle = (TextView) itemView.findViewById(R.id.doc_name);
        documentInfo = (TextView) itemView.findViewById(R.id.doc_info);
        documentIcon = (ImageView) itemView.findViewById(R.id.doc_icon);

    }
}

    public RVAdapter(List<VKApiDocument> list) {
        this.documentslist = new ArrayList<>(list);
        //for (VKApiDocument doc : documentslist) {
            //Log.i("list.size", ""+documentslist.size());
        //}
    }
    @Override
    public DocumentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        DocumentsHolder pvh = new DocumentsHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(DocumentsHolder holder, int position) {


        holder.documentTitle.setText(documentslist.get(position).title);
        holder.documentInfo.setText(convertSize(documentslist.get(position).size) + ", "+convertDate(documentslist.get(position).date));
        if(documentslist.get(position).isImage() || documentslist.get(position).isGif())
        Picasso.with(context).load(documentslist.get(position).photo_100).into(holder.documentIcon);
        else holder.documentIcon.setImageResource(R.drawable.ic_menu_camera);
    }

    public String convertDate(long unixdate)
    {
        Date date = new Date(unixdate * 1000);
        Locale russian = new Locale("ru");
        String[] newMonths = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy в H:m", russian);
        String formatedDate = sdf.format(date);
        return formatedDate;
    }

    public String convertSize(long unixsize)
    {
        String result = ""+unixsize;
        //result = "result.length() = "+result.length();

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
