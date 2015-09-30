package com.ljaymori.cooxing.write.descriptsion;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ljaymori.cooxing.R;

import java.io.File;

public class DescItemView extends DescItemParentView implements View.OnClickListener, View.OnLongClickListener {

    private View viewItem;

    private ImageView ivDesc;
    private EditText etDesc;
    private TextView tvRemove;
    private View viewBlur;

    public DescItemView(View itemView) {
        super(itemView);

        viewItem = itemView.findViewById(R.id.view_description_write);

        ivDesc = (ImageView) itemView.findViewById(R.id.image_description_write_item);
        etDesc = (EditText) itemView.findViewById(R.id.edit_description_write_item);
        tvRemove = (TextView) itemView.findViewById(R.id.text_remove_description_write_item);
        viewBlur = itemView.findViewById(R.id.view_blur_description_write_item);
    }

    public void setDescItemView(DescItemData dd, Context context) {
        if (!drawBlur(dd.getType(), dd.getCount())) {
            Glide.with(context)
                    .load(new File(dd.getFilePath()))
                    .placeholder(R.drawable.image_placeholder)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivDesc);

            etDesc.setText(dd.getDescription());
            etDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (focusListener != null) {
                        focusListener.onFocused(hasFocus, v.getId());
                    }
                }
            });
            etDesc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (textListener != null) {
                        textListener.onChanged(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            viewItem.setOnLongClickListener(this);
            tvRemove.setOnClickListener(this);
        }
    }

    private boolean drawBlur(int type, int count) {
        if (type == DescItemAdapter.TYPE_ITEM) {
            return false;
        } else {
            if(count < 10) {
                if (viewItem.getVisibility() == View.INVISIBLE) {
                    viewItem.setVisibility(View.VISIBLE);
                }
                viewBlur.setVisibility(View.VISIBLE);
                viewBlur.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addListener != null) {
                            addListener.onDescAddClick();
                        }
                    }
                });
                return true;

            } else {
                if (viewItem.getVisibility() == View.VISIBLE) {
                    viewItem.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        }
    }

    public void addTag(int id, String tag) {
        if (id == etDesc.getId()) {
            String str = etDesc.getText().toString();
            int position = etDesc.getSelectionStart();

            if (str.length() > 0 && str.charAt(position - 1) != ' ') {
                str = str.substring(0, position) + " " + tag + " " + str.substring(position, str.length());
                etDesc.setText(str);
                etDesc.setSelection(position + tag.length() + 2);

            } else {
                str = str.substring(0, position) + tag + " " + str.substring(position, str.length());
                etDesc.setText(str);
                etDesc.setSelection(position + tag.length() + 1);

            }
        }
    }

    public void addSharp(int id) {
        if (id == etDesc.getId()) {
            int position = etDesc.getSelectionStart();

            String str = etDesc.getText().toString();
            str = str.substring(0, position) + "#" + str.substring(position, str.length());

            etDesc.setText(str);
            etDesc.setSelection(position + 1);
        }
    }

    public String getDescription() {
        return etDesc.getText().toString();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_remove_description_write_item : {
                if(removeLister != null) {
                    removeLister.onRemoveClick();
                }
                break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.view_description_write) {
            if(longClickListener != null) {
                showRemoveButton();
                longClickListener.onItemLongClick();
            }
            return true;
        }
        return false;
    }

    private void showRemoveButton() {
        if(tvRemove.getVisibility() == View.GONE) {
            tvRemove.setVisibility(View.VISIBLE);
        }
    }

    private void hideRemoveButton() {
        if(tvRemove.getVisibility() == View.VISIBLE) {
            tvRemove.setVisibility(View.GONE);
        }
    }


    public interface OnDescAddItemClickListener {
        void onDescAddClick();
    }
    private OnDescAddItemClickListener addListener;
    public void setOnDescAddItemClickListener(OnDescAddItemClickListener listener) {
        addListener = listener;
    }


    public interface OnItemFocusChangeListener {
        void onFocused(boolean check, int id);
    }
    private OnItemFocusChangeListener focusListener;
    public void setOnItemFocusChangListener(OnItemFocusChangeListener listener) {
        focusListener = listener;
    }


    public interface OnItemTextChangeListener {
        void onChanged(String s);
    }
    private OnItemTextChangeListener textListener;
    public void setOnItemTextChangeListener(OnItemTextChangeListener listener) {
        textListener = listener;
    }


    public interface OnItemRemoveClickListener {
        void onRemoveClick();
    }
    private OnItemRemoveClickListener removeLister;
    public void setOnItemRemoveClickListener(OnItemRemoveClickListener listener) {
        removeLister = listener;
    }


    public interface OnItemLongClickListener {
        void onItemLongClick();
    }
    private OnItemLongClickListener longClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        longClickListener = listener;
    }
}
