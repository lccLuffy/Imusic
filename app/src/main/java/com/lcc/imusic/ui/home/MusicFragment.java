package com.lcc.imusic.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lcc.imusic.R;
import com.lcc.imusic.TestFragment;
import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.base.fragment.AttachFragment;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class MusicFragment extends AttachFragment {

    @Bind(R.id.viewPage)
    ViewPager viewPager;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        FragmentAdapter fa = new FragmentAdapter(getFragmentManager(),
                /*new LocalMusicFragment(),*/
                new RemoteMusicFragment(),
                new TestFragment()
        );
        viewPager.setAdapter(fa);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_viewpage;
    }
}
