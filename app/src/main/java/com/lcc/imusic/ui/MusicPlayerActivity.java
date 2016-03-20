package com.lcc.imusic.ui;

import android.os.Bundle;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.base.MusicBindActivity;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.LocalMusicProvider;
import com.lcc.imusic.model.MusicProvider;
import com.lcc.imusic.musicplayer.MusicPlayerView;
import com.lcc.imusic.service.MusicPlayService;
import com.lcc.imusic.wiget.MusicListDialog;
import com.lcc.imusic.wiget.StateImageView;

import butterknife.Bind;

public class MusicPlayerActivity extends MusicBindActivity {
    @Bind(R.id.musicPlayer)
    MusicPlayerView musicPlayerView;

    MusicProvider musicProvider;

    MusicInfoListener musicInfoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicProvider = LocalMusicProvider.getMusicProvider(this);

        setCurrentMusicItem(musicProvider.getPlayingMusic());
    }

    @Override
    protected void onBind(final MusicPlayService.MusicServiceBind musicServiceBind) {
        musicPlayerView.setPlayBtnState(musicServiceBind.isPlaying());
        musicPlayerView.setPlayType(musicServiceBind.getPlayType());
        musicPlayerView.setMusicPlayerCallBack(new MusicPlayerCallBackImpl());
        musicInfoListener = new MusicInfoListener();
        musicServiceBind.addMusicProgressListener(musicInfoListener);
        musicServiceBind.addMusicReadyListener(musicInfoListener);
    }

    private class MusicInfoListener implements MusicPlayService.MusicPlayListener, MusicPlayService.MusicProgressListener {
        private boolean canAutoProgress = true;

        @Override
        public void onProgress(int second) {
            if (!(musicPlayerView.isUserSliding() || musicPlayerView.isPaused()) && canAutoProgress) {
                musicPlayerView.setProgress(second);
            }
        }

        @Override
        public void onBuffering(int percent) {
            musicPlayerView.setSecondaryProgress(percent);
        }

        @Override
        public void onMusicWillPlay(MusicItem musicItem) {
            canAutoProgress = false;
            musicPlayerView.setSecondaryProgress(0);
            musicPlayerView.setProgress(0);
            setCurrentMusicItem(musicItem);
        }

        @Override
        public void onMusicReady(MusicItem musicItem) {
            canAutoProgress = true;
            setCurrentMusicItem(musicItem);
        }
    }


    @Override
    protected void unBind(MusicPlayService.MusicServiceBind musicServiceBind) {
        if (musicInfoListener != null) {
            musicServiceBind.removeMusicProgressListener(musicInfoListener);
            musicServiceBind.removeMusicReadyListener(musicInfoListener);
            musicInfoListener = null;
        }
    }


    private void setCurrentMusicItem(MusicItem musicItem) {
        if (musicItem != null) {
            setTitle(musicItem.title);
            toolbar.setSubtitle(musicItem.artist);
            musicPlayerView.setTotalProgress(musicItem.duration);
            musicPlayerView.setCover(musicItem.cover);
            if(musicServiceBind != null)
                musicPlayerView.setPlayBtnState(musicServiceBind.isPlaying());
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
            musicServiceBind.next();
        }

        @Override
        public void prev() {
            musicServiceBind.prev();
        }

        @Override
        public void onShowMusicSrc() {
            checkDialogIsNull();
            musicListDialog.show();
        }

        @Override
        public void onSliderChanged(int second) {

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

    private MusicListDialog musicListDialog;

    private void checkDialogIsNull() {
        if (musicListDialog == null) {
            musicListDialog = new MusicListDialog(this);

            musicListDialog.init().getAdapter().setData(musicProvider.provideMusics());

            musicListDialog.getAdapter().setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    musicServiceBind.play(position);
                }
            });
        }
    }
}
