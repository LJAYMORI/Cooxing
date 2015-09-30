package com.ljaymori.cooxing.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;

public class StepItemView extends RecyclerView.ViewHolder {

    private ImageView ivPicture;

    public StepItemView(View itemView) {
        super(itemView);

        ivPicture = (ImageView) itemView.findViewById(R.id.image_step_main_item);
    }

    public void setStepItemView(StepItemData sd, Context context) {
        Glide.with(context)
                .load(NetworkConstant.HTTP_URL + sd.getPicturePath())
                .fitCenter()
                .placeholder(R.drawable.image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPicture);
    }
}
