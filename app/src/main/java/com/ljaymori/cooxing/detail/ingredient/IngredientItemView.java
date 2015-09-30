package com.ljaymori.cooxing.detail.ingredient;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.common.vo.IngredientVO;

public class IngredientItemView extends FrameLayout {

    private TextView tvIngredient;

    public IngredientItemView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_ingredient_detail, this);

        tvIngredient = (TextView) findViewById(R.id.text_ingredient_detail_item);
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvIngredient);
    }


    public void setIngredientItemView(IngredientVO vo) {
        tvIngredient.setText(vo.getName());
    }
}
