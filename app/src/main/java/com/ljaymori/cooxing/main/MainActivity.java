package com.ljaymori.cooxing.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.NetworkResult;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.detail.DetailActivity;
import com.ljaymori.cooxing.like.LikeRecipeFragment;
import com.ljaymori.cooxing.notify.NotifyFragment;
import com.ljaymori.cooxing.profile.ProfileActivity;
import com.ljaymori.cooxing.search.SearchActivity;
import com.ljaymori.cooxing.write.WriteActivity;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final int MAIN_REQUEST_CODE = 314;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int currentPosition = -1;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;

    private LikeRecipeFragment likeRecipeFragment;
    private NotifyFragment notifyFragment;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        getSupportActionBar().setTitle(getString(R.string.title_activity_main));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        NetworkManager.getInstance().logout(this, new NetworkManager.OnResultListener<NetworkResult>() {
            @Override
            public void onSuccess(NetworkResult data) {

            }

            @Override
            public void onFail(int code) {

            }
        });
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        currentPosition = position;

        if (likeRecipeFragment == null) {
            likeRecipeFragment = new LikeRecipeFragment();
        }

        if (notifyFragment == null) {
            notifyFragment = new NotifyFragment();
        }

        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case NavigationDrawerFragment.POSITION_INTEREST: {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, likeRecipeFragment).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.title_activity_interest));
                }
                break;
            }
            case NavigationDrawerFragment.POSITION_NOTIFY: {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, notifyFragment).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.title_activity_notify));
                }
                break;
            }
            case NavigationDrawerFragment.POSITION_MAIN: {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mainFragment).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.title_activity_main));
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_order_by_recent) {

            return true;

        } else if (id == R.id.action_order_by_populate) {

            return true;

        } else if (id == R.id.action_order_by_interest) {

            return true;

        } else if (id == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private static final int MESSAGE_TIME_OUT_BACK_KEY = 0;
    private static final int TIME_BACK_KEY = 2000;
    private boolean isBackPressed = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TIME_OUT_BACK_KEY:
                    isBackPressed = false;
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeNavigation();
            return;
        }

        if (!isBackPressed) {
            isBackPressed = true;
            Toast.makeText(this, getResources().getString(R.string.one_more_back_key), Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(MESSAGE_TIME_OUT_BACK_KEY, TIME_BACK_KEY);
        } else {
            isBackPressed = false;
            mHandler.removeMessages(MESSAGE_TIME_OUT_BACK_KEY);
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("requestCode", requestCode + "");
        Log.i("WriteRequestCode", WriteActivity.WRITE_REQUEST_CODE + "");
        Log.i("ProfileRequestCode", ProfileActivity.PROFILE_REQUEST_CODE + "");
        Log.i("resultCode", resultCode + "");
        Log.i("ActivityResultCOde", Activity.RESULT_OK + "");

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DetailActivity.DETAIL_REQUEST_CODE) {
                RecipeVO recipeVO = (RecipeVO) data.getSerializableExtra(DetailActivity.RECIPE_VO);
                if (recipeVO != null) {
                    mainFragment.updateRecipe(recipeVO);
                }

            } else if (requestCode == WriteActivity.WRITE_REQUEST_CODE) {
                mainFragment.getMainAdapter().notifyDataSetChanged();

            } else if (requestCode == ProfileActivity.PROFILE_REQUEST_CODE) {
//                mainFragment.getMainAdapter().notifyDataSetChanged();
                onNavigationDrawerItemSelected(currentPosition);
                mNavigationDrawerFragment.setUp(
                        R.id.navigation_drawer,
                        (DrawerLayout) findViewById(R.id.drawer_layout));
            }
        }
    }

    public void startWriteActivity() {
        Intent intent = new Intent(MainActivity.this, WriteActivity.class);
        startActivityForResult(intent, WriteActivity.WRITE_REQUEST_CODE);
    }

    public MainFragment getMainFragment() {
        return mainFragment;
    }

    /**
     * MainFragmentEvent
     */
    public void onClickUser(String user_id) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER_ID, user_id);

        startActivityForResult(intent, ProfileActivity.PROFILE_REQUEST_CODE);
    }

    /**
     * LikeFragmentEvent
     */
    public void onClickRecipe(String recipe_id) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.RECIPE_ID, recipe_id);

        startActivity(intent);
    }

}
