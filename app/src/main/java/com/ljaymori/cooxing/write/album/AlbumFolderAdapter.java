package com.ljaymori.cooxing.write.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class AlbumFolderAdapter extends RecyclerView.Adapter<AlbumFolderItemView> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_OTHER = 1;


    private Context mContext;
    private ArrayList<AlbumFolderItemData> items = new ArrayList<AlbumFolderItemData>();

    public AlbumFolderAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<AlbumFolderItemData> list) {
        items.addAll(list);
    }

    @Override
    public AlbumFolderItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_album_folder_write, parent, false);

        return new AlbumFolderItemView(v);
    }

    @Override
    public void onBindViewHolder(AlbumFolderItemView holder, int position) {
        holder.setAlbumFolderView(items.get(position), mContext);
        holder.setOnAlbumFolderItemClickListener(new AlbumFolderItemView.OnAlbumFolderItemClickListener() {
            @Override
            public void onAlbumFolderItemClick(int albumType, String albumName) {
                ((WriteAlbumFolderActivity) mContext).showAlbum(albumType, albumName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }
}
