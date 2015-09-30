package com.ljaymori.cooxing.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.common.MyData;
import com.squareup.picasso.Picasso;

public class UserSearchItemView extends RecyclerView.ViewHolder {

    private ImageView ivPicture;
    private TextView tvName;
    private TextView tvFollowing;

    public UserSearchItemView(View itemView) {
        super(itemView);

        ivPicture = (ImageView) itemView.findViewById(R.id.image_user_search_item);
        tvName = (TextView) itemView.findViewById(R.id.text_user_name_search_item);
        tvFollowing = (TextView) itemView.findViewById(R.id.text_follow_search_item);
    }

    public void setUserItemView(final UserSearchItemData ud, Context context) {
//        ImageLoader.getInstance().displayImage(NetworkConstant.HTTP_URL + ud.getImageURL(), ivPicture);
        Picasso.with(context)
                .load(NetworkConstant.HTTP_URL + ud.getImageURL())
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(ivPicture);

        tvName.setText(ud.getUserName());
        setFollowingImage(ud.get_id());

        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userClickListener != null) {
                    userClickListener.onUserClick(ud.get_id());
                }
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userClickListener != null) {
                    userClickListener.onUserClick(ud.get_id());
                }
            }
        });

        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(followListener != null) {
                    followListener.onFollowClick(ud.get_id());
                }
            }
        });
    }

    public void setFollowingImage(String id) {
        if(MyData.getInstance().get_id().equals(id)) {
            if(tvFollowing.getVisibility() == View.VISIBLE) {
                tvFollowing.setVisibility(View.GONE);
            }

        } else if (MyData.getInstance().getFollowing().contains(id)) {
            if(tvFollowing.getVisibility() == View.GONE) {
                tvFollowing.setVisibility(View.VISIBLE);
            }
            tvFollowing.setBackgroundResource(R.drawable.btn_unfollow);

        } else {
            if(tvFollowing.getVisibility() == View.GONE) {
                tvFollowing.setVisibility(View.VISIBLE);
            }
            tvFollowing.setBackgroundResource(R.drawable.btn_follow);

        }
    }

    public interface OnSearchUserClickListener {
        void onUserClick(String id);
    }
    private OnSearchUserClickListener userClickListener;
    public void setOnSearchUserClickListener(OnSearchUserClickListener listener) {
        userClickListener = listener;
    }

    public interface OnSearchFollowClickListener {
        void onFollowClick(String id);
    }
    private OnSearchFollowClickListener followListener;
    public void setOnSearchFollowClickListener(OnSearchFollowClickListener listener) {
        followListener = listener;
    }
}
