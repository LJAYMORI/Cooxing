package com.ljaymori.cooxing.write.ingredient;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.write.WriteActivity;
import com.ljaymori.cooxing.write.WriteParentFragment;
import com.ljaymori.cooxing.write.tag.TagItemData;

import java.util.ArrayList;

public class WriteIngredientFragment extends WriteParentFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private ArrayList<TagItemData> tagListInIngredient = new ArrayList<TagItemData>();

    private View viewTutorial;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private IngredientItemAdapter ingredientAdapter;

    private EditText etIngredient;
    private TextView tvAdd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write_ingredient, container, false);

        viewTutorial = v.findViewById(R.id.view_tutorial_write_page_ingredient);


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_write_page_ingredient);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        ingredientAdapter = new IngredientItemAdapter(getActivity());
        ingredientAdapter.addAll(new ArrayList<IngredientItemData>());

        recyclerView.setAdapter(ingredientAdapter);


        etIngredient = (EditText) v.findViewById(R.id.edit_write_page_ingredient);
        etIngredient.setOnFocusChangeListener(this);
        etIngredient.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClick(tvAdd);
                    return true;
                }
                return false;
            }
        });
        tvAdd = (TextView) v.findViewById(R.id.text_add_write_page_ingredient);
        tvAdd.setOnClickListener(this);

        setFonts();

        return v;
    }

    private void setFonts() {
        FontUtils.setEditTextFonts(FontUtils.TYPE_BARUN_BOLD, etIngredient);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /* 메뉴 추가 눌렀을 때 */
            case R.id.text_add_write_page_ingredient: {
                if (ingredientAdapter.getItemCount() <= 20) {
                    String menu = etIngredient.getText().toString().replace(' ', '_');
                    if (menu.length() > 0) {

                        IngredientItemData id = new IngredientItemData();
                        id.setName(menu);

                        if (!ingredientAdapter.add(id)) {
                            etIngredient.setText("");
                            return;
                        }

                        showingTutorial(false);

                        initTagList();
                        ((WriteActivity) getActivity()).checkTag();
                        ((WriteActivity) getActivity()).getIngredients().add(menu);

                        etIngredient.setText("");

                        recyclerView.smoothScrollToPosition(ingredientAdapter.getItemCount() - 1);
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.write_message_add_menu_limit), Toast.LENGTH_SHORT).show();
                }

                break;
            }
        }
    }

    public void addTag(String tag) {
        etIngredient.setText(tag.substring(1, tag.length()));
        etIngredient.setSelection(etIngredient.getText().length());
    }

    public void showingTutorial(boolean check) {
        if (check) {
            if (viewTutorial.getVisibility() == View.GONE) {
                viewTutorial.setVisibility(View.VISIBLE);
            }

        } else {
            if (viewTutorial.getVisibility() == View.VISIBLE) {
                viewTutorial.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        if (hasFocus) {
            ((WriteActivity) getActivity()).setFocusedEditText(id);
        }
    }

    public void initTagList() {
        tagListInIngredient.clear();
        tagListInIngredient.addAll(ingredientAdapter.getItems());
    }

    public ArrayList<TagItemData> getTagListInIngredient() {
        return tagListInIngredient;
    }
}
