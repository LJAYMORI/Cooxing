package com.ljaymori.cooxing.write.tag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.write.WriteActivity;

import java.util.ArrayList;

public class TagItemAdapter extends RecyclerView.Adapter<TagItemView> {

    private Context mContext;
    private ArrayList<TagItemData> items = new ArrayList<TagItemData>();

    public TagItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<TagItemData> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void add(int position, TagItemData td) {
        items.add(position, td);
        notifyItemInserted(position);
    }

    public void add(TagItemData td) {
        int position = items.size();
        add(position, td);
    }

    public void remove(TagItemData td) {
        remove(items.indexOf(td));
    }

    public void remove(int position) {
        if (position != -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public TagItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_tag_write, parent, false);

        return new TagItemView(v);
    }

    @Override
    public void onBindViewHolder(TagItemView holder, int position) {
        holder.setTagItemView(items.get(position));
        holder.setOnTagItemViewClickListener(new TagItemView.OnTagItemViewClickListener() {
            @Override
            public void onTagItemClick(String tag) {
                ((WriteActivity) mContext).clickTag(tag);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<TagItemData> getItems() {
        return items;
    }
}
