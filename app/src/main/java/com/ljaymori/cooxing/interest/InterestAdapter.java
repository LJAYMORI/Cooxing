package com.ljaymori.cooxing.interest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class InterestAdapter extends RecyclerView.Adapter<InterestParentView> {

    private SparseBooleanArray booleanArray = new SparseBooleanArray();

    private ArrayList<InterestItemData> items = new ArrayList<InterestItemData>();
    private Context mContext;

    public InterestAdapter(Context context) {
        mContext = context;
    }

    public void add(InterestItemData id) {
        items.add(id);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<InterestItemData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void initItems(ArrayList<InterestItemData> list) {
        items.clear();
        addAll(list);
    }

    @Override
    public InterestParentView onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == InterestActivity.TYPE_ITEM) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_interest, parent, false);
            return new InterestItemView(v);

        } else if (viewType == InterestActivity.TYPE_HEADER) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_interest_header, parent, false);
            return new InterestHeaderView(v);

        } else {
            return null;

        }
    }


    @Override
    public void onBindViewHolder(final InterestParentView holder, final int position) {
        if (holder instanceof InterestItemView) {
            ((InterestItemView) holder).setOnInterestCheckChangeListener(new InterestItemView.OnInterestItemCheckChangeListener() {
                @Override
                public void onChecked(boolean checked) {
                    if (checked) {
                        booleanArray.append(position, checked);
                    } else {
                        booleanArray.delete(position);
                    }
                    notifyItemChanged(position);
                }
            });
            ((InterestItemView) holder).setItemView(items.get(position), mContext);

        } else if (holder instanceof InterestHeaderView) {
            ((InterestHeaderView) holder).setHeaderName(items.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    public SparseBooleanArray getBooleanArray() {
        return booleanArray;
    }

    public ArrayList<InterestItemData> getItems() {
        return items;
    }
}
