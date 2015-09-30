package com.ljaymori.cooxing.notify;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljaymori.cooxing.R;

public class NotifyItemView extends RecyclerView.ViewHolder {

    private ImageView ivPicture;
    private TextView tvContent;

    public NotifyItemView(View itemView) {
        super(itemView);

        ivPicture = (ImageView) itemView.findViewById(R.id.image_user_notify);
        tvContent = (TextView) itemView.findViewById(R.id.text_content_notify);
    }

    public void setNotifyItemView(NotifyItemData nd) {
//        ivPicture.setImageResource(Integer.parseInt(nd.getImageURL()));
//        ImageLoader.getInstance().displayImage(NetworkConstant.HTTP_URL + nd.getImageURL(), ivPicture);
        tvContent.setText(nd.getType()+"");
    }
}
