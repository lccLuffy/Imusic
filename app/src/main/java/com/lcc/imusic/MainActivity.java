package com.lcc.imusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.base.AccountDelegate;
import com.lcc.imusic.base.AttachFragment;
import com.lcc.imusic.base.MusicBindActivity;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.ui.MusicPlayerActivity;
import com.lcc.imusic.ui.setting.SettingActivity;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends MusicBindActivity implements AccountDelegate.AccountListener
        ,View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;


    @Bind(R.id.playBar_wrap)
    View playBar_wrap;

    @Bind(R.id.playBar_cover)
    ImageView playBarCover;

    @Bind(R.id.playBar_title)
    TextView playBarTitle;

    @Bind(R.id.playBar_subtitle)
    TextView playBarSubtitle;

    @Bind(R.id.playBar_playList)
    ImageView playBarPlayList;

    @Bind(R.id.playBar_playToggle)
    CheckBox playBarPlayToggle;

    @Bind(R.id.playBar_next)
    ImageView playBarPlayNext;

    private AccountDelegate accountDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        accountDelegate = new AccountDelegate(this,toolbar,this);
        accountDelegate.init();
        playBar_wrap.setOnClickListener(this);
        playBarPlayToggle.setOnCheckedChangeListener(this);
        playBarPlayNext.setOnClickListener(this);
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
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
        Logger.i("onAttachFragment");
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
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accountDelegate.destroy();
    }

    @Override
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        return false;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.playBar_wrap:
                startActivity(new Intent(this, MusicPlayerActivity.class));
                break;
            case R.id.playBar_next:
                musicServiceBind.next();
                setCurrentMusicItem(musicServiceBind.getPlayingMusic());
                break;
        }
    }


    public void playMusic(int id) {
        musicServiceBind.playMusic(id);
        playBarPlayToggle.setChecked(true);
        setCurrentMusicItem(musicServiceBind.getPlayingMusic());
    }

    private void setCurrentMusicItem(MusicItem musicItem)
    {
        playBarTitle.setText(musicItem.title);
        playBarSubtitle.setText(musicItem.artist);
    }

    private AttachFragment.OnBindActivity onBindActivity;

    public void setOnBindActivity(AttachFragment.OnBindActivity onBindActivity){
        this.onBindActivity = onBindActivity;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            musicServiceBind.start();
        }
        else
        {
            musicServiceBind.pause();
        }
    }
}
