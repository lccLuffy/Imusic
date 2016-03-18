package com.lcc.imusic.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.lcc.imusic.R;
import com.lcc.imusic.base.BaseActivity;
import com.lcc.imusic.musicplayer.MusicPlayerView;
import com.lcc.imusic.service.MusicPlayService;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

public class MusicPlayerActivity extends BaseActivity {
    @Bind(R.id.musicPlayer)
    MusicPlayerView musicPlayerView;
    private MusicPlayService.MyBind myBind;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent, serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBind = (MusicPlayService.MyBind) service;
                musicPlayerView.setMusicPlayerCallBack(new MusicPlayerCallBackImpl());
                musicPlayerView.setPlayBtnState(myBind.isPlaying());
                myBind.setMusicInfoCallBack(new MusicPlayService.MusicInfoCallBack() {
                    @Override
                    public void onReady(MusicPlayerView.MusicItem musicItem) {
                        setTitle(musicItem.title);
                        toolbar.setSubtitle(musicItem.artist);
                        final int totalTime = myBind.getTotalTime();
                        musicPlayerView.setTotalProgress(totalTime);
                    }

                    @Override
                    public void onProgress(int currentTime) {
                        musicPlayerView.setProgress(currentTime);
                    }
                });
                myBind.playMusic(0);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        },BIND_AUTO_CREATE);
        if(myBind == null)
            Logger.i("myBind == null");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_player;
    }

    private class MusicPlayerCallBackImpl implements MusicPlayerView.MusicPlayerCallBack
    {

        @Override
        public void start() {
            myBind.playMusic(0);
        }

        @Override
        public void pause() {
            myBind.pause();
        }

        @Override
        public void next() {
            musicPlayerView.setProgress(0);
            myBind.next();
        }

        @Override
        public void prev() {
            musicPlayerView.setProgress(0);
            myBind.prev();
        }

        @Override
        public void onSliderChanged(int second) {
            musicPlayerView.setProgress(second);
        }

        @Override
        public void onSliderFinished(int currentSecond) {
            myBind.seekTo(currentSecond);
        }
    }
}
