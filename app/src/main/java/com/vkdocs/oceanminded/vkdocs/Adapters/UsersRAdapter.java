package com.vkdocs.oceanminded.vkdocs.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.model.VKApiUser;
import com.vkdocs.oceanminded.vkdocs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pride on 12.03.2016.
 */
public class UsersRAdapter extends RecyclerView.Adapter<UsersRAdapter.UsersHolder> {

    private ArrayList<VKApiUser> usersList;
    private Context context;
    OnItemClickListener mItemClickListener;
    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public UsersRAdapter(ArrayList<VKApiUser> usersList) {
        this.usersList = usersList;

    }

    @Override
    public UsersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        UsersHolder pvh = new UsersHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UsersHolder holder, int position) {
        holder.userName.setText(usersList.get(position).first_name +" "+usersList.get(position).last_name );
        Picasso.with(context).load(usersList.get(position).photo_50).into(holder.userAvatar);
    }

    @Override
    public void onBindViewHolder(UsersHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public class UsersHolder extends RecyclerView.ViewHolder implements RecyclerItemClickListener.OnItemClickListener{

        private TextView userName;
        private ImageView userAvatar;
        public UsersHolder(View itemView) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            userName = (TextView) itemView.findViewById(R.id.user_name);
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
