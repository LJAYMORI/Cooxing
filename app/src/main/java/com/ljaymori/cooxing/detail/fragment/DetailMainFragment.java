package com.ljaymori.cooxing.detail.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.common.utils.URLSpanNoUnderline;
import com.ljaymori.cooxing.detail.DetailActivity;
import com.ljaymori.cooxing.detail.ExpandableHeightGridView;
import com.ljaymori.cooxing.detail.ingredient.IngredientAdapter;
import com.ljaymori.cooxing.search.SearchActivity;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailMainFragment extends DetailParentFragment {

    private ScrollView scrollView;

    private ImageView ivPicture;

    private TextView tvName;
    private TextView tvDesc;
    private TextView tvDate;

    private ImageView ivUser;
    private TextView tvNickname;


    private TextView tvIngredient;
    private ExpandableHeightGridView gridView;
    private IngredientAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecipeVO();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_main, container, false);

        scrollView = (ScrollView) v.findViewById(R.id.scrollView_detail_main);
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                int scrollY = scrollView.getScrollY();
//
//                Log.i("scrollY", scrollY + "");
//            }
//        });

        ivPicture = (ImageView) v.findViewById(R.id.image_detail_main);
        tvName = (TextView) v.findViewById(R.id.text_recipe_name_detail_main);
        tvDesc = (TextView) v.findViewById(R.id.text_recipe_description_detail_main);
        tvDate = (TextView) v.findViewById(R.id.text_recipe_date_detail_main);
        ivUser = (ImageView) v.findViewById(R.id.image_user_detail_main);
        tvNickname = (TextView) v.findViewById(R.id.text_nickname_detail_main);

        tvIngredient = (TextView) v.findViewById(R.id.text_ingredient_detail_main);

        gridView = (ExpandableHeightGridView) v.findViewById(R.id.gridView_ingredient_detail_main);
        gridView.setExpanded(true);

        mAdapter = new IngredientAdapter(getActivity());

        gridView.setAdapter(mAdapter);

        setFonts();

        init();

//        moveScroll();

        return v;
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvNickname, tvName, tvIngredient);
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvDate, tvDesc);
    }

    @Override
    public void init() {
        Glide.with(getActivity())
                .load(NetworkConstant.HTTP_URL + recipeVO.getImages().get(recipeVO.getImages().size() - 1))
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
                .into(ivPicture);

        tvName.setText(recipeVO.getTitle());
        tvDesc.setText(recipeVO.getDescription());
        tvDate.setText(convertDate(recipeVO.getRegisterDate()));

        drawHashtag(tvName, recipeVO.getTitle());
        drawHashtag(tvDesc, recipeVO.getDescription());

        Picasso.with(getActivity())
                .load(NetworkConstant.HTTP_URL + recipeVO.getUser().getProfileImage())
                .placeholder(R.drawable.image_placeholder)
                .centerCrop()
                .fit()
                .into(ivUser);

        tvNickname.setText(recipeVO.getUser().getNickname());
        mAdapter.addAll(recipeVO.getIngredients());
    }

    private void drawHashtag(TextView tv, String content) {
        tv.setText(content);

        if(content.contains("#")) {
            Pattern tagMatcher = Pattern.compile(CooxingConstant.HASHTAG_PATTERN);

            String searchURL = CooxingConstant.SEARCH_ACTIVITY_URL;

            Linkify.TransformFilter transform = new Linkify.TransformFilter() {

                @Override
                public String transformUrl(Matcher match, String url) {
                    return "?" + SearchActivity.PARAM_KEYWORD + "=" + url.substring(1, url.length());
                }
            };

            Linkify.addLinks(tv, tagMatcher, searchURL, null, transform);

            URLSpanNoUnderline.stripUnderlines(tv);
        }
    }

    private String convertDate(String date) {
        return date.substring(0, 10);
    }

    private void moveScroll() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
//                scrollView.smoothScrollTo(0, 0);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 100);
    }

    @Override
    public void initRecipeVO() {
        recipeVO = ((DetailActivity) getActivity()).getRecipeVO();
        if (recipeVO == null) {
            Toast.makeText(getActivity(), getString(R.string.detail_message_no_extra), Toast.LENGTH_SHORT).show();
            onDestroy();
        }
    }
}
