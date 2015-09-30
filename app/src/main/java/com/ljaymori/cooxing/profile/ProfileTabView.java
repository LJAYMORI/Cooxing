package com.ljaymori.cooxing.profile;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.utils.FontUtils;

public class ProfileTabView extends ProfileParentView implements View.OnClickListener {

    private View viewTab;

    private TextView tvWroteRecipe;
    private TextView tvInterestRecipe;
    private TextView tvOpenRecipe;

    private View viewEmpty;
    private TextView tvEmpty;

    public ProfileTabView(View itemView, String user_id) {
        super(itemView);

        viewEmpty = itemView.findViewById(R.id.view_empty_profile_tab);
        tvEmpty = (TextView) itemView.findViewById(R.id.text_empty_profile_tab);
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvEmpty);

        if (MyData.getInstance().get_id().equals(user_id)) {
            viewTab = itemView.findViewById(R.id.view_tab_other_recipe_profile);
            viewTab.setVisibility(View.GONE);

            tvWroteRecipe = (TextView) itemView.findViewById(R.id.text_wrote_my_recipe_profile);
            tvInterestRecipe = (TextView) itemView.findViewById(R.id.text_interest_my_recipe_profile);
            tvOpenRecipe = (TextView) itemView.findViewById(R.id.text_open_my_recipe_profile);

            FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvWroteRecipe, tvInterestRecipe, tvOpenRecipe);

            tvWroteRecipe.setOnClickListener(this);
            tvInterestRecipe.setOnClickListener(this);
            tvOpenRecipe.setOnClickListener(this);

        } else {
            viewTab = itemView.findViewById(R.id.view_tab_my_recipe_profile);
            viewTab.setVisibility(View.GONE);

            tvWroteRecipe = (TextView) itemView.findViewById(R.id.text_wrote_other_recipe_profile);
            tvInterestRecipe = (TextView) itemView.findViewById(R.id.text_interest_other_recipe_profile);

            FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvWroteRecipe, tvInterestRecipe);

            tvWroteRecipe.setOnClickListener(this);
            tvInterestRecipe.setOnClickListener(this);
        }

    }

    public void setProfileTabView(ProfileData pd, Context context) {
        if (pd.getMessage().equals(ProfileActivity.NOT_EMPTY)) {
            if (viewEmpty.getVisibility() == View.VISIBLE) {
                viewEmpty.setVisibility(View.GONE);
            }

        } else {
            if (viewEmpty.getVisibility() == View.GONE) {
                viewEmpty.setVisibility(View.VISIBLE);
            }
            tvEmpty.setText(pd.getMessage());
        }
        drawTabBackground(pd.getFocusedTabPosition(), context);
    }

    private void drawTabBackground(int focusedTabPosition, Context context) {
        switch (focusedTabPosition) {
            case ProfileActivity.TAB_WROTE_RECIPE: {
                tvWroteRecipe.setBackgroundResource(R.drawable.background_tab_focused_left);
                tvWroteRecipe.setTextColor(context.getResources().getColor(R.color.tab_selected_text));

                if (tvOpenRecipe == null) {
                    tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_right);
                    tvInterestRecipe.setTextColor(context.getResources().getColor(R.color.tab_unselected_text));

                } else {
                    tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_center);
                    tvInterestRecipe.setTextColor(context.getResources().getColor(R.color.tab_unselected_text));

                    tvOpenRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_right);
                    tvOpenRecipe.setTextColor(context.getResources().getColor(R.color.tab_unselected_text));
                }
                break;
            }
            case ProfileActivity.TAB_BOOKMARK_RECIPE: {
                tvWroteRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_left);
                tvWroteRecipe.setTextColor(context.getResources().getColor(R.color.tab_unselected_text));

                if (tvOpenRecipe == null) {
                    tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_focused_right);
                    tvInterestRecipe.setTextColor(context.getResources().getColor(R.color.tab_selected_text));

                } else {
                    tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_focused_center);
                    tvInterestRecipe.setTextColor(context.getResources().getColor(R.color.tab_selected_text));

                    tvOpenRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_right);
                    tvOpenRecipe.setTextColor(context.getResources().getColor(R.color.tab_unselected_text));
                }
                break;
            }
            case ProfileActivity.TAB_OPENED_RECIPE: {
                tvWroteRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_left);
                tvWroteRecipe.setTextColor(context.getResources().getColor(R.color.tab_unselected_text));

                tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_center);
                tvInterestRecipe.setTextColor(context.getResources().getColor(R.color.tab_unselected_text));

                tvOpenRecipe.setBackgroundResource(R.drawable.background_tab_focused_right);
                tvOpenRecipe.setTextColor(context.getResources().getColor(R.color.tab_selected_text));
                break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {

            switch (v.getId()) {
                case R.id.text_wrote_my_recipe_profile: {
                    mListener.onClickWroteItem();
                    break;
                }
                case R.id.text_interest_my_recipe_profile: {
                    mListener.onClickInterestItem();
                    break;
                }
                case R.id.text_open_my_recipe_profile: {
                    mListener.onClickOpenItem();
                    break;
                }
                case R.id.text_wrote_other_recipe_profile: {
                    mListener.onClickWroteItem();
                    break;
                }
                case R.id.text_interest_other_recipe_profile: {
                    mListener.onClickInterestItem();
                    break;
                }
            }

        }
    }

    public interface OnTabItemClickListener {
        void onClickWroteItem();

        void onClickInterestItem();

        void onClickOpenItem();
    }

    private OnTabItemClickListener mListener;

    public void setOnTabItemClickListener(OnTabItemClickListener listener) {
        mListener = listener;
    }
}
