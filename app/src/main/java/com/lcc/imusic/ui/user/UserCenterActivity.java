package com.lcc.imusic.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lcc.imusic.R;
import com.lcc.imusic.base.BaseActivity;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class UserCenterActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_center;
    }
}
