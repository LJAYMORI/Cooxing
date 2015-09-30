package com.ljaymori.cooxing.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ljaymori.cooxing.detail.fragment.DetailParentFragment;

import java.util.ArrayList;

public class DetailPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<DetailParentFragment> items = new ArrayList<DetailParentFragment>();

    public DetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void initPages(ArrayList<DetailParentFragment> list) {
        items.clear();
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

    public ArrayList<DetailParentFragment> getItems() {
        return items;
    }
}
