package com.ljaymori.cooxing.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.MyData;

import java.util.ArrayList;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchItemView> {

    private Context mContext;
    private ArrayList<UserSearchItemData> items = new ArrayList<UserSearchItemData>();

    public UserSearchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<UserSearchItemData> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public UserSearchItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search_user, parent, false);

        return new UserSearchItemView(v);
    }

    @Override
    public void onBindViewHolder(UserSearchItemView holder, int position) {
        holder.setUserItemView(items.get(position), mContext);
        holder.setOnSearchUserClickListener(new UserSearchItemView.OnSearchUserClickListener() {
            @Override
            public void onUserClick(String id) {
                ((SearchActivity) mContext).userClick(id);
            }
        });
        holder.setOnSearchFollowClickListener(new UserSearchItemView.OnSearchFollowClickListener() {
            @Override
            public void onFollowClick(String id) {
                if (MyData.getInstance().getFollowing().contains(id)) {
                    ((SearchActivity) mContext).unfollow(id);
                } else {
                    ((SearchActivity) mContext).follow(id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
