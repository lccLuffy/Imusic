package com.lcc.imusic.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicianDetailAdapter;
import com.lcc.imusic.base.activity.UserActivity;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class MusicianDetailActivity extends UserActivity {

    @Bind(R.id.viewPage)
    ViewPager viewPager;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getIntent().getLongExtra("id", 1);

        MusicianDetailAdapter musicDetailAdapter = new MusicianDetailAdapter(getSupportFragmentManager());
        viewPager.setAdapter(musicDetailAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setAvatar("http://img.xiami.net/images/artistlogo/41/13739635501641.jpg");
        setUsername("庄心妍");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_album_detail;
    }


}
