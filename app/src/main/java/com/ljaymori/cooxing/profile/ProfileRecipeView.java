package com.ljaymori.cooxing.profile;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.common.utils.FontUtils;

public class ProfileRecipeView extends ProfileParentView{

    private View viewItem;
    private ImageView ivImage;
    private TextView tvName;

    public ProfileRecipeView(View itemView) {
        super(itemView);

        viewItem = itemView.findViewById(R.id.view_recipe_profile);
        ivImage = (ImageView) itemView.findViewById(R.id.image_recipe_profile);
        tvName = (TextView) itemView.findViewById(R.id.text_recipe_name_profile);

        setFonts();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvName);
    }

    public void setRecipeView(final ProfileData pd, Context context) {
        Glide.with(context)
                .load(NetworkConstant.HTTP_URL + pd.getRecipe().getImages().get(pd.getRecipe().getImages().size() - 1))
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImage);

        tvName.setText(pd.getRecipe().getTitle());
        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeListener != null) {
                    recipeListener.onRecipeClick(pd.getRecipe().get_id());
                }
            }
        });
    }

    public interface OnClickRecipeViewListener {
        void onRecipeClick(String id);
    }
    private OnClickRecipeViewListener recipeListener;
    public void setOnClickRecipeViewListener(OnClickRecipeViewListener listener) {
        recipeListener = listener;
    }
}
