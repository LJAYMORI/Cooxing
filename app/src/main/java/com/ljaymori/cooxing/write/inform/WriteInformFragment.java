package com.ljaymori.cooxing.write.inform;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.write.WriteActivity;
import com.ljaymori.cooxing.write.WriteParentFragment;
import com.ljaymori.cooxing.write.tag.TagItemData;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class WriteInformFragment extends WriteParentFragment implements View.OnFocusChangeListener {

    private ArrayList<TagItemData> tagListInInform = new ArrayList<TagItemData>();

    private TextView tvRecipeName;
    private TextView tvRecipeDesc;

    private EditText etRecipeName;
    private EditText etRecipeDesc;

    private boolean isChangeName = false;
    private boolean isChangeDesc = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write_inform, container, false);

        tvRecipeName = (TextView) v.findViewById(R.id.text_recipe_name_write_page_inform);
        tvRecipeDesc = (TextView) v.findViewById(R.id.text_recipe_desc_write_page_inform);

        etRecipeName = (EditText) v.findViewById(R.id.edit_recipe_name_write_page_inform);
        etRecipeDesc = (EditText) v.findViewById(R.id.edit_recipe_desc_write_page_inform);

        etRecipeName.setOnFocusChangeListener(this);
        etRecipeDesc.setOnFocusChangeListener(this);

        etRecipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChangeName = true;
                ((WriteActivity) getActivity()).setRecipeTitle(s.toString());
                checkTags();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etRecipeDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChangeDesc = true;
                ((WriteActivity) getActivity()).setRecipeDescription(s.toString());
                checkTags();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        HighlightHashtag.drawHashtag(etRecipeName);
//        HighlightHashtag.drawHashtag(etRecipeDesc);

        setFonts();

        return v;
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvRecipeName, tvRecipeDesc);
        FontUtils.setEditTextFonts(FontUtils.TYPE_BARUN_NOMAL, etRecipeName, etRecipeDesc);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        if (hasFocus) {
            ((WriteActivity) getActivity()).setFocusedEditText(id);
            ((WriteActivity) getActivity()).showKeyboard();
        } else {
            if (isChangeName || isChangeDesc) {
//                checkTags();
            }

            if (id == R.id.edit_recipe_name_write_page_inform) {
                isChangeName = false;
            } else if (id == R.id.edit_recipe_desc_write_page_inform) {
                isChangeDesc = false;
            }
        }
    }

    private void checkTags() {
        String nameStr = etRecipeName.getText().toString();
        String descStr = etRecipeDesc.getText().toString();

        StringTokenizer tokenizer = new StringTokenizer(nameStr + " " + descStr);

        ArrayList<TagItemData> tags = new ArrayList<TagItemData>();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (Pattern.matches(CooxingConstant.HASHTAG_PATTERN, token.trim())) {
                TagItemData td = new TagItemData();
                td.setName(token);

                tags.add(td);
            }
        }

        initTagList(tags);

        ((WriteActivity) getActivity()).checkTag();
    }

    public void addTag(String tag, int id) {
        if (etRecipeDesc.getId() == id) {
            String str = etRecipeDesc.getText().toString();
            int position = etRecipeDesc.getSelectionStart();
            if (str.length() > 0 && str.charAt(position - 1) != ' ') {
                str = str.substring(0, position) + " " + tag + " " + str.substring(position, str.length());
                etRecipeDesc.setText(str);
                etRecipeDesc.setSelection(position + tag.length() + 2);

            } else {
                str = str.substring(0, position) + tag + " " + str.substring(position, str.length());
                etRecipeDesc.setText(str);
                etRecipeDesc.setSelection(position + tag.length() + 1);

            }


        } else if (etRecipeName.getId() == id) {
            String str = etRecipeName.getText().toString();
            int position = etRecipeName.getSelectionStart();
            if (str.length() > 0 && str.charAt(position - 1) != ' ') {
                str = str.substring(0, position) + " " + tag + " " + str.substring(position, str.length());
                etRecipeName.setText(str);
                etRecipeName.setSelection(position + tag.length() + 2);

            } else {
                str = str.substring(0, position) + tag + " " + str.substring(position, str.length());
                etRecipeName.setText(str);
                etRecipeName.setSelection(position + tag.length() + 1);

            }
        }
    }

    public void addSharp(int id) {
        EditText et;
        int position;

        if (id == R.id.edit_recipe_name_write_page_inform) {
            et = etRecipeName;
            position = etRecipeName.getSelectionStart();

        } else if (id == R.id.edit_recipe_desc_write_page_inform) {
            et = etRecipeDesc;
            position = etRecipeDesc.getSelectionStart();

        } else {
            return;
        }

        String str = et.getText().toString();
        str = str.substring(0, position) + "#" + str.substring(position, str.length());

        et.setText(str);
        et.setSelection(position + 1);
    }


    private void initTagList(ArrayList<TagItemData> list) {
        tagListInInform.clear();
        tagListInInform.addAll(list);
    }

    public ArrayList<TagItemData> getTagListInInform() {
        return tagListInInform;
    }


}
