package com.ljaymori.cooxing.write.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.write.album.MediaData;
import com.ljaymori.cooxing.write.album.WriteAlbumFolderActivity;
import com.ljaymori.cooxing.write.descriptsion.WriteDescFragment;

import java.util.ArrayList;

public class WriteImageActivity extends ActionBarActivity implements View.OnClickListener {

    private ArrayList<WriteImageItemData> list = new ArrayList<WriteImageItemData>();

    private TextView tvComplete;

    private TextView tvNotice;
    private TextView tvLimit;

    private TextView tvCount;
    private int addedCount;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private WriteImageItemAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_image);

        System.gc();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_left_arrow);
        String title = getIntent().getStringExtra(WriteAlbumFolderActivity.ALBUM_NAME);
        getSupportActionBar().setTitle(title);

        tvNotice = (TextView) findViewById(R.id.text_notice_write_image);
        tvLimit = (TextView) findViewById(R.id.text_limit_count_write_image);

        tvComplete = (TextView) findViewById(R.id.text_complete_write_image);
        tvComplete.setOnClickListener(this);

        tvCount = (TextView) findViewById(R.id.text_image_count_write_image);
        addedCount = getIntent().getIntExtra(WriteDescFragment.ITEM_COUNT, 0);
        changeSelectedCount(addedCount);

        ArrayList<MediaData> mediaList = (ArrayList<MediaData>) getIntent().getSerializableExtra(WriteAlbumFolderActivity.IMAGE_PATHS);
        Log.i("mediaList.size()", mediaList.size() + "");
        if (mediaList == null) {
//            Log.i("WriteImageActivity", "mediaList is null");
            Toast.makeText(this, "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;

        } else {
            for (MediaData md : mediaList) {
                WriteImageItemData wd = new WriteImageItemData();
                wd.setMediaData(md);

                list.add(wd);
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_write_image);
        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new WriteImageItemAdapter(this);

        recyclerView.setAdapter(mAdapter);

        setFonts();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mAdapter.addAll(list);
            }
        });
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvNotice, tvCount, tvLimit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_complete_write_image) {
            clickComplete();
            return true;

        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeSelectedCount(int count) {
        tvCount.setText(count + "");
    }

    public int getImageCountResource(int count) {
        int[] images = new int[]{
                R.drawable.ic_step_1,
                R.drawable.ic_step_2,
                R.drawable.ic_step_3,
                R.drawable.ic_step_4,
                R.drawable.ic_step_5,
                R.drawable.ic_step_6,
                R.drawable.ic_step_7,
                R.drawable.ic_step_8,
                R.drawable.ic_step_9,
                R.drawable.ic_step_10
        };

        return images[count - 1];
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_complete_write_image: {
                clickComplete();
                break;
            }
        }
    }

    private void clickComplete() {
        ArrayList<String> paths = new ArrayList<String>();
        ArrayList<Integer> indexList = mAdapter.getSelectedList();

        for (Integer index : indexList) {
            paths.add(list.get(index).getMediaData().getFilePath());
        }

        Intent data = new Intent();
        data.putExtra(WriteDescFragment.FILE_PATHS, paths);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public int getAddedCount() {
        return addedCount;
    }

}
