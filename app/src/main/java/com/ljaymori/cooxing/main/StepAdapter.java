package com.ljaymori.cooxing.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepItemView> {

    private ArrayList<StepItemData> items = new ArrayList<StepItemData>();
    private Context mContext;

    public StepAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<StepItemData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public StepItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_main_recipe_step, parent, false);

        return new StepItemView(v);
    }

    @Override
    public void onBindViewHolder(final StepItemView holder, final int position) {
        holder.setStepItemView(items.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
