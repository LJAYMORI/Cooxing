package com.ljaymori.cooxing.common.utils;

import android.text.util.Linkify;
import android.widget.EditText;
import android.widget.TextView;

import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.search.SearchActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighlightHashtag {

    public static void drawHashtag(TextView tv) {
        if (isThereSharp(tv.getText().toString())) {
            Pattern tagMatcher = Pattern.compile(CooxingConstant.HASHTAG_PATTERN);
            String searchActivityURL = CooxingConstant.SEARCH_ACTIVITY_URL;

            Linkify.TransformFilter transform = new Linkify.TransformFilter() {
                @Override
                public String transformUrl(Matcher match, String url) {
                    return "?" + SearchActivity.PARAM_KEYWORD + "=" + url.substring(1, url.length());
                }
            };

            Linkify.addLinks(tv, tagMatcher, searchActivityURL, null, transform);

            URLSpanNoUnderline.stripUnderlines(tv);
        }
    }

    public static void drawHashtag(EditText et) {
        if (isThereSharp(et.getText().toString())) {
            Pattern tagMatcher = Pattern.compile(CooxingConstant.HASHTAG_PATTERN);
            String searchActivityURL = CooxingConstant.SEARCH_ACTIVITY_URL;

            Linkify.TransformFilter transform = new Linkify.TransformFilter() {
                @Override
                public String transformUrl(Matcher match, String url) {
                    return "?" + SearchActivity.PARAM_KEYWORD + "=" + url.substring(1, url.length());
                }
            };

            Linkify.addLinks(et, tagMatcher, searchActivityURL, null, transform);

            URLSpanNoUnderline.stripUnderlines(et);
        }
    }

    private static boolean isThereSharp(String str) {
        return str.contains("#");
    }

}
