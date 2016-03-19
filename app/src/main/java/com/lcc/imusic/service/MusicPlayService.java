package com.lcc.imusic.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.lcc.imusic.ui.MusicPlayerActivity;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayService extends Service {

    public static final int PLAY_TYPE_LOOP = 1;
    public static final int PLAY_TYPE_ONE = 2;
    public static final int PLAY_TYPE_RANDOM = 3;

    public static final String ACTION_MUSIC_PLAY_OR_PAUSE = "com.lcc.music.play";
    public static final String ACTION_MUSIC_NEXT = "com.lcc.music.next";

    private int currentIndex = -1;

    private MediaPlayer mediaPlayer;
    private Timer timer;
    private ProgressTask progressTask;
    private IBinder binder;

    private List<MusicReadyListener> musicReadyListeners;
    private List<MusicProgressListener> musicProgressListeners;

    private MusicControllerReceiver musicControllerReceiver;

    private MusicProvider musicProvider;

    private Random random;

    private int playType = PLAY_TYPE_LOOP;

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();
        initLocalMusicList();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
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
    private void initLocalMusicList()
    {
        musicProvider = LocalMusicProvider.getMusicProvider(getApplicationContext());
    }


    RemoteViews contentView;
    public void initNotification()
    {
        musicControllerReceiver = new MusicControllerReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MUSIC_NEXT);
        intentFilter.addAction(ACTION_MUSIC_PLAY_OR_PAUSE);

        registerReceiver(musicControllerReceiver,intentFilter);

        contentView = new RemoteViews(getPackageName(), R.layout.notification_play_panel);
        /**
         * play
         */
        Intent play = new Intent(ACTION_MUSIC_PLAY_OR_PAUSE);
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

        builder.setOngoing(true);
        builder.setContent(contentView)
                .setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        builder.setContentIntent(pendingIntent);
        startForeground(1, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder == null ? (binder = new MusicServiceBind()) : binder;
    }

    public void nextMusic()
    {
        if (playType == PLAY_TYPE_RANDOM)
        {
            playMusic(getPlayIndexWithType());
        }
        else if(currentIndex + 1 < musicProvider.provideMusics().size())
        {
            playMusic(currentIndex + 1);
        }
        else
        {
            playMusic(0);
        }
    }

    private int getPlayIndexWithType()
    {
        switch (playType)
        {
            case PLAY_TYPE_ONE:
                return currentIndex;
            case PLAY_TYPE_RANDOM:
                if(random == null)
                    random = new Random();
                return random.nextInt(musicProvider.provideMusics().size());
        }
        return currentIndex + 1;
    }

    public void prevMusic()
    {
        if (playType == PLAY_TYPE_RANDOM)
        {
            playMusic(getPlayIndexWithType());
        }
        else if(currentIndex >= 1)
        {
            playMusic(currentIndex - 1);
        }
        else
        {
            playMusic(musicProvider.provideMusics().size() - 1);
        }
    }

    public void playMusic(int index)
    {
        if(currentIndex == index)
        {
            startMusic();
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
            catch (Throwable throwable)
            {
                Logger.e(throwable,"Throwable");
            }
        }

    }
    public void pauseMusic()
    {
        mediaPlayer.pause();
    }

    public void startMusic()
    {
        if(currentIndex != -1)
        {
            mediaPlayer.start();
            if(musicReadyListeners != null)
            {
                for (MusicReadyListener listener : musicReadyListeners)
                {
                    listener.onMusicReady(musicProvider.provideMusics().get(currentIndex));
                }
            }
        }
        else
        {
            playMusic(0);
        }
    }

    public class MusicServiceBind extends Binder
    {
        public void playMusic(int index)
        {
            MusicPlayService.this.playMusic(index);
        }

        public void start()
        {
            startMusic();
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
        public void addMusicReadyListener(MusicReadyListener listener)
        {
            if(musicReadyListeners == null)
                musicReadyListeners = new ArrayList<>();
            musicReadyListeners.add(listener);
        }

        public void removeMusicReadyListener(MusicReadyListener listener)
        {
            musicReadyListeners.remove(listener);
        }

        public void addMusicProgressListener(MusicProgressListener listener)
        {
            if(musicProgressListeners == null)
                musicProgressListeners = new ArrayList<>();
            musicProgressListeners.add(listener);
        }
        public void removeMusicProgressListener(MusicProgressListener listener)
        {
            musicProgressListeners.remove(listener);
        }
        public void seekTo(int second)
        {
            mediaPlayer.seekTo(second * 1000);
        }
        public int getPlayType()
        {
            return playType;
        }
        public void setPlayType(int playType)
        {
            MusicPlayService.this.playType = playType;
        }
    }
    private class MediaListener implements MediaPlayer.OnPreparedListener
        ,MediaPlayer.OnCompletionListener,MediaPlayer.OnBufferingUpdateListener
        ,MediaPlayer.OnErrorListener
    {
        @Override
        public void onPrepared(MediaPlayer mp)
        {
            mp.start();
            if(musicReadyListeners != null)
            {
                for (MusicReadyListener listener : musicReadyListeners)
                {
                    listener.onMusicReady(musicProvider.provideMusics().get(currentIndex));
                }
            }
            if(progressTask == null)
            {
                progressTask = new ProgressTask();
                timer = new Timer();
                timer.schedule(new ProgressTask(), 0, 1000);
            }

            if(!hasShowNotification)
            {
                hasShowNotification = true;
                initNotification();
            }
            MusicItem item = musicProvider.getPlayingMusic();
            contentView.setCharSequence(R.id.notification_title,"setText",item.title);
            contentView.setCharSequence(R.id.notification_subtitle,"setText",item.artist);
        }
        private boolean hasShowNotification = false;
        @Override
        public void onCompletion(MediaPlayer mp) {
            playMusic(getPlayIndexWithType());
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
            if(musicProgressListeners != null)
            {
                final int current = mediaPlayer.getCurrentPosition() / 1000;
                for (final MusicProgressListener listener : musicProgressListeners)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onProgress(current);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(musicControllerReceiver != null)
        {
            unregisterReceiver(musicControllerReceiver);
            musicControllerReceiver = null;
        }
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
        if(musicReadyListeners != null)
        {
            musicReadyListeners.clear();
            musicReadyListeners = null;
        }
        if(musicProgressListeners != null)
        {
            musicProgressListeners.clear();
            musicProgressListeners = null;
        }

        mediaPlayer.release();
        binder = null;
        progressTask = null;
        contentView = null;
        musicProvider = null;
        mediaPlayer = null;
        Logger.i("MusicPlayService onDestroy");
    }


    public class MusicControllerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(ACTION_MUSIC_NEXT.equals(action))
            {
                nextMusic();
            }
            else if(ACTION_MUSIC_PLAY_OR_PAUSE.equals(action))
            {
                startMusic();
            }
        }
    }
    public interface MusicReadyListener
    {
        void onMusicReady(MusicItem musicItem);
    }
    public interface MusicProgressListener
    {
        void onProgress(int second);
    }
}
