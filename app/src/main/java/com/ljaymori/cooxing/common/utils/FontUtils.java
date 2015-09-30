package com.ljaymori.cooxing.common.utils;

import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

import com.ljaymori.cooxing.common.CooxingApplication;

public class FontUtils {

    public static final int TYPE_BARUN_NOMAL = 0;
    public static final int TYPE_ROBOTO_NOMAL = 1;
    public static final int TYPE_BARUN_BOLD = 2;

    public static void setTextViewFonts(int type, TextView... tvs) {
        Typeface typeface;
        if (type == TYPE_BARUN_NOMAL) {
            typeface = Typeface.createFromAsset(CooxingApplication.getContext().getAssets(), "fonts/nanum_barun_gothic.ttf");
        } else if (type == TYPE_ROBOTO_NOMAL) {
            typeface = Typeface.createFromAsset(CooxingApplication.getContext().getAssets(), "fonts/roboto_medium.ttf");
        } else if (type == TYPE_BARUN_BOLD) {
            typeface = Typeface.createFromAsset(CooxingApplication.getContext().getAssets(), "fonts/nanum_barun_gothic_bold.ttf");
        } else {
            return;
        }

        for(TextView tv : tvs) {
            tv.setTypeface(typeface);
        }

    }

    public static void setEditTextFonts(int type, EditText... ets) {
        Typeface typeface;
        if (type == TYPE_BARUN_NOMAL) {
            typeface = Typeface.createFromAsset(CooxingApplication.getContext().getAssets(), "fonts/nanum_barun_gothic.ttf");
        } else if (type == TYPE_ROBOTO_NOMAL) {
            typeface = Typeface.createFromAsset(CooxingApplication.getContext().getAssets(), "fonts/roboto_medium.ttf");
        } else if (type == TYPE_BARUN_BOLD) {
            typeface = Typeface.createFromAsset(CooxingApplication.getContext().getAssets(), "fonts/nanum_barun_gothic_bold.ttf");
        } else {
            return;
        }

        for(EditText et : ets) {
            et.setTypeface(typeface);
        }

    }
}
