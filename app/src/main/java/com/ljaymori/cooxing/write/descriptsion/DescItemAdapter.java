package com.ljaymori.cooxing.write.descriptsion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.write.WriteActivity;

import java.util.ArrayList;

public class DescItemAdapter extends RecyclerView.Adapter<DescItemParentView> {

    public static final int TYPE_TAB = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ADD = 2;


    private Context mContext;
    private ArrayList<DescItemData> items = new ArrayList<DescItemData>();

    public DescItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void initItems(ArrayList<DescItemData> list) {
        items.addAll(list);
    }

    public void addAll(ArrayList<DescItemData> list) {
        items.addAll(1, list);

//        int size = list.size();
//        for (int i = 0; i < size; i++) {
//            items.add(items.size() - 1, list.get(i));
//        }
        items.get(items.size()-1).setCount(items.size()-2);
        notifyDataSetChanged();
    }

    public void add(int position, DescItemData dd) {
        items.add(position, dd);
        notifyItemInserted(position);
    }

    public void add(DescItemData dd) {
        add(items.size() - 1, dd);
    }

    public void updateDesc(int position, String desc) {
        items.get(position).setDescription(desc);
    }

    public void setTabActivate(int size) {
        Log.i("setTabActivate", size+"");
        if(size > 0) {
            items.get(0).setIsActivated(true);
        } else {
            items.get(0).setIsActivated(false);
        }
        notifyItemChanged(0);
    }

    public void clear() {
        ArrayList<DescItemData> list = new ArrayList<DescItemData>();
        list.add(items.get(0));
        list.add(items.get(items.size()-1));

        items.clear();
        items.addAll(list);

        notifyDataSetChanged();
    }


    @Override
    public DescItemParentView onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM || viewType == TYPE_ADD) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_description_write, parent, false);

            return new DescItemView(v);

        } else if (viewType == TYPE_TAB) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_description_option_write, parent, false);

            return new DescTabItemView(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(DescItemParentView holder, final int position) {
        final DescItemData data = items.get(position);
        int type = data.getType();

        if (type == TYPE_ITEM) {
            ((DescItemView) holder).setDescItemView(data, mContext);
            ((DescItemView) holder).setOnItemFocusChangListener(new DescItemView.OnItemFocusChangeListener() {
                @Override
                public void onFocused(boolean check, int id) {
                    if (check) {
                        ((WriteActivity) mContext).setFocusedEditText(id);
                        ((WriteActivity) mContext).setFocusedPosition(position);
                    }
                }
            });
            ((DescItemView) holder).setOnItemTextChangeListener(new DescItemView.OnItemTextChangeListener() {
                @Override
                public void onChanged(String s) {
                    ((WriteActivity) mContext).getDescFragment().checkTags();
                }
            });
            ((DescItemView) holder).setOnItemRemoveClickListener(new DescItemView.OnItemRemoveClickListener() {
                @Override
                public void onRemoveClick() {

                }
            });
            ((DescItemView) holder).setOnItemLongClickListener(new DescItemView.OnItemLongClickListener() {
                @Override
                public void onItemLongClick() {

                }
            });


        } else if (type == TYPE_ADD) {
            ((DescItemView) holder).setDescItemView(data, mContext);
            ((DescItemView) holder).setOnDescAddItemClickListener(new DescItemView.OnDescAddItemClickListener() {
                @Override
                public void onDescAddClick() {
                    if (adapterListener != null) {
                        adapterListener.onAddItemEvent();
                    }
                }
            });

        } else if (type == TYPE_TAB) {
            ((DescTabItemView) holder).setDescTabView(data);
            ((DescTabItemView) holder).setOnDescTabItemClickListener(new DescTabItemView.OnDescTabItemClickListener() {
                @Override
                public void onRemoveAllClick() {
                    if (adapterListener != null && items.get(0).isActivated()) {
                        adapterListener.onRemoveAllEvent();
                    }
                }

                @Override
                public void onEditOrderClick() {
                    if (adapterListener != null && items.get(0).isActivated()) {
                        adapterListener.onEditOrderEvent();
                    }
                }
            });
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

    public ArrayList<DescItemData> getItems() {
        return items;
    }

    public interface OnDescAdapterEventListener {
        void onRemoveAllEvent();

        void onEditOrderEvent();

        void onAddItemEvent();
    }

    private OnDescAdapterEventListener adapterListener;

    public void setOnDescAdapterEventListener(OnDescAdapterEventListener listener) {
        adapterListener = listener;
    }

}
