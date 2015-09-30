package com.ljaymori.cooxing.tutorial;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class TutorialAdapter extends FragmentStatePagerAdapter {

    private ArrayList<TutorialFragment> items = new ArrayList<TutorialFragment>();

    public TutorialAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setInitPages(ArrayList<TutorialFragment> list) {
        items.clear();
        items.addAll(list);
    }

    @Override
    public int getItemPosition(Object object) {
        return items.indexOf(object);
    }

    @Override
    public TutorialFragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
