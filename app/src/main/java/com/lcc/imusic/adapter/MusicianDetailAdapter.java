package com.lcc.imusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lcc.imusic.ui.home.MusicianFansFragment;
import com.lcc.imusic.ui.home.MusicianMusicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class MusicianDetailAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;

    public MusicianDetailAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>(2);
        fragments.add(new MusicianMusicFragment());
        fragments.add(new MusicianFansFragment());
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