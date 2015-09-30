package com.ljaymori.cooxing.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.RecipeResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.dialog.LoadingDialogFragment;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.common.vo.CommentVO;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.detail.dots.DotAdapter;
import com.ljaymori.cooxing.detail.fragment.DetailMainFragment;
import com.ljaymori.cooxing.detail.fragment.DetailParentFragment;
import com.ljaymori.cooxing.detail.fragment.DetailStepFragment;
import com.ljaymori.cooxing.reply.ReplyActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DetailActivity extends ActionBarActivity implements View.OnClickListener {

    public static final int DETAIL_REQUEST_CODE = 212;

    public static final String RECIPE_VO = "recipe_vo";
    public static final String PAGE_POSITION = "page_position";

    public static final String RECIPE_ID = "recipe_id";

    public static final int STEP_IMAGES[] = new int[] {
            R.drawable.ic_step_1,
            R.drawable.ic_step_2,
            R.drawable.ic_step_3,
            R.drawable.ic_step_4,
            R.drawable.ic_step_5,
            R.drawable.ic_step_6,
            R.drawable.ic_step_7,
            R.drawable.ic_step_8,
            R.drawable.ic_step_9,
            R.drawable.ic_step_10,
    };

    private View viewLike;
    private TextView tvLikeCount;

    private View viewComment;
    private TextView tvCommentsCount;

    private ViewPager viewPager;
    private DetailPagerAdapter mAdapter;


    private RecipeVO recipeVO;
    private int pagePosition = 0;
    private HashMap<Integer, ArrayList<CommentVO>> commentMap = new HashMap<Integer, ArrayList<CommentVO>>();


    private int cursorWidth;
    private View viewCursor;
    private TextView tvCursorMain;
    private TextView tvCursorStep;

    private RecyclerView dotsRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DotAdapter dotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_left_arrow);

        if(savedInstanceState != null) {
            recipeVO = (RecipeVO) savedInstanceState.getSerializable(RECIPE_VO);
        }

        String recipe_id = getIntent().getStringExtra(RECIPE_ID);
        if (recipe_id == null) {
            Toast.makeText(this, getString(R.string.detail_message_no_extra), Toast.LENGTH_SHORT).show();
            finish();

        } else if(recipeVO == null){
            viewLike = findViewById(R.id.view_like_detail_main);
            viewComment = findViewById(R.id.view_comments_detail_main);

            tvLikeCount = (TextView) findViewById(R.id.text_like_count_detail_main);
            tvCommentsCount = (TextView) findViewById(R.id.text_reply_count_detail_main);

            viewPager = (ViewPager) findViewById(R.id.pager_detail);
            mAdapter = new DetailPagerAdapter(getSupportFragmentManager());

            viewLike.setOnClickListener(DetailActivity.this);
            viewComment.setOnClickListener(DetailActivity.this);


            cursorWidth = (int) getResources().getDimension(R.dimen.detail_dots_cover_width);

            viewCursor = findViewById(R.id.view_dot_detail);
            tvCursorMain = (TextView) findViewById(R.id.text_dots_main_detail);
            tvCursorStep = (TextView) findViewById(R.id.text_dots_step_detail);

            dotsRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_dot_detail);

            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            dotsRecyclerView.setLayoutManager(linearLayoutManager);

            dotAdapter = new DotAdapter(this);
            dotsRecyclerView.setAdapter(dotAdapter);

            setFonts();
            initRecipe(recipe_id);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RECIPE_VO, recipeVO);
    }

    private void setActionBarTitle(String title) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void initRecipe(String recipe_id) {
        final LoadingDialogFragment dialog = new LoadingDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), LoadingDialogFragment.DIALOG_LOADING);

        NetworkManager.getInstance().getRecipe(this, recipe_id, new NetworkManager.OnResultListener<RecipeResult>() {
            @Override
            public void onSuccess(RecipeResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    recipeVO = data.data;

                    setActionBarTitle(recipeVO.getTitle());

                    initCommentMap(recipeVO.getComments());

                    tvLikeCount.setText(getCountToString(recipeVO.getBookmarkers().size()));
                    drawCommentCount(0);

                    mAdapter.initPages(initPages());
                    viewPager.setAdapter(mAdapter);
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            viewCursor.setTranslationX((position + positionOffset) * cursorWidth);
                            if(position == 0) {
                                tvCursorMain.setAlpha(1 - positionOffset);
                                tvCursorStep.setAlpha(positionOffset);
                            }
                        }

                        @Override
                        public void onPageSelected(int position) {
                            pagePosition = position;
                            drawCommentCount(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });

                    drawDots(mAdapter.getCount());


                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(DetailActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    finish();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 1000);

            }

            @Override
            public void onFail(int code) {
                Toast.makeText(DetailActivity.this, code + "", Toast.LENGTH_SHORT).show();
                finish();
                dialog.dismiss();
            }
        });
    }

    private void setFonts() {
        TextView tvLike = (TextView) findViewById(R.id.text_like_detail_main);
        TextView tvComments = (TextView) findViewById(R.id.text_reply_detail_main);

        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvLike, tvComments);
        FontUtils.setTextViewFonts(FontUtils.TYPE_ROBOTO_NOMAL, tvLikeCount, tvCommentsCount);
    }

    private void drawDots(int size) {
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(new String[size]));
        dotAdapter.addAll(list);
    }

    @Override
    public void finish() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(RECIPE_VO, recipeVO);

        setResult(Activity.RESULT_OK, data);

        finish();
    }

    private void initCommentMap(ArrayList<CommentVO> comments) {
        commentMap.clear();

        for (CommentVO vo : comments) {
            int key = vo.getPosition();
            ArrayList<CommentVO> value = commentMap.remove(key);
            if (value == null) {
                value = new ArrayList<CommentVO>();
            }

            value.add(vo);

            commentMap.put(key, value);
        }
    }

    private ArrayList<DetailParentFragment> initPages() {
        ArrayList<DetailParentFragment> list = new ArrayList<DetailParentFragment>();

        DetailMainFragment mainFragment = new DetailMainFragment();
        Bundle mainArgs = new Bundle();
        mainArgs.putSerializable(RECIPE_VO, recipeVO);
        mainFragment.setArguments(mainArgs);

        list.add(mainFragment);

        int size = recipeVO.getImages().size();
        for (int i = 0; i < size; i++) {
            DetailStepFragment stepFragment = new DetailStepFragment();
            Bundle args = new Bundle();
            args.putInt(PAGE_POSITION, i);
            stepFragment.setArguments(args);

            list.add(stepFragment);
        }

        return list;
    }

    private void drawCommentCount(int position) {
        if (position == 0) {
            ArrayList<Integer> keys = new ArrayList<Integer>(commentMap.keySet());
            int size = keys.size();
            int count = 0;
            for (int i = 0; i < size; i++) {
                ArrayList<CommentVO> list = commentMap.get(keys.get(i));
                count += list.size();
            }

            tvCommentsCount.setText(getCountToString(count));

        } else {
            ArrayList<CommentVO> list = commentMap.get(position);
            if (list == null) {
                list = new ArrayList<CommentVO>();
                commentMap.put(position, list);
            }

            tvCommentsCount.setText(getCountToString(list.size()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
            Intent data = new Intent();
            data.putExtra(RECIPE_VO, recipeVO);

            setResult(Activity.RESULT_OK, data);

            finish();
            return true;

        } else if (id == R.id.action_share_detail) {
            Toast.makeText(DetailActivity.this, "share", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_like_detail_main: {
                if (MyData.getInstance().getBookmarks().contains(recipeVO.get_id())) {
                    unlike();
                } else {
                    like();
                }
                break;
            }
            case R.id.view_comments_detail_main: {
                Intent intent = new Intent(this, ReplyActivity.class);
                intent.putExtra(RECIPE_VO, recipeVO);
                intent.putExtra(PAGE_POSITION, pagePosition);
                startActivityForResult(intent, ReplyActivity.REPLY_REQUEST_CODE);

                break;
            }
        }
    }

    private void unlike() {
        NetworkManager.getInstance().unlikeRecipe(this, recipeVO.get_id(), new NetworkManager.OnResultListener<RecipeResult>() {
            @Override
            public void onSuccess(RecipeResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getBookmarks().remove(recipeVO.get_id());
                    recipeVO = data.data;

                    initCommentMap(recipeVO.getComments());
                    tvLikeCount.setText(getCountToString(recipeVO.getBookmarkers().size()));
                    drawCommentCount(pagePosition);

                    DetailParentFragment f = mAdapter.getItems().get(pagePosition);
                    f.initRecipeVO();

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(DetailActivity.this, data.message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(DetailActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void like() {
        NetworkManager.getInstance().likeRecipe(this, recipeVO.get_id(), new NetworkManager.OnResultListener<RecipeResult>() {
            @Override
            public void onSuccess(RecipeResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getBookmarks().add(recipeVO.get_id());
                    recipeVO = data.data;

                    initCommentMap(recipeVO.getComments());
                    tvLikeCount.setText(getCountToString(recipeVO.getBookmarkers().size()));
                    drawCommentCount(pagePosition);

                    DetailParentFragment f = mAdapter.getItems().get(pagePosition);
                    f.initRecipeVO();

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(DetailActivity.this, data.message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(DetailActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getCountToString(int count) {
        return count < 100 ? count + "" : "99+";
    }

    public RecipeVO getRecipeVO() {
        return recipeVO;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ReplyActivity.REPLY_REQUEST_CODE) {
                NetworkManager.getInstance().getRecipe(this, recipeVO.get_id(), new NetworkManager.OnResultListener<RecipeResult>() {
                    @Override
                    public void onSuccess(RecipeResult data) {
                        if (data.result.equals(CooxingConstant.SUCCESS)) {
                            recipeVO = data.data;

                            initCommentMap(recipeVO.getComments());
                            tvLikeCount.setText(getCountToString(recipeVO.getBookmarkers().size()));
                            drawCommentCount(pagePosition);

                            DetailParentFragment f = mAdapter.getItems().get(pagePosition);
                            f.initRecipeVO();

                        } else if (data.result.equals(CooxingConstant.FAIL)) {
                            Toast.makeText(DetailActivity.this, data.message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(DetailActivity.this, code + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean isWindowClicked = false;
    public boolean getIsWindowClicked() {
        return isWindowClicked;
    }
    public void setWindow() {
        if(isWindowClicked) {
            isWindowClicked = false;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            isWindowClicked = true;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        DetailParentFragment prevFragment = mAdapter.getItems().get(viewPager.getCurrentItem() + 1);
        DetailParentFragment currentFragment = mAdapter.getItems().get(viewPager.getCurrentItem());
        DetailParentFragment nextFragment = mAdapter.getItems().get(viewPager.getCurrentItem() - 1);

        if(prevFragment != null && prevFragment instanceof DetailStepFragment) {
            ((DetailStepFragment) prevFragment).drawWindowButton();
        }
        if(currentFragment != null && currentFragment instanceof DetailStepFragment) {
            ((DetailStepFragment) currentFragment).drawWindowButton();
        }
        if(nextFragment != null && nextFragment instanceof DetailStepFragment) {
            ((DetailStepFragment) nextFragment).drawWindowButton();
        }

        mAdapter.notifyDataSetChanged();
    }

    public void showStopwatch() {

    }

}
