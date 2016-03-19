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
import com.lcc.imusic.base.MusicBindActivity;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.LocalMusicProvider;
import com.lcc.imusic.model.MusicProvider;
import com.lcc.imusic.service.MusicPlayService;
import com.lcc.imusic.ui.MusicPlayerActivity;
import com.lcc.imusic.wiget.MusicListDialog;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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

    MusicProvider musicProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        musicProvider = LocalMusicProvider.getMusicProvider(this);
        accountDelegate = new AccountDelegate(this,toolbar,this);
        accountDelegate.init();

        playBar_wrap.setOnClickListener(this);
        playBarPlayToggle.setOnCheckedChangeListener(this);
        playBarPlayNext.setOnClickListener(this);
        playBarPlayList.setOnClickListener(this);
        accountDelegate.setAvatar("http://upload.jianshu.io/users/upload_avatars/1438934/e9fe359cbaf2.jpeg");

        toolbar.inflateMenu(R.menu.toolbar_menu);

    }

    private void init()
    {
        actionBar.setDisplayShowTitleEnabled(false);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        if(!MusicPlayService.HAS_STATED)
        {
            Intent intent = new Intent(this, MusicPlayService.class);
            startService(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /*MenuItem menuItem = menu.add("Music");
        menuItem.setIcon(R.mipmap.actionbar_music_selected);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            finish();
            stopService(new Intent(this, MusicPlayService.class));
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
    protected void onResume() {
        super.onResume();
        if(isBind())
        {
            setCurrentMusicItem(musicProvider.getPlayingMusic());
        }
    }

    @Override
    public boolean onDrawerMenuSelected(View view, int position, IDrawerItem drawerItem) {
        return false;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private MusicListDialog musicListDialog;
    private void checkDialogIsNull() {
        if(musicListDialog == null)
        {
            musicListDialog = new MusicListDialog(this);

            musicListDialog.init().getAdapter().initData(musicProvider.provideMusics());

            musicListDialog.getAdapter().setOnItemClickListener(new NiceAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    playMusic(position);
                }
            });
        }
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
                break;
            case R.id.playBar_playList:
                checkDialogIsNull();
                musicListDialog.show();
                break;
        }
    }

    @Override
    protected void onBind(MusicPlayService.MusicServiceBind musicServiceBind) {
        setCurrentMusicItem(musicProvider.getPlayingMusic());
        if(musicReadyListener == null)
            musicReadyListener = new MusicReadyListener();
        musicServiceBind.addMusicReadyListener(musicReadyListener);
    }

    @Override
    protected void unBind(MusicPlayService.MusicServiceBind musicServiceBind) {
        if(musicReadyListener != null)
        {
            musicServiceBind.removeMusicReadyListener(musicReadyListener);
            musicReadyListener = null;
        }
    }

    public void playMusic(int id) {
        musicServiceBind.playMusic(id);
    }

    private void setCurrentMusicItem(MusicItem musicItem)
    {
        if(musicItem != null)
        {
            playBarTitle.setText(musicItem.title);
            playBarSubtitle.setText(musicItem.artist);
            if(musicServiceBind != null && musicServiceBind.isPlaying())
            {
                playBarPlayToggle.setChecked(true);
            }
            else
            {
                playBarPlayToggle.setChecked(false);
            }
        }
    }

    MusicPlayService.MusicReadyListener musicReadyListener;

    private class MusicReadyListener implements MusicPlayService.MusicReadyListener
    {
        @Override
        public void onMusicReady(MusicItem musicItem) {
            setCurrentMusicItem(musicItem);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(musicServiceBind != null)
        {
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
}
