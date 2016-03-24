package com.lcc.imusic.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.base.activity.UserActivity;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class UserCenterActivity extends UserActivity {

    @Bind(R.id.viewPage)
    ViewPager viewPager;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAvatar("http://upload.jianshu.io/users/upload_avatars/1438934/e9fe359cbaf2.jpeg");
        setUsername("lcc_luffy");
        FragmentAdapter fa = new FragmentAdapter(getSupportFragmentManager(),
                new UserCollectMusicFragment(),
                new UserHistroyMusicFragment()
        );
        viewPager.setAdapter(fa);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_center;
    }
}
