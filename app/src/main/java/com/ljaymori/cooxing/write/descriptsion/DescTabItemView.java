package com.ljaymori.cooxing.write.descriptsion;

import android.view.View;
import android.widget.TextView;

import com.ljaymori.cooxing.R;

public class DescTabItemView extends DescItemParentView implements View.OnClickListener {

    private TextView tvRemoveAll;
    private TextView tvEditOrder;

    public DescTabItemView(View itemView) {
        super(itemView);

        tvRemoveAll = (TextView) itemView.findViewById(R.id.text_remove_all_description_option_write_item);
        tvEditOrder = (TextView) itemView.findViewById(R.id.text_edit_order_description_option_write_item);
    }

    public void setDescTabView(DescItemData id) {
        if(id.isActivated()) {
            activate();
        } else {
            unactivate();
        }

        tvRemoveAll.setOnClickListener(this);
        tvEditOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(tabListener != null) {
            switch (v.getId()) {
                case R.id.text_remove_all_description_option_write_item: {
                    tabListener.onRemoveAllClick();
                    break;
                }
                case R.id.text_edit_order_description_option_write_item: {
                    tabListener.onEditOrderClick();
                    break;
                }
            }
        }
    }

    private void activate() {
        tvRemoveAll.setBackgroundResource(R.drawable.btn_activated_remove_all);
        tvEditOrder.setBackgroundResource(R.drawable.btn_activated_shuffle);
    }

    private void unactivate() {
        tvRemoveAll.setBackgroundResource(R.drawable.btn_unactivated_remove_all);
        tvEditOrder.setBackgroundResource(R.drawable.btn_unactivated_shuffle);
    }

    public interface OnDescTabItemClickListener {
        void onRemoveAllClick();
        void onEditOrderClick();
    }
    private OnDescTabItemClickListener tabListener;
    public void setOnDescTabItemClickListener(OnDescTabItemClickListener listener) {
        tabListener = listener;
    }
}
