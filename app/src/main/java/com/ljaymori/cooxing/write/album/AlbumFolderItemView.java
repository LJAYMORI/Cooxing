package com.ljaymori.cooxing.write.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;

import java.io.File;

public class AlbumFolderItemView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private AlbumFolderItemData itemData;

    private TextView tvTop;
    private TextView tvBottom;

    private View viewItem;
    private ImageView ivImage;
    private TextView tvAlbumName;

    public AlbumFolderItemView(View itemView) {
        super(itemView);

        tvTop = (TextView) itemView.findViewById(R.id.text_top_margin_album_folder_item);
        tvBottom = (TextView) itemView.findViewById(R.id.text_bottom_margin_album_folder_write_item);

        viewItem = itemView.findViewById(R.id.view_album_folder_item);
        ivImage = (ImageView) itemView.findViewById(R.id.image_thumbnail_album_folder_item);
        tvAlbumName = (TextView) itemView.findViewById(R.id.text_name_album_folder_item);

        setFonts();
    }

    public void setAlbumFolderView(AlbumFolderItemData ad, Context context) {
        itemData = ad;

        drawMargin(ad.getType());

        Glide.with(context)
                .load(new File(ad.getThumbnail()))
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImage);

        tvAlbumName.setText(ad.getAlbumName());
        viewItem.setOnClickListener(this);
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvAlbumName);
    }

    private void drawMargin(int type) {
        if(type == AlbumFolderAdapter.TYPE_ALL) {
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_album_folder_item : {
                if(albumListener != null) {
                    albumListener.onAlbumFolderItemClick(itemData.getType(), itemData.getAlbumName());
                }
                break;
            }
        }
    }

    public interface OnAlbumFolderItemClickListener {
        void onAlbumFolderItemClick(int albumType, String albumName);
    }
    private OnAlbumFolderItemClickListener albumListener;
    public void setOnAlbumFolderItemClickListener(OnAlbumFolderItemClickListener listener) {
        albumListener = listener;
    }
}
