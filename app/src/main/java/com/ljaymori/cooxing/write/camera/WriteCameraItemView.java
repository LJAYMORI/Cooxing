package com.ljaymori.cooxing.write.camera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ljaymori.cooxing.R;

import java.io.File;

public class WriteCameraItemView extends RecyclerView.ViewHolder {

    private ImageView ivPicture;
    private Button btnDel;
    private String imagePath;

    public WriteCameraItemView(View itemView) {
        super(itemView);

        ivPicture = (ImageView) itemView.findViewById(R.id.image_picture_write_camera_item);
        btnDel = (Button) itemView.findViewById(R.id.button_del_picture_write_camera_item);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListener != null) {
                    deleteListener.onDelete(imagePath);
                }
            }
        });
    }

    public void setImage(String path, Context context) {
        imagePath = path;
        Glide.with(context)
                .load(new File(path))
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
                .into(ivPicture);
    }

    public interface OnWriteCameraItemDeleteListener {
        void onDelete(String path);
    }

    OnWriteCameraItemDeleteListener deleteListener;

    public void setOnDeleteListener(OnWriteCameraItemDeleteListener listener) {
        deleteListener = listener;
    }
}
