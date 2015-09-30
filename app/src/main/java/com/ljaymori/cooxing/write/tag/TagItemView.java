package com.ljaymori.cooxing.write.tag;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;

public class TagItemView extends RecyclerView.ViewHolder {

    private TextView tvTag;

    public TagItemView(View itemView) {
        super(itemView);

        tvTag = (TextView) itemView.findViewById(R.id.text_tag_write_item);
        setFonts();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvTag);
    }

    public void setTagItemView(TagItemData td) {
        tvTag.setText(td.getName());
        tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onTagItemClick(tvTag.getText().toString());
                }
            }
        });
    }

    public interface OnTagItemViewClickListener {
        void onTagItemClick(String tag);
    }

    private OnTagItemViewClickListener clickListener;

    public void setOnTagItemViewClickListener(OnTagItemViewClickListener listener) {
        clickListener = listener;
    }
}
