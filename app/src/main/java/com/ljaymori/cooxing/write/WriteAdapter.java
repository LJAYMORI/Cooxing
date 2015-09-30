package com.ljaymori.cooxing.write;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class WriteAdapter extends FragmentStatePagerAdapter {

    private ArrayList<WriteParentFragment> items = new ArrayList<WriteParentFragment>();

    public WriteAdapter(FragmentManager fm) {
        super(fm);
    }

    public void initPages(ArrayList<WriteParentFragment> list) {
        items.addAll(list);
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
