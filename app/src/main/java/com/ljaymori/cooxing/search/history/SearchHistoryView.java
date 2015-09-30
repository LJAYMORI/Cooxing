package com.ljaymori.cooxing.search.history;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;

public class SearchHistoryView extends RecyclerView.ViewHolder {

    private TextView tvContent;
    private TextView tvRemove;

    public SearchHistoryView(View itemView) {
        super(itemView);

        tvContent = (TextView) itemView.findViewById(R.id.text_search_history);
        tvRemove = (TextView) itemView.findViewById(R.id.text_remove_search_history);

        setFonts();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvContent);
    }

    public void setHistoryView(final String content) {
        tvContent.setText(content);
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(historyListener != null) {
                    historyListener.onHistoryClick(content);
                }
            }
        });
        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(removeListener != null) {
                    removeListener.onRemoveClick(content);
                }
            }
        });
    }

    public interface OnHistoryRemoveClickListener {
        void onRemoveClick(String content);
    }
    private OnHistoryRemoveClickListener removeListener;
    public void setOnHistoryRemoveClickListener(OnHistoryRemoveClickListener listener) {
        removeListener = listener;
    }

    public interface OnHistoryClickListener {
        void onHistoryClick(String keyword);
    }
    private OnHistoryClickListener historyListener;
    public void setOnHistoryClickListener(OnHistoryClickListener listener) {
        historyListener = listener;
    }
}
