package com.ljaymori.cooxing.notify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyItemView> {

    private Context mContext;
    private ArrayList<NotifyItemData> items = new ArrayList<NotifyItemData>();

    public NotifyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<NotifyItemData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public NotifyItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_notify, parent, false);

        return new NotifyItemView(v);
    }

    @Override
    public void onBindViewHolder(NotifyItemView holder, int position) {
        holder.setNotifyItemView(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
