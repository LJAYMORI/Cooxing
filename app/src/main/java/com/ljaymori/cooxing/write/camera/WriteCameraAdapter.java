package com.ljaymori.cooxing.write.camera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class WriteCameraAdapter extends RecyclerView.Adapter<WriteCameraItemView> {

    private ArrayList<String> items = new ArrayList<String>();
    private Context mContext;

    public WriteCameraAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void add(String path, int position) {
        items.add(path);
        notifyItemInserted(position);
    }

    public void remove(String path) {
        int position = items.indexOf(path);
        if(position == -1) {

        } else {
            ((WriteCameraActivity) mContext).removeFile(items.remove(position));
            notifyItemRemoved(position);
        }
    }

    @Override
    public WriteCameraItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_write_camera, parent, false);

        WriteCameraItemView itemView = new WriteCameraItemView(v);
        itemView.setOnDeleteListener(new WriteCameraItemView.OnWriteCameraItemDeleteListener() {
            @Override
            public void onDelete(String path) {
                remove(path);
            }
        });

        return itemView;
    }

    @Override
    public void onBindViewHolder(WriteCameraItemView holder, int position) {
        holder.setImage(items.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<String> getItems() {
        return items;
    }
}
