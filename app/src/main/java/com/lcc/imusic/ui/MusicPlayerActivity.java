package com.lcc.imusic.ui;

import android.os.Bundle;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.base.MusicProgressCallActivity;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.CurrentMusicProvide;
import com.lcc.imusic.model.CurrentMusicProviderImpl;
import com.lcc.imusic.musicplayer.MusicPlayerView;
import com.lcc.imusic.service.MusicPlayService;
import com.lcc.imusic.wiget.MusicListDialog;
import com.lcc.imusic.wiget.StateImageView;

import butterknife.Bind;

public class MusicPlayerActivity extends MusicProgressCallActivity {
    @Bind(R.id.musicPlayer)
    MusicPlayerView musicPlayerView;

    private CurrentMusicProvide musicProvider;

    private MusicListDialog musicListDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicProvider = CurrentMusicProviderImpl.getMusicProvider(this);
        setCurrentMusicItem(musicProvider.getPlayingMusic());
    }

    @Override
    protected void onBind(final MusicPlayService.MusicServiceBind musicServiceBind) {
        musicPlayerView.setPlayBtnState(musicServiceBind.isPlaying());
        musicPlayerView.setPlayType(musicServiceBind.getPlayType());
        musicPlayerView.setMusicPlayerCallBack(new MusicPlayerCallBackImpl());
    }

    private boolean canAutoProgress = true;

    @Override
    public void onMusicWillPlay(MusicItem musicItem) {
        canAutoProgress = false;
        musicPlayerView.setSecondaryProgress(0);
        setCurrentMusicItem(musicItem);
    }

    @Override
    public void onMusicReady(MusicItem musicItem) {
        canAutoProgress = true;
        musicPlayerView.setTotalProgress(musicServiceBind.getTotalTime());
        musicPlayerView.setPlayBtnState(true);
    }

    @Override
    public void onPlayingIndexChange(int index) {
        if (musicListDialog != null) {
            musicListDialog.getAdapter().playingIndexChangeTo(index);
        }
    }

    @Override
    public void onBuffering(int percent) {
        musicPlayerView.setSecondaryProgress(percent);
    }

    @Override
    public void onProgress(int second) {
        if (!(musicPlayerView.isUserSliding() || musicPlayerView.isPaused()) && canAutoProgress) {
            musicPlayerView.setProgress(second);
        }
    }

    private void setCurrentMusicItem(MusicItem musicItem) {
        if (musicItem != null) {
            setTitle(musicItem.title);
            toolbar.setSubtitle(musicItem.artist);
            musicPlayerView.setTotalProgress(musicItem.duration);
            musicPlayerView.setCover(musicItem.cover);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicListDialog = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_player;
    }

    private class MusicPlayerCallBackImpl implements MusicPlayerView.MusicPlayerCallBack {

        @Override
        public void start() {
            musicServiceBind.startPlayOrResume();
        }

        @Override
        public void pause() {
            musicServiceBind.pause();
        }

        @Override
        public void next() {
            canAutoProgress = false;
            musicPlayerView.setProgress(0);
            musicServiceBind.next();
        }

        @Override
        public void prev() {
            canAutoProgress = false;
            musicPlayerView.setProgress(0);
            musicServiceBind.prev();
        }

        @Override
        public void onShowMusicSrc() {
            checkDialogIsNull();
            musicListDialog.show();
        }

        @Override
        public void onSliderChanged(int second) {
            musicPlayerView.setProgress(second);
        }

        @Override
        public void onSliderFinished(int currentSecond) {
            musicServiceBind.seekTo(currentSecond);
        }

        @Override
        public void onPlayTypeChange(int playType) {
            musicServiceBind.setPlayType(playType);
            toast(StateImageView.state2String(playType));
        }
    }


    private void checkDialogIsNull() {
        if (musicListDialog == null) {
            musicListDialog = new MusicListDialog(this);

            musicListDialog.init().getAdapter().setData(musicProvider.provideMusics());
            musicListDialog.getAdapter().setCurrentPlayingIndex(musicProvider.getPlayingMusicIndex());
            musicListDialog.getAdapter().setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    musicServiceBind.play(position);
                }
            });
        }
    }
}
