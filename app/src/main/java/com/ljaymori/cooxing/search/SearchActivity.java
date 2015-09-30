package com.ljaymori.cooxing.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.NetworkResult;
import com.ljaymori.cooxing.auth.result.RecipeListResult;
import com.ljaymori.cooxing.auth.result.UserListResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.manager.PropertyManager;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.common.vo.UserVO;
import com.ljaymori.cooxing.detail.DetailActivity;
import com.ljaymori.cooxing.profile.ProfileActivity;
import com.ljaymori.cooxing.search.history.SearchHistoryAdapter;

import java.util.ArrayList;

public class SearchActivity extends ActionBarActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    public static final String PARAM_KEYWORD = "keyword";

    public static final int SEARCH_TYPE_USER = 0;
    public static final int SEARCH_TYPE_RECIPE = 1;


    private View actionbarView;
    private EditText etSearch;
    private TextView tvSearch;

    private int currentSearchType = SEARCH_TYPE_RECIPE;
    private TextView tvUser;
    private TextView tvRecipe;

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private RecipeSearchAdapter recipeAdapter;
    private UserSearchAdapter userAdapter;


    private View viewHistory;
    private RecyclerView historyRecyclerView;
    private SearchHistoryAdapter historyAdapter;
    private LinearLayoutManager historyLayoutManager;


    private TextView tvNoResult;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Log.i("SearchActivity", "onCreate");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_left_arrow);

        actionbarView = LayoutInflater.from(this).inflate(R.layout.actionbar_search, null);
        etSearch = (EditText) actionbarView.findViewById(R.id.edit_search_actionbar);
        tvSearch = (TextView) actionbarView.findViewById(R.id.text_search_actionbar);
        etSearch.setOnClickListener(this);
        etSearch.setOnEditorActionListener(this);
        tvSearch.setOnClickListener(this);

        getSupportActionBar().setCustomView(actionbarView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        tvUser = (TextView) findViewById(R.id.text_user_search);
        tvRecipe = (TextView) findViewById(R.id.text_recipe_search);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_search);

        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);

        userAdapter = new UserSearchAdapter(this);
        recipeAdapter = new RecipeSearchAdapter(this);

        tvUser.setOnClickListener(this);
        tvRecipe.setOnClickListener(this);


        viewHistory = findViewById(R.id.view_search_history);
        historyRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_search_history);

        historyLayoutManager = new LinearLayoutManager(this);
        historyRecyclerView.setLayoutManager(historyLayoutManager);

        historyAdapter = new SearchHistoryAdapter(this);
        historyRecyclerView.setAdapter(historyAdapter);

        tvNoResult = (TextView) findViewById(R.id.text_no_result_search);
        tvNoResult.setVisibility(View.GONE);

        drawSearchType(SEARCH_TYPE_RECIPE);
        drawHistory();
        setFonts();



        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(etSearch, 0);
            }
        }, 100);

        Uri data = getIntent().getData();
        if(data != null) {
            final String keyword = data.getQueryParameter(PARAM_KEYWORD);
            etSearch.setText(keyword);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    search(keyword);
                }
            }, 200);
        }
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvNoResult);
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvRecipe, tvUser);
    }

    @Override
    public void onBackPressed() {
        if (viewHistory.getVisibility() == View.VISIBLE) {
            viewHistory.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_user_search: {
                drawNoResultMessage(userAdapter.getItemCount());
                drawSearchType(SEARCH_TYPE_USER);
                break;

            }
            case R.id.text_recipe_search: {
                drawNoResultMessage(recipeAdapter.getItemCount());
                drawSearchType(SEARCH_TYPE_RECIPE);
                break;

            }
            case R.id.text_search_actionbar: {
                String keyword = etSearch.getText().toString();
                search(keyword);
                break;

            }
            case R.id.edit_search_actionbar: {
                drawHistory();
                break;
            }
        }
    }

    private void drawSearchType(int type) {
        currentSearchType = type;
        if (type == SEARCH_TYPE_RECIPE) {
            tvRecipe.setBackgroundResource(R.drawable.background_tab_focused_left);
            tvRecipe.setTextColor(getResources().getColor(R.color.tab_selected_text));
            tvUser.setBackgroundResource(R.drawable.background_tab_unfocused_right);
            tvUser.setTextColor(getResources().getColor(R.color.tab_unselected_text));

            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(recipeAdapter);

        } else if (type == SEARCH_TYPE_USER) {
            tvRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_left);
            tvRecipe.setTextColor(getResources().getColor(R.color.tab_unselected_text));
            tvUser.setBackgroundResource(R.drawable.background_tab_focused_right);
            tvUser.setTextColor(getResources().getColor(R.color.tab_selected_text));

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(userAdapter);
        }
    }

    private void drawHistory() {
        ArrayList<String> searchList = PropertyManager.getInstance().getSearchList();
        if (searchList.size() > 0) {
            if (viewHistory.getVisibility() == View.GONE) {
                viewHistory.setVisibility(View.VISIBLE);
            }
            historyAdapter.addAll(searchList);

        } else {
            if (viewHistory.getVisibility() == View.VISIBLE) {
                viewHistory.setVisibility(View.GONE);
            }
        }
    }

    private void initUserData(String keyword) {
        NetworkManager.getInstance().searchUser(this, keyword, new NetworkManager.OnResultListener<UserListResult>() {
            @Override
            public void onSuccess(UserListResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    ArrayList<UserSearchItemData> list = new ArrayList<UserSearchItemData>();
                    for (UserVO vo : data.datas) {
                        UserSearchItemData ud = new UserSearchItemData();
                        ud.set_id(vo.get_id());
                        ud.setImageURL(vo.getProfileImage());
                        ud.setUserName(vo.getNickname());
                        ud.setIsFollowing(MyData.getInstance().getFollowing().contains(vo.get_id()));

                        list.add(ud);
                    }

                    userAdapter.addAll(list);
                    drawNoResultMessage(userAdapter.getItemCount());

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(SearchActivity.this, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(SearchActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecipeData(String keyword) {
        NetworkManager.getInstance().searchRecipe(this, keyword, new NetworkManager.OnResultListener<RecipeListResult>() {
            @Override
            public void onSuccess(RecipeListResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    ArrayList<RecipeSearchItemData> list = new ArrayList<RecipeSearchItemData>();

                    for (RecipeVO vo : data.datas) {
                        RecipeSearchItemData rd = new RecipeSearchItemData();
                        rd.setImageURL(vo.getImages().get(vo.getImages().size() - 1));
                        rd.setRecipeID(vo.get_id());
                        rd.setRecipeName(vo.getTitle());

                        list.add(rd);
                    }

                    recipeAdapter.addAll(list);
                    drawNoResultMessage(recipeAdapter.getItemCount());

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(SearchActivity.this, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(SearchActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void follow(final String id) {
        NetworkManager.getInstance().follow(this, id, new NetworkManager.OnResultListener<NetworkResult>() {
            @Override
            public void onSuccess(NetworkResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getFollowing().add(id);
                    userAdapter.notifyDataSetChanged();

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(SearchActivity.this, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(SearchActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void unfollow(final String id) {
        NetworkManager.getInstance().unfollow(this, id, new NetworkManager.OnResultListener<NetworkResult>() {
            @Override
            public void onSuccess(NetworkResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getFollowing().remove(id);
                    userAdapter.notifyDataSetChanged();

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(SearchActivity.this, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(SearchActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void userClick(String id) {
        Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER_ID, id);
        startActivity(intent);
    }

    public void recipeClick(String id) {
        Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.RECIPE_ID, id);
        startActivity(intent);

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void drawNoResultMessage(int size) {
        if(size > 0) {
            if(tvNoResult.getVisibility() == View.VISIBLE) {
                tvNoResult.setVisibility(View.GONE);
            }

        } else {
            if(tvNoResult.getVisibility() == View.GONE) {
                tvNoResult.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String keyword = etSearch.getText().toString();
        if (actionId == EditorInfo.IME_ACTION_SEARCH && search(keyword)) {
            return true;
        }
        return false;
    }

    public boolean search(String keyword) {
        if (keyword.length() > 1) {
            PropertyManager.getInstance().addSearchList(keyword);

            if (currentSearchType == SEARCH_TYPE_RECIPE) {
                initRecipeData(keyword);

            } else if (currentSearchType == SEARCH_TYPE_USER) {
                initUserData(keyword);

            }
            imm.hideSoftInputFromWindow(etSearch.getApplicationWindowToken(), 0);
            if (viewHistory.getVisibility() == View.VISIBLE) {
                viewHistory.setVisibility(View.GONE);
            }
            return true;

        } else {
            Toast.makeText(SearchActivity.this, getResources().getString(R.string.search_message_keyword_length), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void removeHistory(String keyword) {
        int size = PropertyManager.getInstance().removeSearchList(keyword);
        if (size == 0) {
            if (viewHistory.getVisibility() == View.VISIBLE) {
                viewHistory.setVisibility(View.GONE);
            }
        }
    }
}
