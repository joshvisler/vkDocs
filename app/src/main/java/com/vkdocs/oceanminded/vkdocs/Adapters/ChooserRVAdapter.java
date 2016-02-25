package com.vkdocs.oceanminded.vkdocs.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkdocs.oceanminded.vkdocs.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by josh on 22.02.16.
 */
public class ChooserRVAdapter extends RecyclerView.Adapter<ChooserRVAdapter.ChooserHolder>{
    ArrayList<String> mdata;
    private Context context;

    public String getMpath() {
        return mpath;
    }

    String mpath;
    OnItemClickListener mItemClickListener;
    public ChooserRVAdapter(ArrayList<String> data) {
        mdata = data;
    }


    public void changeData(ArrayList<String> data){
        mdata.clear();
        mdata.addAll(data);
        notifyDataSetChanged();

    }
    class ChooserHolder extends RecyclerView.ViewHolder implements RecyclerItemClickListener.OnItemClickListener {

        TextView fileType;
        TextView folderTitle;
        TextView fileExt;
        ImageView icon;


        public ChooserHolder(View itemView) {
            super(itemView);
            fileType = (TextView) itemView.findViewById(R.id.doc_text_image);
            folderTitle = (TextView) itemView.findViewById(R.id.choser_item_title);
            fileExt = (TextView) itemView.findViewById(R.id.chooser_file_ext);
            icon = (ImageView) itemView.findViewById(R.id.chooser_folder_icon);
        }


        @Override
        public void onItemClick(View view, int position) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, position);

            }

        }
    }


    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public ChooserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chooser_item, parent, false);
        ChooserHolder pvh = new ChooserHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ChooserHolder holder, int position) {
        holder.folderTitle.setText(mdata.get(position));
        //if file
        if(holder.folderTitle.getText().toString().contains(".")){
            //Budlo code i know :)
            String ext = holder.folderTitle.getText().toString().substring(holder.folderTitle.getText().toString().lastIndexOf(".")+1);
            holder.fileExt.setText(ext);
            holder.icon.setImageDrawable(new ColorDrawable(Color.parseColor("#e9ecf1")));
        }
        else {
            holder.fileExt.setText("");
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_folder));
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

}
