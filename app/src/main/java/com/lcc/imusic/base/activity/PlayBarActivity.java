package com.lcc.imusic.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lcc.imusic.R;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.CurrentMusicProvider;
import com.lcc.imusic.model.CurrentMusicProviderImpl;
import com.lcc.imusic.service.MusicPlayService;
import com.lcc.imusic.ui.MusicPlayerActivity;
import com.lcc.imusic.wiget.MusicListDialog;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public abstract class PlayBarActivity extends MusicProgressCallActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
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

    protected CurrentMusicProvider currentMusicProvider;

    private MusicListDialog musicListDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentMusicProvider = CurrentMusicProviderImpl.getMusicProvider();

        /*if (currentMusicProvider.provideMusics().isEmpty()) {
            currentMusicProvider.copyToMe(LocalMusicProvider.getMusicProvider(this).provideMusics());
        }*/
        progressBar.setProgress(0);

        playBar_wrap.setOnClickListener(this);
        playBarPlayToggle.setOnCheckedChangeListener(this);
        playBarPlayNext.setOnClickListener(this);
        playBarPlayList.setOnClickListener(this);

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
    protected void onResume() {
        super.onResume();
        if (isBind()) {
            setCurrentMusicItem(currentMusicProvider.getPlayingMusic());
            playBarPlayToggle.setChecked(musicServiceBind.isPlaying());
        }
    }

    @Override
    protected void onDestroy() {
        if (musicListDialog != null) {
            musicListDialog.getAdapter().onDestroy();
        }
        currentMusicProvider = null;
        super.onDestroy();
    }

    private void checkDialogIsNull() {
        if (musicListDialog == null) {
            musicListDialog = new MusicListDialog(this);

            musicListDialog.init().getAdapter().setData(currentMusicProvider.provideMusics());
            musicListDialog.getAdapter().setCurrentPlayingIndex(currentMusicProvider.getPlayingMusicIndex());
            musicListDialog.getAdapter().setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Logger.i("position is :" + position);
                    playMusic(position);
                }
            });
        }
    }

    @Override
    protected void onBind(MusicPlayService.MusicServiceBind musicServiceBind) {
        setCurrentMusicItem(currentMusicProvider.getPlayingMusic());
        playBarPlayToggle.setChecked(musicServiceBind.isPlaying());
    }

    public void playMusic(int id) {
        Intent i = new Intent(this, MusicPlayService.class);
        i.putExtra("index", id);
        i.setAction(MusicPlayService.ACTION_PLAY_MUSIC_AT_INDEX);
        startService(i);
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
        if (progressBar != null)
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
        Logger.i("index change to:" + index);
        if (musicListDialog != null) {
            musicListDialog.getAdapter().playingIndexChangeTo(index);
        }
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
            if (currentMusicProvider.provideMusics().isEmpty()) {
                toast("先选择播放列表播放吧");
                return;
            }
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
            Logger.i("musicItems size:" + musicItems.size() + ",index:" + currentMusicProvider.getPlayingMusicIndex());

            musicListDialog.getAdapter().setData(musicItems, currentMusicProvider.getPlayingMusicIndex());
        }
    }
}
