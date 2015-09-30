package com.ljaymori.cooxing.write.ingredient;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.write.WriteActivity;
import com.ljaymori.cooxing.write.tag.TagItemData;

import java.util.ArrayList;

public class IngredientItemAdapter extends RecyclerView.Adapter<IngredientItemView> {

    private Context mContext;
    private ArrayList<IngredientItemData> items = new ArrayList<IngredientItemData>();

    public IngredientItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<IngredientItemData> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void add(int position, IngredientItemData id) {
        items.add(position, id);
        notifyItemInserted(position);
    }

    public boolean add(IngredientItemData id) {
        for (IngredientItemData data : items) {
            if (data.getName().equals(id.getName())) {
                return false;
            }
        }

        add(items.size(), id);
        return true;
    }

    public void remove(IngredientItemData id) {
        remove(items.indexOf(id));
    }

    public void remove(int position) {
        if (position != -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public IngredientItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_ingredient_write, parent, false);

        return new IngredientItemView(v);
    }

    @Override
    public void onBindViewHolder(IngredientItemView holder, int position) {
        holder.setIngredientItemView(items.get(position));
        holder.setOnIngredientItemRemoveListener(new IngredientItemView.OnIngredientItemRemoveListener() {
            @Override
            public void onRemoveClick(String name) {
                int size = items.size();
                for (int i = 0; i < size; i++) {
                    IngredientItemData id = items.get(i);
                    if(id.getName().equals(name)) {
                        remove(i);
                        ((WriteActivity) mContext).getIngredientFragment().initTagList();
                        ((WriteActivity) mContext).checkTag();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((WriteActivity) mContext).getIngredientFragment().showingTutorial(true);
                            }
                        }, 200);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<TagItemData> getItems() {
        ArrayList<TagItemData> list = new ArrayList<TagItemData>();

        for (IngredientItemData id : items) {
            TagItemData td = new TagItemData();
            td.setName(id.getName());

            list.add(td);
        }

        return list;
    }
}
