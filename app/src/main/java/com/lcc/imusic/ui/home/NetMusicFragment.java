package com.lcc.imusic.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.base.AttachFragment;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class NetMusicFragment extends AttachFragment {
    @Bind(R.id.viewPage)
    ViewPager viewPager;


    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getFragmentManager(),
                ActivitiesFragment.newInstance(58451795),
                ActivitiesFragment.newInstance(1),
                ActivitiesFragment.newInstance(865656),
                ActivitiesFragment.newInstance(5845662),
                ActivitiesFragment.newInstance(865656)
        );
        viewPager.setAdapter(fragmentAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_viewpage;
    }

    @Override
    public String toString() {
        return "网络音乐";
    }

}
