package com.ljaymori.cooxing.write.album;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.write.descriptsion.WriteDescFragment;
import com.ljaymori.cooxing.write.image.WriteImageActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class WriteAlbumFolderActivity extends ActionBarActivity {

    public static final int REQUEST_CODE = 111;
    public static final String IMAGE_PATHS = "image_paths";
    public static final String ALBUM_NAME = "album_name";

    private TextView tvNotice;

    private HashMap<String, ArrayList<MediaData>> hashMap = new HashMap<String, ArrayList<MediaData>>();
    private ArrayList<MediaData> allList = new ArrayList<MediaData>();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AlbumFolderAdapter mAdapter;

    private View viewProgress;

    private int addedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_album_folder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_left_arrow);

        addedCount = getIntent().getIntExtra(WriteDescFragment.ITEM_COUNT, 0);

        tvNotice = (TextView) findViewById(R.id.text_notice_write_album_folder);

        viewProgress = findViewById(R.id.view_progress_write_album_folder);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_write_album_folder);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new AlbumFolderAdapter(this);

        initData();
        setFonts();
    }


    private void initData() {
        new MediaLoadAsync().execute();
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvNotice);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class MediaLoadAsync extends AsyncTask<Void, Void, HashMap<String, ArrayList<MediaData>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected HashMap<String, ArrayList<MediaData>> doInBackground(Void... params) {
            HashMap<String, ArrayList<MediaData>> hm = new HashMap<String, ArrayList<MediaData>>();

            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] imageProjection = {
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_TAKEN
            };

            Cursor imageCursor = getContentResolver().query(imageUri, imageProjection, null,
                    null, null);

            int image_column_index_bucket = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int image_column_index_data = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int image_column_index_taken_date = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);


            imageCursor.moveToFirst();
            while (imageCursor.moveToNext()) {
                String albumName = imageCursor.getString(image_column_index_bucket);
                String path = imageCursor.getString(image_column_index_data);
                String date = imageCursor.getString(image_column_index_taken_date);

                MediaData md = new MediaData();
                md.setAlbumName(albumName);
                md.setFilePath(path);
                md.setTakenDate(date);

                ArrayList<MediaData> list = hm.get(albumName);
                if (list == null) {
                    list = new ArrayList<MediaData>();
                }
                list.add(0, md);

                hm.put(albumName, list);
                allList.add(0, md);
            }

            return hm;
        }

        @Override
        protected void onPostExecute(HashMap<String, ArrayList<MediaData>> hm) {
            ArrayList<AlbumFolderItemData> albumList = new ArrayList<AlbumFolderItemData>();

            AlbumFolderItemData all = new AlbumFolderItemData();
            all.setType(AlbumFolderAdapter.TYPE_ALL);
            all.setAlbumName(getString(R.string.write_album_folder_all_list));
            all.setThumbnail(allList.get(0).getFilePath());

            albumList.add(all);

            for (String albumName : hm.keySet()) {
                ArrayList<MediaData> list = hm.get(albumName);

                if (list != null && list.size() > 0) {
                    AlbumFolderItemData ad = new AlbumFolderItemData();
                    MediaData md = list.get(0);

                    ad.setType(AlbumFolderAdapter.TYPE_OTHER);
                    ad.setAlbumName(albumName);
                    ad.setThumbnail(md.getFilePath());

                    albumList.add(ad);
                }
            }

            hashMap.putAll(hm);
            mAdapter.addAll(albumList);

            recyclerView.setAdapter(mAdapter);
        }


    }

    public void showAlbum(int albumType, String albumName) {
        ArrayList<MediaData> list = new ArrayList<MediaData>();
        if (albumType == AlbumFolderAdapter.TYPE_ALL) {
            list.addAll(allList);
        } else {
            list.addAll(hashMap.get(albumName));
        }

        if (list.size() > 0) {
            Intent intent = new Intent(WriteAlbumFolderActivity.this, WriteImageActivity.class);
            intent.putExtra(ALBUM_NAME, albumName);
            intent.putExtra(IMAGE_PATHS, list);
            intent.putExtra(WriteDescFragment.ITEM_COUNT, addedCount);
            startActivityForResult(intent, REQUEST_CODE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }
}
