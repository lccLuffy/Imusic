package com.lcc.imusic.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.base.activity.UserActivity;
import com.lcc.imusic.manager.UserManager;

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
        if (UserManager.isLogin()) {
            setAvatar(UserManager.avatar());
            setUsername(UserManager.username());
        }
        FragmentAdapter fa = new FragmentAdapter(getSupportFragmentManager(),
                new UserCollectMusicFragment(),
                new UserHistroyMusicFragment()
        );
        avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadAvatar();
            }
        });
        viewPager.setAdapter(fa);
        tabLayout.setupWithViewPager(viewPager);
    }

    private static int SELECT_PIC = 321;

    private void upLoadAvatar() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PIC && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                toast(uri.toString());

            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_center;
    }
}
