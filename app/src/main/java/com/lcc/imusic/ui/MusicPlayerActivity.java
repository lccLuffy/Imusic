package com.lcc.imusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.base.activity.MusicProgressCallActivity;
import com.lcc.imusic.bean.DlBean;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.CurrentMusicProvider;
import com.lcc.imusic.model.CurrentMusicProviderImpl;
import com.lcc.imusic.musicplayer.MusicPlayerView;
import com.lcc.imusic.service.DownLoadHelper;
import com.lcc.imusic.service.DownloadService;
import com.lcc.imusic.service.MusicPlayService;
import com.lcc.imusic.wiget.MusicListDialog;
import com.lcc.imusic.wiget.StateImageView;

import java.io.File;

import butterknife.Bind;

public class MusicPlayerActivity extends MusicProgressCallActivity {
    @Bind(R.id.musicPlayer)
    MusicPlayerView musicPlayerView;

    private CurrentMusicProvider currentMusicProvider;

    private MusicListDialog musicListDialog;

    DownloadService.DownLoadEvent downLoadEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentMusicProvider = CurrentMusicProviderImpl.getMusicProvider();
        setCurrentMusicItem(currentMusicProvider.getPlayingMusic());
        downLoadEvent = new DownloadService.DownLoadEventAdapter() {
            @Override
            public void onStart(DlBean dlBean) {
                toast("start download " + dlBean.fileName);
            }

            @Override
            public void onSuccess(DlBean dlBean, File file) {
                toast("download " + dlBean.fileName + "success");
            }

            @Override
            public void onFail(DlBean dlBean, Throwable throwable) {
                toast("download " + dlBean.fileName + " failed," + throwable.getMessage());
            }
        };
        DownLoadHelper.get().addDownloadEvent(downLoadEvent);
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
        if (musicServiceBind != null) {
            musicPlayerView.setTotalProgress(musicServiceBind.getTotalTime());
            musicPlayerView.setPlayBtnState(true);
        }
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
        if (musicListDialog != null) {
            musicListDialog.getAdapter().onDestroy();
            musicListDialog = null;
        }
        if (downLoadEvent != null) {
            DownLoadHelper.get().removeDownloadEvent(downLoadEvent);
            downLoadEvent = null;
        }
        musicListDialog = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_player;
    }

    private class MusicPlayerCallBackImpl extends MusicPlayerView.MusicPlayerCallBackAdapter {
        @Override
        public void onDownload() {
            MusicItem item = currentMusicProvider.getPlayingMusic();
            if (item != null) {
                DlBean<MusicItem> dlBean = new DlBean<>(item.data, item.title.trim() + "-" + item.artist.trim() + ".mp3", item);
                DownLoadHelper.get().enqueue(dlBean);
                Intent intent = new Intent(MusicPlayerActivity.this, DownloadService.class);
                startService(intent);
            }
        }

        @Override
        public void onComment() {
            startActivity(new Intent(MusicPlayerActivity.this, CommentActivity.class));
        }

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
            musicListDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast("download all");
                    DownLoadHelper.get().downloadAll(MusicPlayerActivity.this, currentMusicProvider.provideMusics());
                }
            });
            musicListDialog.init().getAdapter().setData(currentMusicProvider.provideMusics());
            musicListDialog.getAdapter().setCurrentPlayingIndex(currentMusicProvider.getPlayingMusicIndex());
            musicListDialog.getAdapter().setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    musicServiceBind.play(position);
                }
            });
        }
    }
}
