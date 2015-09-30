package com.ljaymori.cooxing.interest;

import android.view.View;
import android.widget.TextView;

import com.ljaymori.cooxing.R;

public class InterestHeaderView extends InterestParentView {

    private TextView tvHeaderName;

    public InterestHeaderView(View itemView) {
        super(itemView);

        tvHeaderName = (TextView) itemView.findViewById(R.id.text_header_interest);
    }

    public void setHeaderName(String name) {
        tvHeaderName.setText(name);
    }
}
