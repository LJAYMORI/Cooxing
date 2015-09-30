package com.ljaymori.cooxing.userlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.common.vo.UserVO;
import com.squareup.picasso.Picasso;

public class UserListItemView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private UserVO userVO;
    private Context mContext;

    private ImageView ivPicture;
    private TextView tvNickname;
    private TextView tvFollow;

    public UserListItemView(View itemView) {
        super(itemView);

        ivPicture = (ImageView) itemView.findViewById(R.id.image_user_list);
        tvNickname = (TextView) itemView.findViewById(R.id.text_nickname_user_list);
        tvFollow = (TextView) itemView.findViewById(R.id.text_follow_unfollow_user_list);
    }

    public void setUserListItemView(final UserVO vo, Context context) {
        mContext = context;
        userVO = vo;

//        ImageLoader.getInstance().displayImage(NetworkConstant.HTTP_URL + vo.getProfileImage(), ivPicture);
        Picasso.with(context)
                .load(NetworkConstant.HTTP_URL + vo.getProfileImage())
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(ivPicture);

        tvNickname.setText(vo.getNickname());
        drawFollow(vo.get_id());

        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userClickListener != null) {
                    userClickListener.onUserClick(vo.get_id());
                }
            }
        });

        tvNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userClickListener != null) {
                    userClickListener.onUserClick(vo.get_id());
                }
            }
        });

        tvFollow.setOnClickListener(this);

        setFonts();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvFollow);
    }

    private void drawFollow(String id) {
        if(MyData.getInstance().get_id().equals(id)) {
            if(tvFollow.getVisibility() == View.VISIBLE) {
                tvFollow.setVisibility(View.GONE);
            }

        } else if(MyData.getInstance().getFollowing().contains(id)) {
            if(tvFollow.getVisibility() == View.GONE) {
                tvFollow.setVisibility(View.VISIBLE);
            }
            tvFollow.setBackgroundResource(R.drawable.btn_unfollow);

        } else {
            if(tvFollow.getVisibility() == View.GONE) {
                tvFollow.setVisibility(View.VISIBLE);
            }
            tvFollow.setBackgroundResource(R.drawable.btn_follow);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_follow_unfollow_user_list: {
                String id = userVO.get_id();
                if (MyData.getInstance().getFollowing().contains(id)) {
                    ((UserListActivity) mContext).unfollow(id);

                } else {
                    ((UserListActivity) mContext).follow(id);

                }
                break;
            }
        }
    }

    public interface OnUserListClickListener {
        void onUserClick(String id);
    }
    private OnUserListClickListener userClickListener;
    public void setOnUserListClickListener(OnUserListClickListener listener) {
        userClickListener = listener;
    }

}
