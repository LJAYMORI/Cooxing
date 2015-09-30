package com.ljaymori.cooxing.write.image;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class WriteImageItemAdapter extends RecyclerView.Adapter<WriteImageItemView> {

    private Context mContext;
    private ArrayList<WriteImageItemData> items = new ArrayList<WriteImageItemData>();
    private ArrayList<Integer> selectedList = new ArrayList<Integer>();

    public WriteImageItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<WriteImageItemData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public WriteImageItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_write_image, parent, false);

        return new WriteImageItemView(v);
    }

    @Override
    public void onBindViewHolder(WriteImageItemView holder, int position) {
        Log.i("bindViewHolder", "called");
        holder.setImageItemView(items.get(position), mContext);
        holder.setOnImageItemClickListener(new WriteImageItemView.OnImageItemClickListener() {
            @Override
            public void onImageItemClick(boolean isSelected, String filePath, int countNum) {
                int addedCount = ((WriteImageActivity) mContext).getAddedCount();
                if (isSelected) {
                    int selectedSize = selectedList.size() + addedCount;
                    if (selectedSize < 10) {
                        int size = items.size();
                        for (int i = 0; i < size; i++) {
                            if (filePath.equals(items.get(i).getMediaData().getFilePath())) {
                                selectedList.add(i);
                                items.get(i).setCountNum(selectedSize + 1);
                                notifyItemChanged(i);
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.write_message_add_image_limit), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    int idx = countNum - addedCount - 1;
                    int position = selectedList.remove(idx);
                    items.get(position).setCountNum(-1);
                    notifyItemChanged(position);

                    int selectedListSize = selectedList.size();
                    for (int i = 0; i < selectedListSize; i++) {
                        int index = selectedList.get(i);
                        items.get(index).setCountNum(addedCount + i + 1);
                        notifyItemChanged(index);
                    }

                }
                ((WriteImageActivity) mContext).changeSelectedCount(addedCount + selectedList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<Integer> getSelectedList() {
        return selectedList;
    }
}
