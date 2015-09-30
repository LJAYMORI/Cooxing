package com.ljaymori.cooxing.like;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.common.utils.FontUtils;

public class LikeRecipeView extends RecyclerView.ViewHolder {

    private View viewItem;
    private ImageView ivImage;
    private TextView tvName;

    public LikeRecipeView(View itemView) {
        super(itemView);

        viewItem = itemView.findViewById(R.id.view_recipe_like);
        ivImage = (ImageView) itemView.findViewById(R.id.image_recipe_like);
        tvName = (TextView) itemView.findViewById(R.id.text_recipe_name_like);

        setFonts();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvName);
    }

    public void setLikeRecipView(final LikeRecipeData ld, Context context) {
        Glide.with(context)
                .load(NetworkConstant.HTTP_URL + ld.getImageURL())
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImage);

        tvName.setText(ld.getRecipeName());
        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likeRecipeClickListener != null) {
                    likeRecipeClickListener.onLikeRecipeClick(ld.getRecipeID());
                }
            }
        });
    }

    public interface OnLikeRecipeClickListener {
        void onLikeRecipeClick(String id);
    }
    private OnLikeRecipeClickListener likeRecipeClickListener;
    public void setOnLikeRecipeClickListener(OnLikeRecipeClickListener listener) {
        likeRecipeClickListener = listener;
    }
}
