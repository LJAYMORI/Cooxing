package com.ljaymori.cooxing.reply;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.vo.CommentUserVO;

import java.util.ArrayList;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyItemView> {

    private Context mContext;
    private ArrayList<CommentUserVO> items = new ArrayList<CommentUserVO>();

    public ReplyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void init(ArrayList<CommentUserVO> list, int pagePosition) {
        items.clear();

        ArrayList<CommentUserVO> comments = new ArrayList<CommentUserVO>();
        for (CommentUserVO vo : list) {
            if (pagePosition == 0 || vo.getPosition() == pagePosition) {
                comments.add(vo);
            }
        }

        addAll(comments);
    }

    public void addAll(ArrayList<CommentUserVO> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ReplyItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_reply, parent, false);

        return new ReplyItemView(v);
    }

    @Override
    public void onBindViewHolder(ReplyItemView holder, int position) {
        holder.setReplyItemView(items.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
