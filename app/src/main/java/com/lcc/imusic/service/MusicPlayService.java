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

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayService extends Service {

    public static final String ACTION_MUSIC_PLAY = "com.lcc.music.play";
    public static final String ACTION_MUSIC_PAUSE = "com.lcc.music.pause";
    public static final String ACTION_MUSIC_NEXT = "com.lcc.music.next";

    private int currentIndex = -1;

    List<MusicItem> localMusicList;

    private MediaPlayer mediaPlayer;
    private Timer timer;
    private ProgressTask progressTask;
    private IBinder binder;

    private MusicInfoCallBack musicInfoCallBack;

    private void initLocalMusicList()
    {
        localMusicList = LocalMusicProvider.getMusicProvider(getApplicationContext()).provideMusics();
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
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder == null ? (binder = new MusicServiceBind()) : binder;
    }

    public void nextMusic()
    {
        if(currentIndex + 1 < localMusicList.size())
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
                mediaPlayer.setDataSource(localMusicList.get(index).path);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        currentIndex = index;
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
        public void setMusicInfoCallBack(MusicInfoCallBack callBack)
        {
            MusicPlayService.this.musicInfoCallBack = callBack;
        }

        public MusicItem getPlayingMusic()
        {
            return localMusicList.get(currentIndex);
        }

        public void seekTo(int second)
        {
            mediaPlayer.seekTo(second * 1000);
        }
    }
    private class MediaListener implements MediaPlayer.OnPreparedListener
        ,MediaPlayer.OnCompletionListener
    {
        @Override
        public void onPrepared(MediaPlayer mp)
        {
            if(musicInfoCallBack != null)
            {
                musicInfoCallBack.onReady(localMusicList.get(currentIndex));
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
    }

    private class ProgressTask extends TimerTask {
        private Handler handler;
        public ProgressTask()
        {
            handler = new Handler(Looper.getMainLooper());
        }
        @Override
        public void run() {
            if(musicInfoCallBack != null)
            {
                final int current = mediaPlayer.getCurrentPosition() / 1000;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        musicInfoCallBack.onProgress(current);
                    }
                });
            }
        }
    }

    public interface MusicInfoCallBack
    {
        void onReady(MusicItem musicItem);
        void onProgress(int currentTime);
    }
}
