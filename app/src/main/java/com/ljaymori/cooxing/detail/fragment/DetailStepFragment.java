package com.ljaymori.cooxing.detail.fragment;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.utils.URLSpanNoUnderline;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.detail.DetailActivity;
import com.ljaymori.cooxing.search.SearchActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailStepFragment extends DetailParentFragment implements View.OnClickListener {

    private RecipeVO recipeVO;
    private int position;

    private ImageView ivStep;

    private TextView tvStopwatch;
    private TextView tvWindow;

    private TextView tvPosition;
    private TextView tvDesc;

    private TextView tvCover;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecipe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_step, container, false);

        ivStep = (ImageView) v.findViewById(R.id.image_main_detail_step);
        tvStopwatch = (TextView) v.findViewById(R.id.text_stopwatch_detail_step);
        tvWindow = (TextView) v.findViewById(R.id.text_window_detail_step);

        tvPosition = (TextView) v.findViewById(R.id.text_position_detail_step);
        tvDesc = (TextView) v.findViewById(R.id.text_step_intro_detail_step);

        tvCover = (TextView) v.findViewById(R.id.text_cover_step_intro_detail_step);

        tvStopwatch.setOnClickListener(this);
        tvWindow.setOnClickListener(this);
        tvCover.setOnClickListener(this);

        return v;
    }

    private void drawPage() {
        Glide.with(getActivity())
                .load(NetworkConstant.HTTP_URL + recipeVO.getImages().get(position))
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivStep);

        drawWindowButton();

        drawHashtag(recipeVO.getSteps().get(position));

        tvPosition.setBackgroundResource(DetailActivity.STEP_IMAGES[position]);

//        HighlightHashtag.drawHashtag(tvDesc);

    }

    private void drawHashtag(String content) {
        tvDesc.setText(content);

        if(content.contains("#")) {
            Pattern tagMatcher = Pattern.compile(CooxingConstant.HASHTAG_PATTERN);

            String searchURL = CooxingConstant.SEARCH_ACTIVITY_URL;

            Linkify.TransformFilter transform = new Linkify.TransformFilter() {

                @Override
                public String transformUrl(Matcher match, String url) {
                    return "?" + SearchActivity.PARAM_KEYWORD + "=" + url.substring(1, url.length());
                }
            };

            Linkify.addLinks(tvDesc, tagMatcher, searchURL, null, transform);

            URLSpanNoUnderline.stripUnderlines(tvDesc);
        }
    }

    public void drawWindowButton() {
        if (((DetailActivity) getActivity()).getIsWindowClicked()) {
            tvWindow.setBackgroundResource(R.drawable.ic_focused_window);
        } else {
            tvWindow.setBackgroundResource(R.drawable.ic_unfocused_window);
        }
    }

    private void initRecipe() {
        position = getArguments().getInt(DetailActivity.PAGE_POSITION, -1);
        recipeVO = ((DetailActivity) getActivity()).getRecipeVO();
    }

    @Override
    public void onResume() {
        super.onResume();
        drawPage();
    }

    private boolean isStopWatchClicked = false;
    private boolean isWindowClicked = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_stopwatch_detail_step: {
                clickStopWatch();
                break;
            }
            case R.id.text_window_detail_step: {
                clickWindow();
                break;
            }
            case R.id.text_cover_step_intro_detail_step: {
                Toast.makeText(getActivity(), "click cover", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void clickStopWatch() {
        if (isStopWatchClicked) {
            isStopWatchClicked = false;

        } else {
            isStopWatchClicked = true;

        }
    }

    private void clickWindow() {
        ((DetailActivity) getActivity()).setWindow();
        if (isWindowClicked) {
            isWindowClicked = false;
        } else {
            isWindowClicked = true;
        }
    }

}
