package com.ljaymori.cooxing.userlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.vo.UserVO;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListItemView> {

    private ArrayList<UserVO> items = new ArrayList<UserVO>();
    private Context mContext;

    public UserListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<UserVO> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void init(ArrayList<UserVO> list) {
        items.clear();
        addAll(list);
    }

    @Override
    public UserListItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false);

        return new UserListItemView(v);
    }

    @Override
    public void onBindViewHolder(UserListItemView holder, int position) {
        holder.setUserListItemView(items.get(position), mContext);
        holder.setOnUserListClickListener(new UserListItemView.OnUserListClickListener() {
            @Override
            public void onUserClick(String id) {
                ((UserListActivity) mContext).userClick(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
