package com.ljaymori.cooxing.splash;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.UserResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.manager.PropertyManager;
import com.ljaymori.cooxing.common.vo.UserVO;
import com.ljaymori.cooxing.interest.InterestActivity;
import com.ljaymori.cooxing.login.LoginActivity;
import com.ljaymori.cooxing.main.MainActivity;

import java.io.IOException;

public class SplashActivity extends ActionBarActivity {

    public static final long SPLASH_DELAY_TIME = 2000;
    public static final String TAG = SplashActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // test용
//		boolean check = true;
//		if (check) {
//			redirectActivity(MainActivity.class);
//		}
    }

    Runnable nextAction = new Runnable() {

        @Override
        public void run() {
            // 경우에 따른 Redirect!
            String joinPath = PropertyManager.getInstance().getJoinPath();

            UserVO user = new UserVO();
            user.setUserID(PropertyManager.getInstance().getUserID());
            user.setPw(PropertyManager.getInstance().getUserPW());
            user.setPushKey(PropertyManager.getInstance().getRegistrationId());

            if (joinPath.equals("")) {
                redirectActivity(LoginActivity.class);

            } else if (joinPath.equals(CooxingConstant.FACEBOOK)) {

            } else if (joinPath.equals(CooxingConstant.KAKAOTALK)) {

            } else if (joinPath.equals(CooxingConstant.COOXING)) {

                NetworkManager.getInstance().login(SplashActivity.this, user, new NetworkManager.OnResultListener<UserResult>() {
                    @Override
                    public void onSuccess(UserResult data) {
                        if(data.result.equals(CooxingConstant.SUCCESS)) {
                            MyData.getInstance().set_id(data.data.get_id());
                            MyData.getInstance().setNickname(data.data.getNickname());
                            MyData.getInstance().setProfileImage(data.data.getProfileImage());
                            MyData.getInstance().setIntro(data.data.getIntro());
                            MyData.getInstance().setRecipes(data.data.getRecipes());
                            MyData.getInstance().setBookmarks(data.data.getBookmarks());
                            MyData.getInstance().setHistory(data.data.getHistory());
                            MyData.getInstance().setFollowing(data.data.getFollowing());
                            MyData.getInstance().setFollower(data.data.getFollower());
                            MyData.getInstance().setIngredients(data.data.getIngredients());
                            MyData.getInstance().setPoint(data.data.getPoint());

                            if(data.data.getIngredients().size() == 0) {
                                redirectActivity(InterestActivity.class);
                            } else {
                                redirectActivity(MainActivity.class);
                            }

                        } else if (data.result.equals(CooxingConstant.FAIL)) {
                            Toast.makeText(SplashActivity.this, data.message, Toast.LENGTH_SHORT).show();
                            redirectActivity(LoginActivity.class);
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(SplashActivity.this, code + "", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }
    };

    private void redirectActivity(final Class activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, activity));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, SPLASH_DELAY_TIME);
    }


    private boolean isFirst = true;

    @Override
    protected void onResume() {
        super.onResume();

        if (checkPlayServices()) {
            String regId = PropertyManager.getInstance().getRegistrationId();
            if (!regId.equals("")) {
                runOnUiThread(nextAction);
            } else {
                registerInBackground();
            }
        } else {
            if (isFirst) {
                isFirst = false;
            } else {
                //TODO 서비스가 없다 알려줘

            }
        }
    }

    GoogleCloudMessaging gcm;

    private void registerInBackground() {
        new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(SplashActivity.this);
                    }
                    String regid = gcm.register(getResources().getString(R.string.sender_id));
                    PropertyManager.getInstance().setRegistrationId(regid);

//					ServerUtilities.register(SplashActivity.this, regid);
                } catch (IOException ex) {
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                runOnUiThread(nextAction);
            }
        }.execute(null, null, null);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
            } else {
                //TODO 여기선 뭐하는거지?
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
