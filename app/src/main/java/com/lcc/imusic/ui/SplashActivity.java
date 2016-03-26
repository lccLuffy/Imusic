package com.lcc.imusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.model.LocalMusicProvider;

import butterknife.Bind;

public class SplashActivity extends BaseActivity {
    @Bind(R.id.splash_image)
    ImageView splash_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                task();
                handler.sendMessageDelayed(Message.obtain(), 1000);
            }
        }).start();
    }

    private void task() {
        LocalMusicProvider.getMusicProvider(SplashActivity.this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }
}
