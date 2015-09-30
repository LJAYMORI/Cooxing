package com.ljaymori.cooxing.userlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.NetworkResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.vo.UserVO;
import com.ljaymori.cooxing.profile.ProfileActivity;

import java.util.ArrayList;

public class UserListActivity extends ActionBarActivity implements View.OnClickListener {

    public static final int USER_LIST_REQUEST_CODE = 529;

    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_FOLLOWING_LIST = "extra_following_list";
    public static final String EXTRA_FOLLOWER_LIST = "extra_follower_list";

    public static final int TYPE_FOLLOWER = 0;
    public static final int TYPE_FOLLOWING = 1;

    private TextView tvTabFollowing;
    private TextView tvTabFollower;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserListAdapter mAdapter;

    private int type;
    private ArrayList<UserVO> followingList;
    private ArrayList<UserVO> followerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_left_arrow);

        type = getIntent().getIntExtra(EXTRA_TYPE, -1);
        try {
            followingList = new ArrayList<UserVO>((ArrayList<UserVO>) getIntent().getSerializableExtra(EXTRA_FOLLOWING_LIST));
            followerList = new ArrayList<UserVO>((ArrayList<UserVO>) getIntent().getSerializableExtra(EXTRA_FOLLOWER_LIST));
        } catch (Exception e) {
            Toast.makeText(UserListActivity.this, getString(R.string.user_list_no_extra), Toast.LENGTH_SHORT).show();
            finish();
        }

        tvTabFollowing = (TextView) findViewById(R.id.text_tab_following_user_list);
        tvTabFollower= (TextView) findViewById(R.id.text_tab_follower_user_list);
        tvTabFollowing.setOnClickListener(this);
        tvTabFollower.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_user_list);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new UserListAdapter(this);
        recyclerView.setAdapter(mAdapter);

        initData();
    }

    @Override
    public void finish() {
        setResult(Activity.RESULT_OK);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initData() {
        drawTab();
        if(type == TYPE_FOLLOWING) {
            mAdapter.init(followingList);

        } else if (type == TYPE_FOLLOWER) {
            mAdapter.init(followerList);

        }
    }

    private void drawTab() {
        if(type == TYPE_FOLLOWING) {
            setActionBarTitle(getString(R.string.user_list_title_following_list));
            tvTabFollowing.setBackgroundResource(R.drawable.background_tab_focused_left);
            tvTabFollower.setBackgroundResource(R.drawable.background_tab_unfocused_right);

            tvTabFollowing.setTextColor(getResources().getColor(R.color.tab_selected_text));
            tvTabFollower.setTextColor(getResources().getColor(R.color.tab_unselected_text));

        } else if (type == TYPE_FOLLOWER) {
            setActionBarTitle(getString(R.string.user_list_title_follower_list));
            tvTabFollowing.setBackgroundResource(R.drawable.background_tab_unfocused_left);
            tvTabFollower.setBackgroundResource(R.drawable.background_tab_focused_right);

            tvTabFollowing.setTextColor(getResources().getColor(R.color.tab_unselected_text));
            tvTabFollower.setTextColor(getResources().getColor(R.color.tab_selected_text));
        }
    }

    private void setActionBarTitle(String title) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_user_list, menu);
        return false;
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

    public void unfollow(final String id) {
        NetworkManager.getInstance().unfollow(this, id, new NetworkManager.OnResultListener<NetworkResult>() {
            @Override
            public void onSuccess(NetworkResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getFollowing().remove(id);
                    mAdapter.notifyDataSetChanged();

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(UserListActivity.this, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(UserListActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void follow(final String id) {
        NetworkManager.getInstance().follow(this, id, new NetworkManager.OnResultListener<NetworkResult>() {
            @Override
            public void onSuccess(NetworkResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getFollowing().add(id);
                    mAdapter.notifyDataSetChanged();

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(UserListActivity.this, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(UserListActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void userClick(String id) {
        Intent intent = new Intent(UserListActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER_ID, id);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_tab_following_user_list: {
                type = TYPE_FOLLOWING;
                initData();
                break;
            }
            case R.id.text_tab_follower_user_list: {
                type = TYPE_FOLLOWER;
                initData();
                break;
            }
        }
    }

}
