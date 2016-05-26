package com.lcc.imusic.config;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.lcc.imusic.service.MusicPlayService;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.orhanobut.logger.Logger;
import com.orm.SugarContext;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        LeakCanary.install(this);
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        app = this;
        Logger.init("main");
        DrawerImageLoader.init(new ImageLoader());
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

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
    public class AppBlockCanaryContext extends BlockCanaryContext {
        // override to provide context like app qualifier, uid, network type, block threshold, log save path

        // this is default block threshold, you can set it by phone's performance
        @Override
        public int getConfigBlockThreshold() {
            return 500;
        }

        // if set true, notification will be shown, else only write log file
        @Override
        public boolean isNeedDisplay() {
            return true;
        }

        // path to save log file
        @Override
        public String getLogPath() {
            return "/blockcanary/performance";
        }
    }
}
