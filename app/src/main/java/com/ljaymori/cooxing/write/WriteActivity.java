package com.ljaymori.cooxing.write;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.NetworkResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.dialog.LoadingDialogFragment;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.common.vo.IngredientVO;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.write.descriptsion.WriteDescFragment;
import com.ljaymori.cooxing.write.inform.WriteInformFragment;
import com.ljaymori.cooxing.write.ingredient.WriteIngredientFragment;
import com.ljaymori.cooxing.write.tag.TagItemAdapter;
import com.ljaymori.cooxing.write.tag.TagItemData;

import java.util.ArrayList;

public class WriteActivity extends ActionBarActivity implements View.OnClickListener {

    public static final int WRITE_REQUEST_CODE = 874;

    private WriteFocus focus;

    private View viewSharp;
    private TextView tvSharp;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TagItemAdapter tagAdapter;


    private ViewPager viewPager;
    private WriteAdapter pagerAdapter;

    private TextView tvStrip;

    private TextView tvTabInform;
    private TextView tvTabIngredient;
    private TextView tvTabDesc;

    private WriteInformFragment informFragment;
    private WriteIngredientFragment ingredientFragment;
    private WriteDescFragment descFragment;

    private int tabWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_left_arrow);

        focus = new WriteFocus();

        initTab();

        initTag();

        viewPager = (ViewPager) findViewById(R.id.pager_write);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvStrip.setTranslationX((position + positionOffset) * tabWidth);
            }

            @Override
            public void onPageSelected(int position) {
                focus.setCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        pagerAdapter = new WriteAdapter(getSupportFragmentManager());
        pagerAdapter.initPages(initPages());

        viewPager.setAdapter(pagerAdapter);

        setFonts();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvTabInform, tvTabIngredient, tvTabDesc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;

        } else if (id == R.id.action_post_write) {
            submit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        final LoadingDialogFragment dialog = new LoadingDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), LoadingDialogFragment.DIALOG_LOADING);

        final RecipeVO recipe = new RecipeVO();
        recipe.setTitle(recipeTitle);
        recipe.setDescription(recipeDescription);
        recipe.setIngredients(getConvertedIngredients());
        recipe.setImages(imageList);
        recipe.setSteps(steps);
        recipe.setHashtags(getHashtags());

        if (checkRecipe(recipe)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    NetworkManager.getInstance().registerRecipe(WriteActivity.this, recipe, new NetworkManager.OnResultListener<NetworkResult>() {
                        @Override
                        public void onSuccess(NetworkResult data) {
                            if (data.result.equals(CooxingConstant.SUCCESS)) {
                                Toast.makeText(WriteActivity.this, getString(R.string.write_message_post_success), Toast.LENGTH_SHORT).show();
                                setResult(Activity.RESULT_OK);
                                finish();

                            } else if (data.result.equals(CooxingConstant.FAIL)) {
                                Toast.makeText(WriteActivity.this, data.message, Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(WriteActivity.this, code + "", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }, 200);

        }
    }

    private ArrayList<WriteParentFragment> initPages() {

        informFragment = new WriteInformFragment();
        ingredientFragment = new WriteIngredientFragment();
        descFragment = new WriteDescFragment();

        ArrayList<WriteParentFragment> list = new ArrayList<WriteParentFragment>();

        list.add(informFragment);
        list.add(ingredientFragment);
        list.add(descFragment);

        return list;
    }

    private void initTab() {
        tvTabInform = (TextView) findViewById(R.id.text_tab_inform_write);
        tvTabIngredient = (TextView) findViewById(R.id.text_tab_ingredient_write);
        tvTabDesc = (TextView) findViewById(R.id.text_tab_description_write);

        tvTabInform.setOnClickListener(this);
        tvTabIngredient.setOnClickListener(this);
        tvTabDesc.setOnClickListener(this);

        Display dis = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        dis.getSize(size);
        tabWidth = (int) (size.x - (getResources().getDimension(R.dimen.write_tab_left_right_margin) * 2)) / 3;

        tvStrip = (TextView) findViewById(R.id.strip_write);
        tvStrip.setLayoutParams(new LinearLayout.LayoutParams(tabWidth, (int) getResources().getDimension(R.dimen.write_tab_strip_height)));
    }

    private void initTag() {
        viewSharp = findViewById(R.id.view_sharp_write);
        tvSharp = (TextView) findViewById(R.id.text_sharp_write);
        tvSharp.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_write_sharp);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        tagAdapter = new TagItemAdapter(this);
        tagAdapter.addAll(initTags());

        recyclerView.setAdapter(tagAdapter);

    }

    private ArrayList<TagItemData> initTags() {
        ArrayList<TagItemData> list = new ArrayList<TagItemData>();

        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_tab_inform_write: {
                viewPager.setCurrentItem(0, true);
                break;
            }
            case R.id.text_tab_ingredient_write: {
                viewPager.setCurrentItem(1, true);
                break;
            }
            case R.id.text_tab_description_write: {
                viewPager.setCurrentItem(2, true);
                break;
            }
            case R.id.text_sharp_write: {
                addSharp(focus);
                break;
            }
        }
    }

    public WriteIngredientFragment getIngredientFragment() {
        return ingredientFragment;
    }

    public void setFocusedEditText(int id) {
        focus.setFocusedEditText(id);
    }

    public void setFocusedPosition(int position) {
        focus.setPositionOfRecyclerView(position);
    }

    public void addSharp(WriteFocus f) {
        switch (f.getCurrentPosition()) {
            case 0: {
                informFragment.addSharp(f.getFocusedEditText());
                break;
            }
            case 1: {
                Toast.makeText(this, getResources().getString(R.string.write_message_add_menu_hashtag), Toast.LENGTH_SHORT).show();
                break;
            }
            case 2: {
                descFragment.addSharp(f.getFocusedEditText(), f.getPositionOfRecyclerView());
                break;
            }
        }
    }

    private boolean isShowingKeyboard = false;

    public void showKeyboard() {
        viewSharp.setVisibility(View.VISIBLE);
        isShowingKeyboard = true;
    }

    public void hideKeyboard() {
        viewSharp.setVisibility(View.INVISIBLE);
        isShowingKeyboard = false;
    }

    private void addTag(TagItemData td) {
        int size = tagAdapter.getItemCount();
        for (int position = 0; position < size; position++) {
            String keyword = tagAdapter.getItems().get(position).getName();

            int compareCase = keyword.compareToIgnoreCase(td.getName());
            if (compareCase == 0) {
                return;
            } else if (compareCase > 0) {
                tagAdapter.add(position, td);
                return;
            }
        }

        tagAdapter.add(td);
    }

    public void clickTag(String tag) {
        switch (focus.getCurrentPosition()) {
            case 0: {
                informFragment.addTag(tag, focus.getFocusedEditText());
                break;
            }
            case 1: {
                ingredientFragment.addTag(tag);
                break;
            }
            case 2: {
                descFragment.addTag(tag, focus.getFocusedEditText(), focus.getPositionOfRecyclerView());
                break;
            }
        }
    }

    public void checkTag() {
        ArrayList<TagItemData> tagAdapterList = new ArrayList<TagItemData>();
        tagAdapterList.addAll(tagAdapter.getItems());

        ArrayList<TagItemData> tagFragmentList = new ArrayList<TagItemData>();
        tagFragmentList.addAll(informFragment.getTagListInInform());
        tagFragmentList.addAll(ingredientFragment.getTagListInIngredient());
        tagFragmentList.addAll(descFragment.getTagListInDescription());

        ArrayList<TagItemData> intersectionList = new ArrayList<TagItemData>();

        for (TagItemData td : tagFragmentList) {
            if (tagAdapterList.contains(td)) {
                intersectionList.add(td);
            }
        }

        for (TagItemData td : intersectionList) {
            tagAdapterList.remove(td);
            tagFragmentList.remove(td);
        }

        int adapterSize = tagAdapterList.size();
        for (int i = 0; i < adapterSize; i++) {
            TagItemData td = tagAdapterList.get(i);
            tagAdapter.remove(td);
        }

        int tagsSize = tagFragmentList.size();
        for (int i = 0; i < tagsSize; i++) {
            TagItemData td = tagFragmentList.get(i);
            addTag(td);
        }

    }


    private boolean checkRecipe(RecipeVO recipe) {
        return true;
    }

    private ArrayList<IngredientVO> getConvertedIngredients() {
        ArrayList<IngredientVO> list = new ArrayList<IngredientVO>();

        int size = ingredients.size();
        for (int i = 0; i < size; i++) {
            IngredientVO vo = new IngredientVO();
            vo.setName(ingredients.get(i));

            list.add(vo);
        }

        return list;
    }


    private String recipeTitle = new String("");
    private String recipeDescription = new String("");
    private ArrayList<String> ingredients = new ArrayList<String>();
    private ArrayList<String> imageList = new ArrayList<String>();
    private ArrayList<String> steps = new ArrayList<String>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == WriteDescFragment.REQUEST_CODE) {
                ArrayList<String> list = (ArrayList<String>) data.getSerializableExtra(WriteDescFragment.FILE_PATHS);
                if( list != null) {
                    imageList.addAll(list);
                    for (String str : list) {
                        steps.add(new String(""));
                    }

                    descFragment.addFiles(imageList, steps);
                }
            }
        }
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public ArrayList<String> getHashtags() {
        ArrayList<String> list = new ArrayList<String>();
        for (TagItemData td : tagAdapter.getItems()) {
            list.add(td.getName());
        }

        return list;
    }

    public WriteDescFragment getDescFragment() {
        return descFragment;
    }

}
