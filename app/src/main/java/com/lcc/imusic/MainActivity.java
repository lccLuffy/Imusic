package com.lcc.imusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.base.AccountDelegate;
import com.lcc.imusic.base.MusicBindActivity;
import com.lcc.imusic.ui.MusicPlayerActivity;
import com.lcc.imusic.ui.setting.SettingActivity;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends MusicBindActivity implements AccountDelegate.AccountListener {
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private AccountDelegate accountDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        accountDelegate = new AccountDelegate(this,toolbar,this);
        accountDelegate.init();

        accountDelegate.setAvatar("http://static.oschina.net/uploads/img/201304/17033908_N1hN.jpg");
    }

    private void init()
    {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public List<IDrawerItem> onCreateMenuItem() {
        List<IDrawerItem> list = new ArrayList<>();
        list.add(new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home));
        list.add(new PrimaryDrawerItem().withName("Music").withIcon(FontAwesome.Icon.faw_music));
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accountDelegate.destroy();
    }

    @Override
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        if(position == 2)
        {
            startActivity(new Intent(this, MusicPlayerActivity.class));
            return true;
        }
        return false;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

}
