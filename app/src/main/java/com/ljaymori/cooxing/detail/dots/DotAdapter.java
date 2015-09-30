package com.ljaymori.cooxing.detail.dots;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class DotAdapter extends RecyclerView.Adapter<DotView> {

    private ArrayList<String> items = new ArrayList<String>();
    private Context mContext;

    public DotAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<String> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public DotView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_dot_detail, parent, false);

        return new DotView(v);
    }

    @Override
    public void onBindViewHolder(DotView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
