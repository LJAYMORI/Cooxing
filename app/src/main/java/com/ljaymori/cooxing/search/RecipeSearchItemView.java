package com.ljaymori.cooxing.search;

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

public class RecipeSearchItemView extends RecyclerView.ViewHolder {

    private View viewRecipe;
    private ImageView ivPicture;
    private TextView tvName;

    public RecipeSearchItemView(View itemView) {
        super(itemView);

        viewRecipe = itemView.findViewById(R.id.view_recipe_search_item);
        ivPicture = (ImageView) itemView.findViewById(R.id.image_recipe_search_item);
        tvName = (TextView) itemView.findViewById(R.id.text_recipe_name_search_item);

        setFonts();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvName);
    }

    public void setRecipeItemView(final RecipeSearchItemData rd, Context context) {

        viewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchListener != null) {
                    searchListener.onRecipeClick(rd.getRecipeID());
                }
            }
        });

        Glide.with(context)
                .load(NetworkConstant.HTTP_URL + rd.getImageURL())
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPicture);

        tvName.setText(rd.getRecipeName());
    }


    public interface OnSearchRecipeListener {
        void onRecipeClick(String id);
    }

    private OnSearchRecipeListener searchListener;

    public void setOnSearchRecipeListener(OnSearchRecipeListener listener) {
        searchListener = listener;
    }
}
