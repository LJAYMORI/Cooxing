package com.ljaymori.cooxing.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.UserResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.dialog.LoadingDialogFragment;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.manager.PropertyManager;
import com.ljaymori.cooxing.common.vo.UserVO;
import com.ljaymori.cooxing.interest.InterestActivity;
import com.ljaymori.cooxing.main.MainActivity;
import com.ljaymori.cooxing.signup.SignupActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private MaterialEditText etID;
    private MaterialEditText etPW;
    private TextView tvLogin;
    private TextView tvFindPW;
    private TextView tvKakaoLogin;
    private TextView tvFacebookLogin;
    private TextView tvSignup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etID = (MaterialEditText) findViewById(R.id.edit_id_login);
        etPW = (MaterialEditText) findViewById(R.id.edit_password_login);

        tvLogin = (TextView) findViewById(R.id.text_login_login);
        tvFindPW = (TextView) findViewById(R.id.text_find_password_login);
        tvKakaoLogin = (TextView) findViewById(R.id.text_kakao_login);
        tvFacebookLogin = (TextView) findViewById(R.id.text_facebook_login);
        tvSignup = (TextView) findViewById(R.id.text_signup_login);

        tvLogin.setOnClickListener(this);
        tvFindPW.setOnClickListener(this);
        tvKakaoLogin.setOnClickListener(this);
        tvFacebookLogin.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_login_login: {
                login();
                break;
            }
            case R.id.text_find_password_login: {
                Toast.makeText(this, "서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.text_kakao_login: {
                Toast.makeText(this, "서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.text_facebook_login: {
                Toast.makeText(this, "서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.text_signup_login: {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;
            }
        }
    }

    private void login() {
        final LoadingDialogFragment dialog = new LoadingDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), LoadingDialogFragment.DIALOG_LOADING);

        UserVO user = new UserVO();
        user.setUserID(etID.getText().toString());
        user.setPw(etPW.getText().toString());
        user.setPushKey(PropertyManager.getInstance().getRegistrationId());

        NetworkManager.getInstance().login(this, user, new NetworkManager.OnResultListener<UserResult>() {
            @Override
            public void onSuccess(final UserResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    PropertyManager.getInstance().setUserID(etID.getText().toString());
                    PropertyManager.getInstance().setUserPW(etPW.getText().toString());
                    PropertyManager.getInstance().setJoinPath(CooxingConstant.COOXING);

                    MyData.getInstance().setMyData(data.data);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(data.data.getIngredients().size() != 0) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(LoginActivity.this, InterestActivity.class));
                                finish();
                            }
                            dialog.dismiss();
                        }
                    }, 1000);

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, data.message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }, 1000);
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(LoginActivity.this, code + "", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
