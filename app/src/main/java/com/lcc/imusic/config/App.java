package com.lcc.imusic.config;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.lcc.imusic.service.MusicPlayService;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.orhanobut.logger.Logger;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        BlockCanary.install(this, new BlockCanaryContext() {
            @Override
            public int getConfigBlockThreshold() {
                return 400;
            }
        }).start();
        app = this;
        Logger.init("main");
        DrawerImageLoader.init(new ImageLoader());
        /*setTheme(R.style.AppThemeNight);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);*/
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        Intent intent = new Intent(this, MusicPlayService.class);
        startService(intent);
    }

    public static App getApp() {
        return app;
    }

    private class ImageLoader extends AbstractDrawerImageLoader {
        @Override
        public void set(ImageView imageView, Uri uri, Drawable placeholder) {
            Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
        }

        @Override
        public void cancel(ImageView imageView) {
            Glide.clear(imageView);
        }
    }
}
