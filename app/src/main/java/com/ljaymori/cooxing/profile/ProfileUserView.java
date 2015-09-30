package com.ljaymori.cooxing.profile;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.auth.result.NetworkResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.userlist.UserListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileUserView extends ProfileParentView implements View.OnClickListener {

    private ImageView ivBackground;

    private TextView tvInterestIcon;
    private TextView tvNickname;

    private TextView tvFollow;
    private TextView tvSetting;

    private ImageView ivImage;
    private TextView tvEdit;
    private TextView tvIntro;

    private View viewFollowing;
    private TextView tvFollowingCount;
    private View viewFollower;
    private TextView tvFollowerCount;

    private Context mContext;
    private ProfileData profileData;

    public ProfileUserView(View v) {
        super(v);

        ivBackground = (ImageView) v.findViewById(R.id.image_background_profile);

        tvInterestIcon = (TextView) v.findViewById(R.id.text_interest_icon_profile);
        tvNickname = (TextView) v.findViewById(R.id.text_nickname_profile);

        tvFollow = (TextView) v.findViewById(R.id.text_follow_profile);
        tvSetting = (TextView) v.findViewById(R.id.text_setting_profile);

        ivImage = (ImageView) v.findViewById(R.id.image_picture_profile);
        tvEdit = (TextView) v.findViewById(R.id.text_edit_profile);
        tvIntro = (TextView) v.findViewById(R.id.text_intro_sentence_profile);

        viewFollowing = v.findViewById(R.id.view_following_profile);
        tvFollowingCount = (TextView) v.findViewById(R.id.text_following_count_profile);
        viewFollower = v.findViewById(R.id.view_follower_profile);
        tvFollowerCount = (TextView) v.findViewById(R.id.text_follower_count_profile);
    }

    public void setUserView(ProfileData pd, Context context) {
        mContext = context;
        profileData = pd;

        drawBackground(pd.getUser().getRecipes(), context);

//        tvInterestIcon.setBackgroundResource(pd.getUser().getInterestIcon());
        tvInterestIcon.setVisibility(View.GONE);
        tvNickname.setText(pd.getUser().getNickname());

        updateProfileImage(pd.getUser().getProfileImage(), context);

        if (MyData.getInstance().get_id().equals(pd.getUser().get_id())) {
            tvEdit.setOnClickListener(this);
            tvFollow.setVisibility(View.GONE);
            tvSetting.setOnClickListener(this);

        } else {
            tvEdit.setVisibility(View.GONE);
            tvSetting.setVisibility(View.GONE);

            if (MyData.getInstance().getFollowing().contains(pd.getUser().get_id())) {
                tvFollow.setBackgroundResource(R.drawable.ic_unfollow_profile);
            } else {
                tvFollow.setBackgroundResource(R.drawable.ic_follow_profile);
            }
            tvFollow.setOnClickListener(this);

        }

        tvIntro.setText(pd.getUser().getIntro());

        viewFollowing.setOnClickListener(this);
        viewFollower.setOnClickListener(this);

        tvFollowingCount.setText(pd.getUser().getFollowing().size() + "");
        tvFollowerCount.setText(pd.getUser().getFollower().size() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_edit_profile: {
                if (profileChangeClickListener != null) {
                    profileChangeClickListener.onProfileChangeClick();
                }
                break;
            }
            case R.id.view_following_profile: {
                ((ProfileActivity) mContext).onClickUserList(UserListActivity.TYPE_FOLLOWING);
                break;
            }
            case R.id.view_follower_profile: {
                ((ProfileActivity) mContext).onClickUserList(UserListActivity.TYPE_FOLLOWER);
                break;
            }
            case R.id.text_follow_profile: {
                String id = profileData.getUser().get_id();
                if (MyData.getInstance().getFollowing().contains(id)) {
                    unfollow(id);
                } else {
                    follow(id);
                }

                break;
            }
            case R.id.text_setting_profile: {
                break;
            }
        }
    }

    public void updateProfileImage(String path, Context context) {
        Picasso.with(context)
                .load(NetworkConstant.HTTP_URL + path)
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(ivImage);
    }

    private void drawBackground(ArrayList<RecipeVO> list, Context context) {
        int size = list.size();
        if (size > 0) {
            RecipeVO vo = list.get(0);
            String imageURL = NetworkConstant.HTTP_URL + vo.getImages().get(vo.getImages().size() - 1);

            Glide.with(context)
                    .load(imageURL)
                    .placeholder(R.drawable.image_placeholder)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivBackground);
        }
    }


    private void follow(final String id) {
        NetworkManager.getInstance().follow(mContext, id, new NetworkManager.OnResultListener<NetworkResult>() {
            @Override
            public void onSuccess(NetworkResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getFollowing().add(id);
                    ((ProfileActivity) mContext).initData();

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(mContext, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(mContext, code + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void unfollow(final String id) {
        NetworkManager.getInstance().unfollow(mContext, id, new NetworkManager.OnResultListener<NetworkResult>() {
            @Override
            public void onSuccess(NetworkResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getFollowing().remove(id);
                    ((ProfileActivity) mContext).initData();

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(mContext, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(mContext, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface OnProfileChangeClickListener {
        void onProfileChangeClick();
    }

    private OnProfileChangeClickListener profileChangeClickListener;

    public void setOnProfileChangeClickListener(OnProfileChangeClickListener listener) {
        profileChangeClickListener = listener;
    }
}
