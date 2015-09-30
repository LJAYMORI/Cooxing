package com.ljaymori.cooxing.reply;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.vo.CommentUserVO;
import com.squareup.picasso.Picasso;

public class ReplyItemView extends RecyclerView.ViewHolder {

    private ImageView ivPicture;
    private TextView tvNickname;
    private TextView tvContent;
    private TextView tvTime;
    private TextView tvDelete;

    public ReplyItemView(View itemView) {
        super(itemView);

        ivPicture = (ImageView) itemView.findViewById(R.id.image_user_reply_item);
        tvNickname = (TextView) itemView.findViewById(R.id.text_nickname_reply_item);
        tvContent = (TextView) itemView.findViewById(R.id.text_content_reply_item);
        tvTime = (TextView) itemView.findViewById(R.id.text_time_reply_item);
        tvDelete = (TextView) itemView.findViewById(R.id.text_delete_reply_item);
    }

    public void setReplyItemView(final CommentUserVO vo, final Context context) {
//        ImageLoader.getInstance().displayImage(NetworkConstant.HTTP_URL + vo.getUser().getProfileImage(), ivPicture);
        Picasso.with(context)
                .load(NetworkConstant.HTTP_URL + vo.getUser().getProfileImage())
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(ivPicture);

        tvNickname.setText(vo.getUser().getNickname());
        tvContent.setText(vo.getContent());
        tvTime.setText(vo.getRegisterDate());
        if (drawDeleteButton(vo.getUser().get_id())) {
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ReplyActivity) context).removeReply(vo.get_id());
                }
            });
        }
    }

    private boolean drawDeleteButton(String id) {
        if(id.equals(MyData.getInstance().get_id())) {
            tvDelete.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }


}
