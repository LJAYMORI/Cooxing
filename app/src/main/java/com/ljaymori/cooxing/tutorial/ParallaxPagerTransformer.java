package com.ljaymori.cooxing.tutorial;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.ljaymori.cooxing.R;

public class ParallaxPagerTransformer implements ViewPager.PageTransformer {

    private int backgroundID = R.id.image_background_tutorial;
//    private int textID;


    @Override
    public void transformPage(View view, float position) {
        View parallaxImageView = view.findViewById(backgroundID);
//        View parallaxTextView = view.findViewById(textID);

        int width = parallaxImageView.getWidth();

        parallaxImageView.setTranslationX(-position * (width / 2));
//        parallaxTextView.setTranslationX(position * (width / 2));
//        parallaxTextView.setRotation(position * 180);
//        parallaxTextView.setScaleX(1 + position);
//        parallaxTextView.setScaleY(1 + position);
//        parallaxTextView.setAlpha(1 + position);

    }
}