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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcc.imusic.adapter.FragmentAdapter;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.base.AccountDelegate;
import com.lcc.imusic.base.MusicProgressCallActivity;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.CurrentMusicProvider;
import com.lcc.imusic.model.CurrentMusicProviderImpl;
import com.lcc.imusic.model.LocalMusicProvider;
import com.lcc.imusic.service.DownloadService;
import com.lcc.imusic.service.MusicPlayService;
import com.lcc.imusic.ui.MusicPlayerActivity;
import com.lcc.imusic.ui.home.HotMusicianFragment;
import com.lcc.imusic.ui.home.NetMusicFragment;
import com.lcc.imusic.wiget.MusicListDialog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends MusicProgressCallActivity implements AccountDelegate.AccountListener
        , View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;


    @Bind(R.id.playBar_wrap)
    View playBar_wrap;

    @Bind(R.id.playBar_cover)
    ImageView playBarCover;

    @Bind(R.id.progress)
    ProgressBar progressBar;

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

    CurrentMusicProvider currentMusicProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        currentMusicProvider = CurrentMusicProviderImpl.getMusicProvider();

        if (currentMusicProvider.provideMusics().isEmpty()) {
            currentMusicProvider.copyToMe(LocalMusicProvider.getMusicProvider(this).provideMusics());
        }
        accountDelegate = new AccountDelegate(this, toolbar, this);
        accountDelegate.init();

        playBar_wrap.setOnClickListener(this);
        playBarPlayToggle.setOnCheckedChangeListener(this);
        playBarPlayNext.setOnClickListener(this);
        playBarPlayList.setOnClickListener(this);
        accountDelegate.setAvatar("http://upload.jianshu.io/users/upload_avatars/1438934/e9fe359cbaf2.jpeg");
        setCurrentMusicItem(currentMusicProvider.getPlayingMusic());
        /*download();*/
    }

    private void download() {
        startService(new Intent(this, DownloadService.class));
    }


    private void init() {
        actionBar.setDisplayShowTitleEnabled(false);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),
                new HotMusicianFragment(), new NetMusicFragment());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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
        accountDelegate.destroy();
        if (musicListDialog != null) {
            musicListDialog.getAdapter().onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isBind()) {
            setCurrentMusicItem(currentMusicProvider.getPlayingMusic());
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
        if (musicListDialog == null) {
            musicListDialog = new MusicListDialog(this);

            musicListDialog.init().getAdapter().setData(currentMusicProvider.provideMusics());
            musicListDialog.getAdapter().setCurrentPlayingIndex(currentMusicProvider.getPlayingMusicIndex());
            musicListDialog.getAdapter().setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    playMusic(position);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        setCurrentMusicItem(currentMusicProvider.getPlayingMusic());
        if (musicServiceBind.isPlaying()) {
            playBarPlayToggle.setChecked(musicServiceBind.isPlaying());
        }
    }

    public void playMusic(int id) {
        musicServiceBind.play(id);
    }

    private void setCurrentMusicItem(MusicItem musicItem) {
        if (musicItem != null) {
            playBarTitle.setText(musicItem.title);
            playBarSubtitle.setText(musicItem.artist);
            progressBar.setMax(musicItem.duration);
            Glide.with(this)
                    .load(musicItem.cover)
                    .placeholder(R.mipmap.placeholder_disk_play_song)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(playBarCover);

        }
    }

    @Override
    public void onProgress(int second) {
        progressBar.setProgress(second);
    }

    @Override
    public void onBuffering(int percent) {
        progressBar.setSecondaryProgress((int) (percent * 1.0f / 100 * progressBar.getMax()));
    }

    @Override
    public void onMusicReady(MusicItem musicItem) {
        setCurrentMusicItem(musicItem);
        progressBar.setMax(musicServiceBind.getTotalTime());
        playBarPlayToggle.setChecked(true);
    }

    @Override
    public void onPlayingIndexChange(int index) {
        if (musicListDialog != null)
            musicListDialog.getAdapter().playingIndexChangeTo(index);
    }

    @Override
    public void onMusicWillPlay(MusicItem musicItem) {
        setCurrentMusicItem(musicItem);
        playBarPlayToggle.setChecked(true);
        progressBar.setProgress(0);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (musicServiceBind != null) {
            if (isChecked) {
                musicServiceBind.startPlayOrResume();
            } else {
                musicServiceBind.pause();
            }
        }
    }

    @Override
    public void onCurrentPlayingListChange(@NonNull List<MusicItem> musicItems) {
        if (musicListDialog != null) {
            musicListDialog.getAdapter().setData(musicItems, currentMusicProvider.getPlayingMusicIndex());
        }
    }
}
