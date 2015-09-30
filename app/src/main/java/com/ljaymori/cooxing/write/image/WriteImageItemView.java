package com.ljaymori.cooxing.write.image;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ljaymori.cooxing.R;

import java.io.File;

public class WriteImageItemView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private WriteImageItemData itemData;

    private View viewItem;
    private ImageView ivImage;

    private View viewCount;
    private TextView tvCount;

    public WriteImageItemView(View itemView) {
        super(itemView);

        viewItem = itemView.findViewById(R.id.view_write_image_item);
        ivImage = (ImageView) itemView.findViewById(R.id.image_write_image_item);

        viewCount = itemView.findViewById(R.id.view_count_write_image_item);
        tvCount = (TextView) itemView.findViewById(R.id.text_count_write_image_item);

        viewItem.setOnClickListener(this);
    }

    public synchronized void setImageItemView(WriteImageItemData wd, Context context) {
        itemData = wd;

        Glide.with(context)
                .load(new File(itemData.getMediaData().getFilePath()))
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImage);


        if (wd.getCountNum() > 0) {
            viewCount.setVisibility(View.VISIBLE);

            int res = ((WriteImageActivity)context).getImageCountResource(wd.getCountNum());
            tvCount.setBackgroundResource(res);
        } else {
            viewCount.setVisibility(View.GONE);
//            tvCount.setText(wd.getCountNum() + "");
        }
    }

    @Override
    public void onClick(View v) {
        if (imageListener != null) {
            switch (v.getId()) {
                case R.id.view_write_image_item: {
                    if (viewCount.getVisibility() == View.GONE) {
                        imageListener.onImageItemClick(true, itemData.getMediaData().getFilePath(), itemData.getCountNum());

                    } else {
                        imageListener.onImageItemClick(false, itemData.getMediaData().getFilePath(), itemData.getCountNum());

                    }
                    break;
                }
            }
        }
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(boolean isSelected, String filePath, int countNum);
    }

    private OnImageItemClickListener imageListener;

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        imageListener = listener;
    }

}
