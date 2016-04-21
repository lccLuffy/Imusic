package com.lcc.imusic.ui;

import android.os.Bundle;

import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }
}
