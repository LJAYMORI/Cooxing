package com.ljaymori.cooxing.write.ingredient;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;

public class IngredientItemView extends RecyclerView.ViewHolder {

    private IngredientItemData itemData;

    private View viewCover;
    private TextView tvName;
    private TextView tvRemove;

    public IngredientItemView(View itemView) {
        super(itemView);

        viewCover = itemView.findViewById(R.id.view_item_write_page_ingredient);
        tvName = (TextView) itemView.findViewById(R.id.text_item_name_write_page_ingredient);

        tvRemove = (TextView) itemView.findViewById(R.id.text_item_remove_write_page_ingredient);

        setFonts();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvName);
    }

    public void setIngredientItemView(IngredientItemData id) {
        itemData = id;

        tvName.setText(id.getName());
        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeListener != null) {
                    removeListener.onRemoveClick(itemData.getName());
                }
            }
        });
    }

    public interface OnIngredientItemRemoveListener {
        void onRemoveClick(String name);
    }
    private OnIngredientItemRemoveListener removeListener;
    public void setOnIngredientItemRemoveListener(OnIngredientItemRemoveListener listener) {
        removeListener = listener;
    }
}
