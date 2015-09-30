package com.ljaymori.cooxing.like;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.main.MainActivity;

import java.util.ArrayList;

public class LikeRecipeAdapter extends RecyclerView.Adapter<LikeRecipeView> {

    private ArrayList<LikeRecipeData> items = new ArrayList<LikeRecipeData>();
    private Context mContext;

    public LikeRecipeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<LikeRecipeData> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public LikeRecipeView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_like, parent, false);

        return new LikeRecipeView(v);
    }

    @Override
    public void onBindViewHolder(LikeRecipeView holder, int position) {
        holder.setLikeRecipView(items.get(position), mContext);
        holder.setOnLikeRecipeClickListener(new LikeRecipeView.OnLikeRecipeClickListener() {
            @Override
            public void onLikeRecipeClick(String id) {
                ((MainActivity) mContext).onClickRecipe(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
