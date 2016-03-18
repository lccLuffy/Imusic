package com.lcc.imusic.ui;

import android.os.Bundle;

import com.lcc.imusic.R;
import com.lcc.imusic.base.MusicBindActivity;
import com.lcc.imusic.musicplayer.MusicPlayerView;
import com.lcc.imusic.service.MusicPlayService;

import butterknife.Bind;

public class MusicPlayerActivity extends MusicBindActivity {
    @Bind(R.id.musicPlayer)
    MusicPlayerView musicPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onBind(final MusicPlayService.MusicServiceBind musicServiceBind) {
        boolean isPlay = false;
        musicPlayerView.setPlayBtnState(isPlay = musicServiceBind.isPlaying());
        if(isPlay)
        {
            setCurrentMusicItem(musicServiceBind.getPlayingMusic());
        }
        musicPlayerView.setMusicPlayerCallBack(new MusicPlayerCallBackImpl());

        musicServiceBind.setMusicInfoCallBack(new MusicPlayService.MusicInfoCallBack() {
            @Override
            public void onReady(MusicPlayerView.MusicItem musicItem) {
                setCurrentMusicItem(musicItem);
                final int totalTime = musicServiceBind.getTotalTime();
                musicPlayerView.setTotalProgress(totalTime);
            }
            @Override
            public void onProgress(int currentTime) {
                musicPlayerView.setProgress(currentTime);
            }
        });
    }

    private void setCurrentMusicItem(MusicPlayerView.MusicItem musicItem)
    {
        setTitle(musicItem.title);
        toolbar.setSubtitle(musicItem.artist);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_player;
    }

    private class MusicPlayerCallBackImpl implements MusicPlayerView.MusicPlayerCallBack
    {
        @Override
        public void start() {
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
