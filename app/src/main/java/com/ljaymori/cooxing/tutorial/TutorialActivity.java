package com.ljaymori.cooxing.tutorial;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class TutorialActivity extends ActionBarActivity {

    public static final String KEY_IS_LAST_PAGE = "last_page";
    public static final String KEY_BACKGROUND_RESOURCE = "background_resource";
    public static final String KEY_SYMBOL_RESOURCE = "symbol_resource";
    public static final String KEY_POSITION = "position";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "desc";

    private ViewPager viewPager;
    private TutorialAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        initPages();

        viewPager = (ViewPager) findViewById(R.id.pager_tutorial);
        mAdapter = new TutorialAdapter(getSupportFragmentManager());
        mAdapter.setInitPages(initPages());

        viewPager.setAdapter(mAdapter);

        viewPager.setPageTransformer(false, new ParallaxPagerTransformer());
    }

    private ArrayList<TutorialFragment> initPages() {

        boolean[] lastPages = new boolean[] {
                false,
                false,
                false,
                false,
                true
        };

        int[] bgResources = new int[] {
                R.drawable.background_tutorial_1,
                R.drawable.background_tutorial_1,
                R.drawable.background_tutorial_1,
                R.drawable.background_tutorial_1,
                R.drawable.background_tutorial_1
        };

        int[] symbolResources = new int[] {
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher,
                R.drawable.ic_launcher
        };

        String[] titles = new String[] {
                getResources().getString(R.string.tuto_title_one),
                getResources().getString(R.string.tuto_title_two),
                getResources().getString(R.string.tuto_title_three),
                getResources().getString(R.string.tuto_title_four),
                getResources().getString(R.string.tuto_title_five)
        };

        String[] descs = new String[] {
                getResources().getString(R.string.tuto_desc_one),
                getResources().getString(R.string.tuto_desc_two),
                getResources().getString(R.string.tuto_desc_three),
                getResources().getString(R.string.tuto_desc_four),
                getResources().getString(R.string.tuto_desc_five)
        };


        ArrayList<TutorialFragment> list = new ArrayList<TutorialFragment>();
        for(int i=0; i<lastPages.length; i++) {
            Bundle args = new Bundle();
            args.putBoolean(KEY_IS_LAST_PAGE, lastPages[i]);
            args.putInt(KEY_POSITION, i+1);
            args.putInt(KEY_BACKGROUND_RESOURCE, bgResources[i]);
            args.putInt(KEY_SYMBOL_RESOURCE, symbolResources[i]);
            args.putString(KEY_TITLE, titles[i]);
            args.putString(KEY_DESCRIPTION, descs[i]);
            TutorialFragment f = new TutorialFragment();
            f.setArguments(args);
            list.add(f);
        }

        return list;
    }

}
