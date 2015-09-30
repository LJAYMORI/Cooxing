package com.ljaymori.cooxing.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class RecipeSearchAdapter extends RecyclerView.Adapter<RecipeSearchItemView> {

    private Context mContext;
    private ArrayList<RecipeSearchItemData> items = new ArrayList<RecipeSearchItemData>();

    public RecipeSearchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<RecipeSearchItemData> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public RecipeSearchItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search_recipe, parent, false);

        return new RecipeSearchItemView(v);
    }

    @Override
    public void onBindViewHolder(RecipeSearchItemView holder, int position) {
        holder.setRecipeItemView(items.get(position), mContext);
        holder.setOnSearchRecipeListener(new RecipeSearchItemView.OnSearchRecipeListener() {
            @Override
            public void onRecipeClick(String id) {
                ((SearchActivity) mContext).recipeClick(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
