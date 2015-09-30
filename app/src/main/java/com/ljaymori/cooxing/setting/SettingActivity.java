package com.ljaymori.cooxing.setting;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;

public class SettingActivity extends ActionBarActivity implements View.OnClickListener {
    private TextView tvIndexProfile;
    private TextView tvIndexApp;
    private TextView tvIndexAccount;
    private TextView tvIndexAbout;
    private TextView tvIndexEtc;

    private TextView tvMyProfile;
    private TextView tvIngredient;
    private TextView tvAlarm;
    private TextView tvPassword;
    private TextView tvWithdraw;
    private TextView tvNotice;
    private TextView tvQnA;
    private TextView tvOpenSource;
    private TextView tvClause;
    private TextView tvPolicy;
    private TextView tvProducer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_left_arrow);

        tvIndexProfile = (TextView) findViewById(R.id.text_index_profile_setting);
        tvIndexApp = (TextView) findViewById(R.id.text_index_app_setting);
        tvIndexAccount = (TextView) findViewById(R.id.text_index_account_setting);
        tvIndexAbout = (TextView) findViewById(R.id.text_index_about_setting);
        tvIndexEtc = (TextView) findViewById(R.id.text_index_etc_setting);

        tvMyProfile = (TextView) findViewById(R.id.text_menu_my_profile_setting);
        tvIngredient = (TextView) findViewById(R.id.text_menu_ingredient_setting);
        tvAlarm = (TextView) findViewById(R.id.text_menu_alarm_setting);
        tvPassword = (TextView) findViewById(R.id.text_menu_password_setting);
        tvWithdraw = (TextView) findViewById(R.id.text_menu_withdraw_setting);
        tvNotice = (TextView) findViewById(R.id.text_menu_notice_setting);
        tvQnA = (TextView) findViewById(R.id.text_menu_qna_setting);
        tvOpenSource = (TextView) findViewById(R.id.text_menu_open_source_setting);
        tvClause = (TextView) findViewById(R.id.text_menu_clause_setting);
        tvPolicy = (TextView) findViewById(R.id.text_menu_policy_setting);
        tvProducer = (TextView) findViewById(R.id.text_menu_producer_setting);

        findViewById(R.id.view_my_profile_setting).setOnClickListener(this);
        findViewById(R.id.view_ingredient_setting).setOnClickListener(this);
        findViewById(R.id.view_alarm_setting).setOnClickListener(this);
        findViewById(R.id.view_edit_password_setting).setOnClickListener(this);
        findViewById(R.id.view_withdraw_setting).setOnClickListener(this);
        findViewById(R.id.view_notice_setting).setOnClickListener(this);
        findViewById(R.id.view_qna_setting).setOnClickListener(this);
        findViewById(R.id.view_open_source_setting).setOnClickListener(this);
        findViewById(R.id.view_clause_setting).setOnClickListener(this);
        findViewById(R.id.view_policy_setting).setOnClickListener(this);
        findViewById(R.id.view_producer_setting).setOnClickListener(this);

        setFonts();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD,
                tvIndexProfile, tvIndexApp, tvIndexAccount, tvIndexAbout, tvIndexEtc);

        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL,
                tvMyProfile, tvIngredient, tvAlarm, tvPassword, tvWithdraw, tvNotice, tvQnA,
                tvOpenSource, tvClause, tvPolicy, tvProducer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_my_profile_setting: {
                break;
            }
            case R.id.view_ingredient_setting: {
                break;
            }
            case R.id.view_alarm_setting: {
                break;
            }
            case R.id.view_edit_password_setting: {
                break;
            }
            case R.id.view_withdraw_setting: {
                break;
            }
            case R.id.view_notice_setting: {
                break;
            }
            case R.id.view_qna_setting: {
                break;
            }
            case R.id.view_open_source_setting: {
                break;
            }
            case R.id.view_clause_setting: {
                break;
            }
            case R.id.view_policy_setting: {
                break;
            }
            case R.id.view_producer_setting: {
                break;
            }
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
}
