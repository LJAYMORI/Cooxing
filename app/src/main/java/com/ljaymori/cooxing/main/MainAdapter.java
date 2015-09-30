package com.ljaymori.cooxing.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.vo.RecipeVO;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainItemView>
        implements MainItemView.OnMainItemChangeListener {

    private ArrayList<RecipeVO> items = new ArrayList<RecipeVO>();
    private Context mContext;

    public MainAdapter(Context context) {
        mContext = context;
    }

    public void addAll(ArrayList<RecipeVO> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void update(int position, RecipeVO vo) {
        items.set(position, vo);
        notifyItemChanged(position);
    }

    @Override
    public MainItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);

        return new MainItemView(v);
    }

    @Override
    public void onBindViewHolder(MainItemView holder, final int position) {
        holder.setContext(mContext);
        holder.setOnMainItemChangeListener(this);
        holder.setMainItemView(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<RecipeVO> getItems() {
        return items;
    }


    /**
     * MainItemView.OnMainItemChangeListener
     */
    @Override
    public void onMainItemChanged(RecipeVO vo) {
        int size = items.size();
        for (int i = 0; i < size; i++) {
            RecipeVO prevVO = items.get(i);
            if(prevVO.get_id().equals(vo.get_id())) {
                items.set(i, vo);
//                notifyItemChanged(i);
                notifyDataSetChanged();
                break;
            }
        }
    }
}
