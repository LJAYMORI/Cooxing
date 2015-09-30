package com.ljaymori.cooxing.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.UserResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.manager.PropertyManager;
import com.ljaymori.cooxing.common.vo.UserVO;
import com.ljaymori.cooxing.interest.InterestActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignupActivity extends ActionBarActivity implements View.OnClickListener {

    public static final int SIGNUP_ACTIVITY = 123;
    public static final String CHECK_AGREEMENT = "check_agreement";

    public static final int CHECKBOX_ON = R.drawable.check_on;
    public static final int CHECKBOX_OFF = R.drawable.check_off;

    private MaterialEditText etID;
    private MaterialEditText etPW;
    private MaterialEditText etPWCheck;
    private MaterialEditText etNickname;

    private boolean checkAgreement = false;
    private View viewAgree;
    private TextView tvAgreeIcon;

    private TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etID = (MaterialEditText) findViewById(R.id.edit_id_signup);
        etPW = (MaterialEditText) findViewById(R.id.edit_password_signup);
        etPWCheck = (MaterialEditText) findViewById(R.id.edit_password_check_signup);
        etNickname = (MaterialEditText) findViewById(R.id.edit_nickname_signup);

        tvAgreeIcon = (TextView) findViewById(R.id.text_agree_signup);
        viewAgree = findViewById(R.id.view_agree_signup);

        tvSubmit = (TextView) findViewById(R.id.text_submit_signup);

        viewAgree.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_agree_signup : {
                Intent intent = new Intent(SignupActivity.this, ClauseSignupActivity.class);
                intent.putExtra(CHECK_AGREEMENT, checkAgreement);
                startActivityForResult(intent, SIGNUP_ACTIVITY);
                break;
            }
            case R.id.text_submit_signup : {
                UserVO user = new UserVO();
                user.setUserID(etID.getText().toString());
                user.setPw(etPW.getText().toString());
                user.setNickname(etNickname.getText().toString());
                user.setPushKey(PropertyManager.getInstance().getRegistrationId());

                NetworkManager.getInstance().signup(this, user, new NetworkManager.OnResultListener<UserResult>() {
                    @Override
                    public void onSuccess(UserResult data) {
                        if (data.result.equals(CooxingConstant.SUCCESS)) {
                            PropertyManager.getInstance().setUserID(etID.getText().toString());
                            PropertyManager.getInstance().setUserPW(etPW.getText().toString());
                            PropertyManager.getInstance().setJoinPath(CooxingConstant.COOXING);

                            startActivity(new Intent(SignupActivity.this, InterestActivity.class));
                            finish();

                        } else if (data.result.equals(CooxingConstant.FAIL)) {
                            Toast.makeText(SignupActivity.this, data.message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(SignupActivity.this, code + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGNUP_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK) {
                checkAgreement = true;
                tvAgreeIcon.setBackgroundResource(R.drawable.check_on);

            } else if(resultCode == Activity.RESULT_CANCELED) {
                checkAgreement = false;
                tvAgreeIcon.setBackgroundResource(R.drawable.check_off);

            }
        }
    }
}
