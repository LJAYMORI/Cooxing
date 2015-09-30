package com.ljaymori.cooxing.search.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.search.SearchActivity;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryView> {

    private ArrayList<String> items = new ArrayList<String>();
    private Context mContext;

    public SearchHistoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<String> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        ((SearchActivity) mContext).removeHistory(items.remove(index));
        notifyDataSetChanged();
    }

    public void remove(String content) {
        int index = items.indexOf(content);
        if(index != -1) {
            remove(index);
        }
    }

    @Override
    public SearchHistoryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search_history, parent, false);

        return new SearchHistoryView(v);
    }

    @Override
    public void onBindViewHolder(SearchHistoryView holder, int position) {
        holder.setHistoryView(items.get(position));
        holder.setOnHistoryClickListener(new SearchHistoryView.OnHistoryClickListener() {
            @Override
            public void onHistoryClick(String keyword) {
                ((SearchActivity) mContext).search(keyword);
            }
        });
        holder.setOnHistoryRemoveClickListener(new SearchHistoryView.OnHistoryRemoveClickListener() {
            @Override
            public void onRemoveClick(String content) {
                remove(content);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
