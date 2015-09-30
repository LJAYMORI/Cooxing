package com.ljaymori.cooxing.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.StringResult;
import com.ljaymori.cooxing.auth.result.UserPopResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.dialog.LoadingDialogFragment;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.common.vo.UserPopVO;
import com.ljaymori.cooxing.detail.DetailActivity;
import com.ljaymori.cooxing.userlist.UserListActivity;
import com.ljaymori.cooxing.write.WriteActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class ProfileActivity extends ActionBarActivity implements View.OnClickListener {

    public static final int PROFILE_REQUEST_CODE = 946;

    public static final float MAX_SCROLL_Y = 500;

    public static final int TYPE_USER = 0;
    public static final int TYPE_TAB = 1;
    public static final int TYPE_RECIPE = 2;

    public static final String NOT_EMPTY = "";

    public static final int TAB_WROTE_RECIPE = 0;
    public static final int TAB_BOOKMARK_RECIPE = 1;
    public static final int TAB_OPENED_RECIPE = 2;
    private int currentTabPosition = TAB_WROTE_RECIPE;

    public static final String USER_ID = "user_id";

    private RecyclerView recyclerView;
    private GridLayoutManager mGridManager;
    private ProfileAdapter mAdapter;

    private View viewTab;
    private TextView tvWroteRecipe;
    private TextView tvInterestRecipe;
    private TextView tvOpenRecipe;
    private float userInfoHeight;

    private View viewWrite;
    private float writeY = 0;
    private boolean isVisibleWrite;

    private UserPopVO userVO;
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_left_arrow);

        user_id = getIntent().getStringExtra(USER_ID);
        if (user_id == null) {
            Toast.makeText(ProfileActivity.this, getString(R.string.profile_message_no_extra), Toast.LENGTH_SHORT).show();
            finish();

        } else {
            userInfoHeight = getResources().getDimension(R.dimen.profile_item_userinfo_height);
            viewTab = findViewById(R.id.tab_profile);

            viewWrite = findViewById(R.id.view_write_recipe_profile);

            View viewEmpty = viewTab.findViewById(R.id.view_empty_profile_tab);
            viewEmpty.setVisibility(View.GONE);

            if (MyData.getInstance().get_id().equals(user_id)) {
                View view = viewTab.findViewById(R.id.view_tab_other_recipe_profile);
                view.setVisibility(View.GONE);

                tvWroteRecipe = (TextView) viewTab.findViewById(R.id.text_wrote_my_recipe_profile);
                tvInterestRecipe = (TextView) viewTab.findViewById(R.id.text_interest_my_recipe_profile);
                tvOpenRecipe = (TextView) viewTab.findViewById(R.id.text_open_my_recipe_profile);

                viewWrite.setVisibility(View.VISIBLE);
                isVisibleWrite = true;

                FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvWroteRecipe, tvInterestRecipe, tvOpenRecipe);

                tvWroteRecipe.setOnClickListener(this);
                tvInterestRecipe.setOnClickListener(this);
                tvOpenRecipe.setOnClickListener(this);

                viewWrite.setOnClickListener(this);

            } else {
                View view = viewTab.findViewById(R.id.view_tab_my_recipe_profile);
                view.setVisibility(View.GONE);

                tvWroteRecipe = (TextView) viewTab.findViewById(R.id.text_wrote_other_recipe_profile);
                tvInterestRecipe = (TextView) viewTab.findViewById(R.id.text_interest_other_recipe_profile);

                viewWrite.setVisibility(View.GONE);
                isVisibleWrite = false;

                FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_NOMAL, tvWroteRecipe, tvInterestRecipe);

                tvWroteRecipe.setOnClickListener(this);
                tvInterestRecipe.setOnClickListener(this);

            }

            viewTab.setVisibility(View.GONE);

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_profile);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recView, int dx, int dy) {
                    super.onScrolled(recView, dx, dy);

                    View view = mGridManager.getChildAt(0);

                    if (view.getHeight() == userInfoHeight) {
                        if (viewTab.getVisibility() == View.VISIBLE)
                            viewTab.setVisibility(View.GONE);

                    } else {
                        if (viewTab.getVisibility() == View.GONE)
                            viewTab.setVisibility(View.VISIBLE);
                    }

                    if (isVisibleWrite) {
                        float y = writeY + (dy / 2);
                        if (y > MAX_SCROLL_Y) {
                            writeY = MAX_SCROLL_Y;
                        } else if (y < 0) {
                            writeY = 0;
                        } else {
                            writeY = y;
                        }

                        viewWrite.setTranslationY(writeY);
                    }
                }
            });
            mGridManager = new GridLayoutManager(this, 2);
            mGridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0 || position == 1) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            });
            recyclerView.setLayoutManager(mGridManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

            mAdapter = new ProfileAdapter(this);

//            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
//            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
//            scaleAdapter.setFirstOnly(false);
//            scaleAdapter.setDuration(500);
//            scaleAdapter.setInterpolator(new OvershootInterpolator());

            recyclerView.setAdapter(mAdapter);

            initData();

            if (savedInstanceState != null) {
                filename = savedInstanceState.getString(FILENAME);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FILENAME, filename);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        setResult(Activity.RESULT_OK);
        super.finish();
    }

    public void initData() {

        final LoadingDialogFragment dialog = new LoadingDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), LoadingDialogFragment.DIALOG_LOADING);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NetworkManager.getInstance().getUser(ProfileActivity.this, user_id, new NetworkManager.OnResultListener<UserPopResult>() {
                    @Override
                    public void onSuccess(UserPopResult data) {
                        if (data.result.equals(CooxingConstant.SUCCESS)) {
                            userVO = data.data;
                            if (user_id.equals(MyData.getInstance().get_id())) {
                                MyData.getInstance().setMyData(data.data);
                            }
                            setAcitionBarTitle(userVO.getNickname());
                            reverseList();

                            switch (currentTabPosition) {
                                case TAB_WROTE_RECIPE: {
                                    String message = emptyMessage(userVO.getRecipes().size(), TAB_WROTE_RECIPE);

                                    initProfileAndTab(message);
                                    onClickWroteRecipes();
                                    break;
                                }
                                case TAB_BOOKMARK_RECIPE: {
                                    String message = emptyMessage(userVO.getBookmarks().size(), TAB_BOOKMARK_RECIPE);

                                    initProfileAndTab(message);
                                    onClickInterestRecipes();
                                    break;
                                }
                                case TAB_OPENED_RECIPE: {
                                    String message = emptyMessage(userVO.getHistory().size(), TAB_OPENED_RECIPE);

                                    initProfileAndTab(message);
                                    onClickOpenedRecipes();
                                    break;
                                }
                            }


                        } else if (data.result.equals(CooxingConstant.FAIL)) {
                            Toast.makeText(ProfileActivity.this, data.message, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(ProfileActivity.this, code + "", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        }, 200);

    }

    private void setAcitionBarTitle(String nickname) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(nickname + " " + getString(R.string.profile_title));
        }
    }

    private void initProfileAndTab(String message) {
        mAdapter.setUser_id(userVO.get_id());

        ArrayList<ProfileData> list = new ArrayList<ProfileData>();

        ProfileData profile = new ProfileData();
        profile.setType(TYPE_USER);
        profile.setUser(userVO);

        list.add(profile);

        ProfileData tab = new ProfileData();
        tab.setType(TYPE_TAB);
        tab.setMessage(message);

        list.add(tab);

        mAdapter.initProfileAndTab(list);
    }

    private void reverseList() {
        Collections.reverse(userVO.getRecipes());
        Collections.reverse(userVO.getBookmarks());
        Collections.reverse(userVO.getHistory());
    }

    private String emptyMessage(int size, int type) {
        String message;
        if (type == TAB_WROTE_RECIPE) {
            message = getString(R.string.profile_message_no_wrote_recipe);
        } else if (type == TAB_BOOKMARK_RECIPE) {
            message = getString(R.string.profile_message_no_bookmark_recipe);
        } else if (type == TAB_OPENED_RECIPE) {
            message = getString(R.string.profile_message_no_opened_recipe);
        } else {
            return null;
        }

        return size > 0 ? NOT_EMPTY : message;
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

    public void onClickUserList(int type) {
        Intent intent = new Intent(this, UserListActivity.class);
        intent.putExtra(UserListActivity.EXTRA_TYPE, type);

        intent.putExtra(UserListActivity.EXTRA_FOLLOWING_LIST, userVO.getFollowing());
        intent.putExtra(UserListActivity.EXTRA_FOLLOWER_LIST, userVO.getFollower());

        startActivityForResult(intent, UserListActivity.USER_LIST_REQUEST_CODE);
    }


    public void onClickWroteRecipes() {
        currentTabPosition = TAB_WROTE_RECIPE;

        ArrayList<RecipeVO> list = userVO.getRecipes();
        String message = emptyMessage(list.size(), TAB_WROTE_RECIPE);

        addRecipes(list, message);
    }

    public void onClickInterestRecipes() {
        currentTabPosition = TAB_BOOKMARK_RECIPE;

        ArrayList<RecipeVO> list = userVO.getBookmarks();
        String message = emptyMessage(list.size(), TAB_BOOKMARK_RECIPE);

        addRecipes(list, message);
    }

    public void onClickOpenedRecipes() {
        currentTabPosition = TAB_OPENED_RECIPE;

        ArrayList<RecipeVO> list = userVO.getHistory();
        String message = emptyMessage(list.size(), TAB_OPENED_RECIPE);

        addRecipes(list, message);
    }

    private void addRecipes(ArrayList<RecipeVO> recipes, String message) {
        drawTabs(currentTabPosition);
        ArrayList<ProfileData> list = new ArrayList<ProfileData>();
        int size = recipes.size();
        for (int i = 0; i < size; i++) {
            ProfileData pd = new ProfileData();
            pd.setType(TYPE_RECIPE);
            pd.setRecipe(recipes.get(i));

            list.add(pd);
        }

        mAdapter.addRecipes(list, message);
    }

    private void drawTabs(int position) {
        if (MyData.getInstance().get_id().equals(user_id)) {
            switch (position) {
                case TAB_WROTE_RECIPE: {
                    tvWroteRecipe.setBackgroundResource(R.drawable.background_tab_focused_left);
                    tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_center);
                    tvOpenRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_right);

                    tvWroteRecipe.setTextColor(getResources().getColor(R.color.tab_selected_text));
                    tvInterestRecipe.setTextColor(getResources().getColor(R.color.tab_unselected_text));
                    tvOpenRecipe.setTextColor(getResources().getColor(R.color.tab_unselected_text));

                    break;
                }
                case TAB_BOOKMARK_RECIPE: {
                    tvWroteRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_left);
                    tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_focused_center);
                    tvOpenRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_right);

                    tvWroteRecipe.setTextColor(getResources().getColor(R.color.tab_unselected_text));
                    tvInterestRecipe.setTextColor(getResources().getColor(R.color.tab_selected_text));
                    tvOpenRecipe.setTextColor(getResources().getColor(R.color.tab_unselected_text));

                    break;
                }
                case TAB_OPENED_RECIPE: {
                    tvWroteRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_left);
                    tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_center);
                    tvOpenRecipe.setBackgroundResource(R.drawable.background_tab_focused_right);

                    tvWroteRecipe.setTextColor(getResources().getColor(R.color.tab_unselected_text));
                    tvInterestRecipe.setTextColor(getResources().getColor(R.color.tab_unselected_text));
                    tvOpenRecipe.setTextColor(getResources().getColor(R.color.tab_selected_text));
                    break;
                }
            }
        } else {
            switch (position) {
                case TAB_WROTE_RECIPE: {
                    tvWroteRecipe.setBackgroundResource(R.drawable.background_tab_focused_left);
                    tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_right);

                    tvWroteRecipe.setTextColor(getResources().getColor(R.color.tab_selected_text));
                    tvInterestRecipe.setTextColor(getResources().getColor(R.color.tab_unselected_text));

                    break;
                }
                case TAB_BOOKMARK_RECIPE: {
                    tvWroteRecipe.setBackgroundResource(R.drawable.background_tab_unfocused_left);
                    tvInterestRecipe.setBackgroundResource(R.drawable.background_tab_focused_right);

                    tvWroteRecipe.setTextColor(getResources().getColor(R.color.tab_unselected_text));
                    tvInterestRecipe.setTextColor(getResources().getColor(R.color.tab_selected_text));

                    break;
                }
            }
        }
    }

    public void onClickRecipe(String id) {
        Intent intent = new Intent(ProfileActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.RECIPE_ID, id);
        startActivity(intent);
    }

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    public static final String FILENAME = "filename";
    public String filename;

    public void onClickProfileChange() {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };


        new AlertDialog.Builder(ProfileActivity.this)
                .setTitle("사진").setMessage("등록 방법 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener).show();
    }

    private Uri mImageCaptureUri;

    private void doTakePhotoAction() {
        /*
		 * 참고 해볼곳 http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
		 * http://stackoverflow
		 * .com/questions/1050297/how-to-get-the-url-of-the-captured-image
		 * http://www.damonkohler.com/2009/02/android-recipes.html
		 * http://www.firstclown.us/tag/android/
		 */

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
        // intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    public void saveBitmapToFileCache(Bitmap bitmap, String strFilePath, String filename) {
        File file = new File(strFilePath);

        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }

        File fileCacheItem = new File(strFilePath + "/" + filename);
        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            Log.i(TAG, "filesize : " + fileCacheItem.length() / (long) 1024 + "KB");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void applyImage() {
        final LoadingDialogFragment dialog = new LoadingDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), LoadingDialogFragment.DIALOG_LOADING);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (filename != null && filename.length() > 0) {
                    NetworkManager.getInstance().registerProfileImage(ProfileActivity.this, filename, new NetworkManager.OnResultListener<StringResult>() {
                        @Override
                        public void onSuccess(StringResult data) {
                            if (data.result.equals(CooxingConstant.SUCCESS)) {
                                dialog.dismiss();
                                initData();

                                File f = new File(filename);
                                if (f.exists()) {
                                    f.delete();
                                }

                            } else if (data.result.equals(CooxingConstant.FAIL)) {
                                Toast.makeText(ProfileActivity.this, data.message, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(ProfileActivity.this, code + "", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.profile_message_invalid_file_path), Toast.LENGTH_SHORT).show();
                }
            }
        }, 300);
    }


    public int getCurrentTabPosition() {
        return currentTabPosition;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_wrote_my_recipe_profile: {
                onClickWroteRecipes();
                break;
            }
            case R.id.text_interest_my_recipe_profile: {
                onClickInterestRecipes();
                break;
            }
            case R.id.text_open_my_recipe_profile: {
                onClickOpenedRecipes();
                break;
            }
            case R.id.text_wrote_other_recipe_profile: {
                onClickWroteRecipes();
                break;
            }
            case R.id.text_interest_other_recipe_profile: {
                onClickInterestRecipes();
                break;
            }
            case R.id.view_write_recipe_profile: {
                Intent intent = new Intent(ProfileActivity.this, WriteActivity.class);
                startActivityForResult(intent, WriteActivity.WRITE_REQUEST_CODE);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UserListActivity.USER_LIST_REQUEST_CODE) {
                initData();

            } else if (requestCode == WriteActivity.WRITE_REQUEST_CODE) {
                initData();

            } else if (requestCode == CROP_FROM_CAMERA) {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
//                    ivPicture.setImageBitmap(photo);

                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String fileName = String.valueOf(System.currentTimeMillis() / 1000) + ".jpg";
                    filename = filePath + "/" + fileName;
                    saveBitmapToFileCache(photo, filePath, fileName);
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    Log.i("ProfileActivity", "FILE DELETE : " + f.delete());
                }

                applyImage();

            } else if (requestCode == PICK_FROM_ALBUM) {
                mImageCaptureUri = data.getData();

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 200);
                intent.putExtra("aspectY", 200);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

            } else if (requestCode == PICK_FROM_CAMERA) {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 200);
                intent.putExtra("aspectY", 200);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);
            }

        }
    }
}
