package com.lcc.imusic.ui;

import android.os.Bundle;

import com.lcc.imusic.R;
import com.lcc.imusic.base.MusicBindActivity;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.LocalMusicProvider;
import com.lcc.imusic.model.MusicProvider;
import com.lcc.imusic.musicplayer.MusicPlayerView;
import com.lcc.imusic.service.MusicPlayService;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

public class MusicPlayerActivity extends MusicBindActivity {
    @Bind(R.id.musicPlayer)
    MusicPlayerView musicPlayerView;

    MusicProvider musicProvider;

    MusicPlayService.MusicInfoCallBack callBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        musicProvider = LocalMusicProvider.getMusicProvider(this);
        setCurrentMusicItem(musicProvider.getPlayingMusic());
    }

    @Override
    protected void onBind(final MusicPlayService.MusicServiceBind musicServiceBind)
    {
        musicPlayerView.setPlayBtnState(musicServiceBind.isPlaying());

        musicPlayerView.setMusicPlayerCallBack(new MusicPlayerCallBackImpl());

        callBack = musicServiceBind.addMusicInfoCallBack(new MusicPlayService.MusicInfoCallBack() {
            @Override
            public void onReady(MusicItem musicItem) {
                setCurrentMusicItem(musicItem);
            }
            @Override
            public void onProgress(int currentTime) {
                musicPlayerView.setProgress(currentTime);
            }
        });
    }


    @Override
    protected void unBind(MusicPlayService.MusicServiceBind musicServiceBind) {
        if(callBack != null)
        {
            musicServiceBind.removeMusicInfoCallBack(callBack);
            callBack = null;
        }
    }

    private void setCurrentMusicItem(MusicItem musicItem)
    {
        if(musicItem != null)
        {
            setTitle(musicItem.title);
            toolbar.setSubtitle(musicItem.artist);
            musicPlayerView.setTotalProgress(musicItem.duration);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_player;
    }

    private class MusicPlayerCallBackImpl implements MusicPlayerView.MusicPlayerCallBack
    {
        @Override
        public void start() {
            Logger.i("MusicPlayerActivity start");

            musicServiceBind.playMusic(0);
        }

        @Override
        public void pause() {
            musicServiceBind.pause();
        }

        @Override
        public void next() {
            musicPlayerView.setProgress(0);
            musicServiceBind.next();
        }
        @Override
        public void prev() {
            musicPlayerView.setProgress(0);
            musicServiceBind.prev();
        }
        @Override
        public void onSliderChanged(int second) {
            musicPlayerView.setProgress(second);
        }

        @Override
        public void onSliderFinished(int currentSecond) {
            musicServiceBind.seekTo(currentSecond);
        }
    }
}
