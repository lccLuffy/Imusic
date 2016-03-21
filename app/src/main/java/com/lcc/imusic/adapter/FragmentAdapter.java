package com.lcc.imusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm, Fragment... fs) {
        super(fm);
        fragments = new ArrayList<>(2);
        if (fs != null) {
            fragments.addAll(Arrays.asList(fs));
        }


    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).toString();
    }
}
