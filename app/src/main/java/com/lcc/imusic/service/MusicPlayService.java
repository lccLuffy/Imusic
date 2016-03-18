package com.lcc.imusic.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.LocalMusicProvider;
import com.lcc.imusic.model.MusicProvider;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayService extends Service {

    public static final String ACTION_MUSIC_PLAY = "com.lcc.music.play";
    public static final String ACTION_MUSIC_PAUSE = "com.lcc.music.pause";
    public static final String ACTION_MUSIC_NEXT = "com.lcc.music.next";

    private int currentIndex = -1;

    private MediaPlayer mediaPlayer;
    private Timer timer;
    private ProgressTask progressTask;
    private IBinder binder;

    private List<MusicInfoCallBack> musicInfoCallBacks;

    MusicProvider musicProvider;

    private void initLocalMusicList()
    {
        musicProvider = LocalMusicProvider.getMusicProvider(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();

        initMediaPlayer();

        initLocalMusicList();


        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.notification_play_panel);

        /**
         * play
         */
        Intent play = new Intent(ACTION_MUSIC_PLAY);
        PendingIntent playIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, play, 0);
        contentView.setOnClickPendingIntent(R.id.notification_play,playIntent);


        /**
         * next
         */
        Intent next = new Intent(ACTION_MUSIC_NEXT);
        PendingIntent nextIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, next, 0);
        contentView.setOnClickPendingIntent(R.id.notification_next,nextIntent);


        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext());

        builder.setContent(contentView)
                .setSmallIcon(R.mipmap.ic_launcher);
        startForeground(1, builder.build());
    }

    private void initMediaPlayer()
    {
        mediaPlayer = new MediaPlayer();
        MediaListener mediaListener = new MediaListener();
        mediaPlayer.setOnPreparedListener(mediaListener);
        mediaPlayer.setOnCompletionListener(mediaListener);
        mediaPlayer.setOnBufferingUpdateListener(mediaListener);
        mediaPlayer.setOnErrorListener(mediaListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder == null ? (binder = new MusicServiceBind()) : binder;
    }

    public void nextMusic()
    {
        if(currentIndex + 1 < musicProvider.provideMusics().size())
        {
            playMusic(currentIndex + 1);
        }
        else
        {
            playMusic(0);
        }
    }
    public void prevMusic()
    {
        if(currentIndex >= 1)
        {
            playMusic(currentIndex - 1);
        }
    }
    public void playMusic(int index)
    {
        if(currentIndex == index)
        {
            if(!mediaPlayer.isPlaying())
            {
                mediaPlayer.start();
            }
        }
        else
        {
            try
            {
                mediaPlayer.reset();
                musicProvider.setPlayingMusic(index);
                currentIndex = index;
                mediaPlayer.setDataSource(musicProvider.provideMusics().get(index).data);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.e(e,"IOException");
            }
        }

    }
    public void pauseMusic()
    {
        mediaPlayer.pause();
    }

    public class MusicServiceBind extends Binder
    {
        public void playMusic(int index)
        {
            MusicPlayService.this.playMusic(index);
        }

        public void start()
        {
            mediaPlayer.start();
        }

        public void pause()
        {
            pauseMusic();
        }

        public int getTotalTime()
        {
            return mediaPlayer.getDuration() / 1000;
        }

        public boolean isPlaying()
        {
            return mediaPlayer.isPlaying();
        }
        public void next()
        {
            nextMusic();
        }
        public void prev()
        {
            prevMusic();
        }
        public MusicInfoCallBack addMusicInfoCallBack(MusicInfoCallBack callBack)
        {
            if(musicInfoCallBacks == null)
                musicInfoCallBacks = new ArrayList<>();
            musicInfoCallBacks.add(callBack);
            return callBack;
        }

        public void removeMusicInfoCallBack(MusicInfoCallBack callBack)
        {
            musicInfoCallBacks.remove(callBack);
        }

        public void seekTo(int second)
        {
            mediaPlayer.seekTo(second * 1000);
        }
    }
    private class MediaListener implements MediaPlayer.OnPreparedListener
        ,MediaPlayer.OnCompletionListener,MediaPlayer.OnBufferingUpdateListener
        ,MediaPlayer.OnErrorListener
    {
        @Override
        public void onPrepared(MediaPlayer mp)
        {
            if(musicInfoCallBacks != null)
            {
                for (MusicInfoCallBack callBack : musicInfoCallBacks)
                {
                    callBack.onReady(musicProvider.provideMusics().get(currentIndex));
                }
            }
            if(progressTask == null)
            {
                progressTask = new ProgressTask();
                timer.schedule(new ProgressTask(), 0, 1000);
            }
            mp.start();
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            nextMusic();
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Logger.i("onBufferingUpdate:%d%",percent);
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Logger.i("onError:what:%d,extra:%d",what,extra);
            return false;
        }
    }

    private class ProgressTask extends TimerTask {
        private Handler handler;
        public ProgressTask()
        {
            handler = new Handler(Looper.getMainLooper());
        }
        @Override
        public void run() {
            if(musicInfoCallBacks != null)
            {
                final int current = mediaPlayer.getCurrentPosition() / 1000;
                for (final MusicInfoCallBack callBack : musicInfoCallBacks)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onProgress(current);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        timer.cancel();
        binder = null;
        progressTask = null;
        if(musicInfoCallBacks != null)
        {
            musicInfoCallBacks.clear();
            musicInfoCallBacks = null;
        }
        timer = null;
        musicProvider = null;
        mediaPlayer = null;
        Logger.i("MusicPlayService onDestroy");
    }

    public interface MusicInfoCallBack
    {
        void onReady(MusicItem musicItem);
        void onProgress(int currentTime);
    }
}
