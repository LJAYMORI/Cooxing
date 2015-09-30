package com.ljaymori.cooxing.detail.ingredient;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ljaymori.cooxing.common.vo.IngredientVO;

import java.util.ArrayList;

public class IngredientAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<IngredientVO> items = new ArrayList<IngredientVO>();

    public IngredientAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<IngredientVO> list) {
        items.addAll(list);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        IngredientItemView view;
        if(convertView == null) {
            view = new IngredientItemView(mContext);
        } else {
            view = (IngredientItemView) convertView;
        }

        view.setIngredientItemView(items.get(position));

        return view;
    }
}
