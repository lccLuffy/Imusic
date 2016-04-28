package com.lcc.imusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.lcc.imusic.R;
import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.base.activity.AccountDelegate;
import com.lcc.imusic.base.activity.PlayBarActivity;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.manager.UserManager;
import com.lcc.imusic.service.MusicPlayService;
import com.lcc.imusic.ui.home.MusicFragment;
import com.lcc.imusic.ui.home.MusicNewsFragment;
import com.lcc.imusic.ui.home.MusicianListFragment;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends PlayBarActivity implements AccountDelegate.AccountListener {
    @Bind(R.id.tabLayout)
    SmartTabLayout tabLayout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private AccountDelegate accountDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        accountDelegate = new AccountDelegate(this, toolbar, this);
        accountDelegate.init();
        accountDelegate.setAvatar("http://upload.jianshu.io/users/upload_avatars/1438934/e9fe359cbaf2.jpeg");

        NetManager_.API().me().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                toast(response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                toast(t.toString());
            }
        });
    }

    private void init() {
        actionBar.setDisplayShowTitleEnabled(false);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),
                new MusicianListFragment(), new MusicFragment(), new MusicNewsFragment());
        viewPager.setAdapter(adapter);
        tabLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                ImageView imageView = (ImageView) getLayoutInflater()
                        .inflate(R.layout.tab_icon_imageview, container, false);
                switch (position) {
                    case 0:
                        imageView.setImageResource(R.mipmap.actionbar_friends_selected);
                        break;
                    case 1:
                        imageView.setImageResource(R.mipmap.actionbar_music_selected);
                        break;
                    case 2:
                        imageView.setImageResource(R.mipmap.actionbar_discover_selected);
                        break;
                }
                return imageView;
            }
        });
        tabLayout.setViewPager(viewPager);
        if (!MusicPlayService.HAS_STATED) {
            Intent intent = new Intent(this, MusicPlayService.class);
            startService(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        return true;
    }

    @NonNull
    @Override
    public List<IDrawerItem> onCreateMenuItem() {
        List<IDrawerItem> list = new ArrayList<>();
        list.add(new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home));
        list.add(new PrimaryDrawerItem().withName("下载管理").withIcon(FontAwesome.Icon.faw_cloud_download));
        if (UserManager.isLogin()) {
            list.add(new PrimaryDrawerItem().withName("退出登录").withIcon(FontAwesome.Icon.faw_sign_out));
        }
        list.add(new PrimaryDrawerItem().withName("退出").withIcon(FontAwesome.Icon.faw_sign_out));
        return list;
    }

    @Override
    protected void onDestroy() {
        accountDelegate.destroy();
        accountDelegate = null;
        Logger.i("onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        if (UserManager.isLogin()) {
            switch (position) {
                case 2:
                    startActivity(new Intent(this, DownLoadActivity.class));
                    break;
                case 3:
                    UserManager.logout();
                    accountDelegate.close();
                    toast("已退出登录");
                    break;
                case 4:
                    finish();
                    stopService(new Intent(this, MusicPlayService.class));
                    break;
            }
        } else {
            switch (position) {
                case 2:
                    startActivity(new Intent(this, DownLoadActivity.class));
                    break;
                case 3:
                    finish();
                    stopService(new Intent(this, MusicPlayService.class));
                    break;
            }
        }

        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
