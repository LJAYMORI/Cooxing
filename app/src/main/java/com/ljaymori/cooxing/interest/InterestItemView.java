package com.ljaymori.cooxing.interest;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;

public class InterestItemView extends InterestParentView implements Checkable, View.OnClickListener {

    private InterestItemData itemData;

    private View viewItem;

    private ImageView ivIcon;
    private TextView tvCheck;

    public InterestItemView(View itemView) {
        super(itemView);

        viewItem = itemView.findViewById(R.id.view_item_interest);
        ivIcon = (ImageView) itemView.findViewById(R.id.image_item_background_interest);
        tvCheck = (TextView) itemView.findViewById(R.id.text_item_check_interest);

        viewItem.setOnClickListener(this);
    }

    public void setItemView(InterestItemData id, Context context) {
        itemData = id;

        Glide.with(context)
                .load(NetworkConstant.HTTP_URL + id.getImage())
                .placeholder(R.drawable.image_placeholder)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivIcon);

        setCheckedView(id.isChecked());
    }

    public void setCheckedView(boolean check) {
        if (check) {
            tvCheck.setVisibility(View.VISIBLE);
        } else {
            tvCheck.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        itemData.setChecked(checked);

        setCheckedView(checked);

        if (mListener != null) {
            mListener.onChecked(checked);
        }
    }

    @Override
    public boolean isChecked() {
        return itemData.isChecked();
    }

    @Override
    public void toggle() {
        itemData.setChecked(!itemData.isChecked());

        setCheckedView(itemData.isChecked());

        if (mListener != null) {
            mListener.onChecked(itemData.isChecked());
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.view_item_interest) {
            toggle();
        }
    }

    public interface OnInterestItemCheckChangeListener {
        void onChecked(boolean checked);
    }

    OnInterestItemCheckChangeListener mListener;

    public void setOnInterestCheckChangeListener(OnInterestItemCheckChangeListener listener) {
        mListener = listener;
    }
}
