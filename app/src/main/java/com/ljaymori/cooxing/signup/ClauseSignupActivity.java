package com.ljaymori.cooxing.signup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ljaymori.cooxing.R;

public class ClauseSignupActivity extends ActionBarActivity
        implements View.OnClickListener {

    CheckBox checkUseAgreement;
    CheckBox checkPersonalInform;
    TextView tvOk;


    boolean isUseAgreementChecked = false;
    boolean isPersonalInformChecked = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clause_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.menu_left_arrow));

        boolean checkAgreement = getIntent().getBooleanExtra(SignupActivity.CHECK_AGREEMENT, false);
        TextView tvUseAgreement = (TextView) findViewById(R.id.text_clause_use_agreement);
        TextView tvPersonalInformation = (TextView) findViewById(R.id.text_clause_personal_information);

//        tvUseAgreement.setText(Html.fromHtml(getString(R.string.use_agreement)));
//        tvPersonalInformation.setText(Html.fromHtml(getString(R.string.personal_information)));

        checkUseAgreement = (CheckBox) findViewById(R.id.check_clause_use_agreement);
        checkPersonalInform = (CheckBox) findViewById(R.id.check_clause_personal_information);

        checkUseAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isUseAgreementChecked = isChecked;
                checkAgree();
            }

        });

        checkPersonalInform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPersonalInformChecked = isChecked;
                checkAgree();
            }
        });

        tvOk = (TextView) findViewById(R.id.text_clause_ok);
        tvOk.setClickable(false);
        tvOk.setOnClickListener(this);

        if(checkAgreement){
            checkUseAgreement.setChecked(true);
            checkPersonalInform.setChecked(true);

            isUseAgreementChecked = true;
            isPersonalInformChecked = true;
            checkAgree();

        }
    }

    private void checkAgree(){
        if(isUseAgreementChecked && isPersonalInformChecked){
            tvOk.setBackgroundColor(getResources().getColor(R.color.button_activated_color));
            tvOk.setClickable(true);

        } else {
            tvOk.setBackgroundColor(getResources().getColor(R.color.button_unactivated_color));
            tvOk.setClickable(false);

        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.text_clause_ok){
            setResult(Activity.RESULT_OK);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.signup, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_order_by_recent) {
            return true;
        } else if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
