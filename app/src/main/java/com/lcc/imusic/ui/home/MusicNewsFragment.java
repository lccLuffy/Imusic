package com.lcc.imusic.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.base.fragment.AttachFragment;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class MusicNewsFragment extends AttachFragment {
    @Bind(R.id.viewPage)
    ViewPager viewPager;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_viewpage;
    }

    @Override
    public String toString() {
        return "音乐资讯";
    }

}
