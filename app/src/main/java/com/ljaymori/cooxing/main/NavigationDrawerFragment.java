package com.ljaymori.cooxing.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.auth.result.NetworkResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.manager.PropertyManager;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.login.LoginActivity;
import com.ljaymori.cooxing.profile.ProfileActivity;
import com.ljaymori.cooxing.setting.SettingActivity;
import com.squareup.picasso.Picasso;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */

public class NavigationDrawerFragment extends Fragment implements OnClickListener {

    public static final int POSITION_INTEREST = 0;
    public static final int POSITION_NOTIFY = 1;
    public static final int POSITION_MAIN = 2;

    public View viewRoot;

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the
     * user manually expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;

    private boolean mFromSavedInstanceState;
//    private boolean mUserLearnedDrawer;
    private int mCurrentSelectedPosition = POSITION_MAIN;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated
        // awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
//        SharedPreferences sp = PreferenceManager
//                .getDefaultSharedPreferences(getActivity());
//        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState
                    .getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of
        // actions in the action bar.
        setHasOptionsMenu(true);
    }

    private ImageView ivUser;
    private TextView tvSetting;
    private TextView tvNickname;
    private TextView tvIntro;

    private View viewInterest;
    private TextView tvInterest;
    private TextView tvInterestIcon;

    private View viewNotify;
    private TextView tvNotify;
    private TextView tvNotifyIcon;

    private View viewMain;
    private TextView tvMain;
    private TextView tvMainIcon;

    private View viewLogout;
    private TextView tvLogout;
    private TextView tvLogoutIcon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        viewRoot = view.findViewById(R.id.rootLayout_navi);

        ivUser = (ImageView) view.findViewById(R.id.image_profile_navi);
        tvSetting = (TextView) view.findViewById(R.id.text_setting_navi);
        tvNickname = (TextView) view.findViewById(R.id.text_nickname_navi);
        tvIntro = (TextView) view.findViewById(R.id.text_intro_navi);

        viewInterest = view.findViewById(R.id.view_interest_navi);
        tvInterest = (TextView) view.findViewById(R.id.text_interest_navi);
        tvInterestIcon = (TextView) view.findViewById(R.id.text_interest_icon_navi);

        viewNotify = view.findViewById(R.id.view_alarm_navi);
        tvNotify = (TextView) view.findViewById(R.id.text_alarm_navi);
        tvNotifyIcon = (TextView) view.findViewById(R.id.text_alarm_icon_navi);

        viewMain = view.findViewById(R.id.view_main_navi);
        tvMain = (TextView)view.findViewById(R.id.text_main_navi);
        tvMainIcon = (TextView)view.findViewById(R.id.text_main_icon_navi);

        viewLogout = view.findViewById(R.id.view_logout_navi);
        tvLogout = (TextView) view.findViewById(R.id.text_logout_navi);
        tvLogoutIcon = (TextView) view.findViewById(R.id.text_logout_icon_navi);


//        ImageLoader.getInstance().displayImage(NetworkConstant.HTTP_URL + MyData.getInstance().getProfileImage(), ivUser);

        initProfile();

        viewRoot.setOnClickListener(this);

        ivUser.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
        viewInterest.setOnClickListener(this);
        viewNotify.setOnClickListener(this);
        viewMain.setOnClickListener(this);
        viewLogout.setOnClickListener(this);

        setFonts();

        return view;
    }

    public void initProfile() {
        Picasso.with(getActivity())
                .load(NetworkConstant.HTTP_URL + MyData.getInstance().getProfileImage())
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(ivUser);

        tvNickname.setText(MyData.getInstance().getNickname());
        tvIntro.setText(MyData.getInstance().getIntro());
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvNickname, tvMain);
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvLogout);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.image_profile_navi: {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        intent.putExtra(ProfileActivity.USER_ID, MyData.getInstance().get_id());
                        ((MainActivity) getActivity()).startActivityForResult(intent, ProfileActivity.PROFILE_REQUEST_CODE);
                        closeNavigation();
                    }
                }, 100);
                break;
            }
            case R.id.text_setting_navi : {
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            }
            case R.id.view_interest_navi : {
                selectItem(POSITION_INTEREST);
                break;
            }
            case R.id.view_alarm_navi: {
                Toast.makeText(getActivity(), "서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
//                selectItem(POSITION_NOTIFY);
                break;
            }
            case R.id.view_main_navi : {
                selectItem(POSITION_MAIN);
                break;
            }
            case R.id.view_logout_navi : {
                PropertyManager.getInstance().logout();
                NetworkManager.getInstance().logout(getActivity(), new NetworkManager.OnResultListener<NetworkResult>() {
                    @Override
                    public void onSuccess(NetworkResult data) {
                        if(data.result.equals(CooxingConstant.SUCCESS)) {
                            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();

                        } else if (data.result.equals(CooxingConstant.FAIL)) {
                            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(getActivity(), code+"", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
                break;
            }
        }

    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null
                && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation
     * drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(), /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.string.navigation_drawer_open, /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls
                // onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

//                if (!mUserLearnedDrawer) {
//                    // The user manually opened the drawer; store this flag to
//                    // prevent auto-showing
//                    // the navigation drawer automatically in the future.
//                    mUserLearnedDrawer = true;
//                    SharedPreferences sp = PreferenceManager
//                            .getDefaultSharedPreferences(getActivity());
//                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
//                            .commit();
//                }

                getActivity().supportInvalidateOptionsMenu(); // calls
                // onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer /* nav drawer image to replace 'Up' caret */);
        // If the user hasn't 'learned' about the drawer, open it to introduce
        // them to the drawer,
        // per the navigation drawer design guidelines.

//        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
//            mDrawerLayout.openDrawer(mFragmentContainerView);
//        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;

        if(isInitedNavigation()) {
            setMenuBackground(position);
        }

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public void setMenuBackground(int position) {
        switch (position) {
            case POSITION_INTEREST : {
                viewInterest.setBackgroundColor(getResources().getColor(R.color.navi_selected));
                tvInterest.setTextColor(getResources().getColor(R.color.navi_selected_text));
                tvInterestIcon.setBackgroundResource(R.drawable.ic_focused_interest);

                viewNotify.setBackgroundResource(R.drawable.selector_background_navi_menu);
                tvNotify.setTextColor(getResources().getColor(R.color.navi_unselected_text));
                tvNotifyIcon.setBackgroundResource(R.drawable.ic_unfocused_alarm);

                viewMain.setBackgroundResource(R.drawable.selector_background_navi_menu);
                tvMain.setTextColor(getResources().getColor(R.color.navi_unselected_text));
                tvMainIcon.setBackgroundResource(R.drawable.ic_unfocused_main_recipes);

                FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvInterest);
                FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvNotify, tvMain);

                break;
            }
            case POSITION_NOTIFY: {
                viewInterest.setBackgroundResource(R.drawable.selector_background_navi_menu);
                tvInterest.setTextColor(getResources().getColor(R.color.navi_unselected_text));
                tvInterestIcon.setBackgroundResource(R.drawable.ic_unfocused_interest);

                viewNotify.setBackgroundColor(getResources().getColor(R.color.navi_selected));
                tvNotify.setTextColor(getResources().getColor(R.color.navi_selected_text));
                tvNotifyIcon.setBackgroundResource(R.drawable.ic_focused_alarm);

                viewMain.setBackgroundResource(R.drawable.selector_background_navi_menu);
                tvMain.setTextColor(getResources().getColor(R.color.navi_unselected_text));
                tvMainIcon.setBackgroundResource(R.drawable.ic_unfocused_main_recipes);

                FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvNotify);
                FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvInterest, tvMain);

                break;
            }
            case POSITION_MAIN : {
                viewInterest.setBackgroundResource(R.drawable.selector_background_navi_menu);
                tvInterest.setTextColor(getResources().getColor(R.color.navi_unselected_text));
                tvInterestIcon.setBackgroundResource(R.drawable.ic_unfocused_interest);

                viewNotify.setBackgroundResource(R.drawable.selector_background_navi_menu);
                tvNotify.setTextColor(getResources().getColor(R.color.navi_unselected_text));
                tvNotifyIcon.setBackgroundResource(R.drawable.ic_unfocused_alarm);

                viewMain.setBackgroundColor(getResources().getColor(R.color.navi_selected));
                tvMain.setTextColor(getResources().getColor(R.color.navi_selected_text));
                tvMainIcon.setBackgroundResource(R.drawable.ic_focused_main_recipes);

                FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvMain);
                FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvInterest, tvNotify);

                break;
            }
        }
    }

    public void closeNavigation() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_order_by_recent) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to
     * show the global app 'context', rather than just what's in the current
     * screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public boolean isInitedNavigation() {
        return viewInterest != null && viewMain != null && viewNotify != null;
    }


    /**
     * Callbacks interface that all activities using this fragment must
     * implement.
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}