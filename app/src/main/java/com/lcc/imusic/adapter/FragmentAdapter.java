package com.lcc.imusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lcc.imusic.ui.home.ActivitiesFragment;
import com.lcc.imusic.ui.home.AuditionMusicFragment;
import com.lcc.imusic.ui.home.HotFansFragment;
import com.lcc.imusic.ui.home.HotMusicianFragment;
import com.lcc.imusic.ui.home.HotSingleFragment;
import com.lcc.imusic.ui.home.MusicNewsFragment;
import com.lcc.imusic.ui.home.RingtoneFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class FragmentAdapter extends FragmentPagerAdapter{
    List<Fragment> fragments;
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>(7);
        fragments.add(new HotMusicianFragment());
        fragments.add(new HotSingleFragment());
        fragments.add(new MusicNewsFragment());
        fragments.add(new RingtoneFragment());
        fragments.add(new AuditionMusicFragment());
        fragments.add(new HotFansFragment());
        fragments.add(new ActivitiesFragment());
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
