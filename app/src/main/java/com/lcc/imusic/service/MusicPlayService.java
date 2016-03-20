package com.lcc.imusic.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
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

    private MediaPlayer mediaPlayer;
    private Timer timer;
    private ProgressTask progressTask;
    private IBinder binder;

    private List<MusicPlayListener> musicPlayListeners;
    private List<MusicProgressListener> musicProgressListeners;

    public static boolean HAS_STATED = false;

    private MusicControllerReceiver musicControllerReceiver;

    private MusicProvider musicProvider;


    @Override
    public void onCreate() {
        super.onCreate();
        HAS_STATED = true;
        initMediaPlayer();
        initLocalMusicList();
        initNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        MediaListener mediaListener = new MediaListener();
        mediaPlayer.setOnPreparedListener(mediaListener);
        mediaPlayer.setOnCompletionListener(mediaListener);
        mediaPlayer.setOnBufferingUpdateListener(mediaListener);
        mediaPlayer.setOnErrorListener(mediaListener);
    }

    private void initLocalMusicList() {
        musicProvider = LocalMusicProvider.getMusicProvider(getApplicationContext());
    }

    private int currentIndex = -1;


    RemoteViews contentView;

    private Random random;

    public void initNotification() {
        musicControllerReceiver = new MusicControllerReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MUSIC_NEXT);
        intentFilter.addAction(ACTION_MUSIC_PLAY_OR_PAUSE);

        registerReceiver(musicControllerReceiver, intentFilter);

        contentView = new RemoteViews(getPackageName(), R.layout.notification_play_panel);
        /**
         * play
         */
        Intent play = new Intent(ACTION_MUSIC_PLAY_OR_PAUSE);
        PendingIntent playIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, play, 0);
        contentView.setOnClickPendingIntent(R.id.notification_play, playIntent);


        /**
         * next
         */
        Intent next = new Intent(ACTION_MUSIC_NEXT);
        PendingIntent nextIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, next, 0);
        contentView.setOnClickPendingIntent(R.id.notification_next, nextIntent);


        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext());

        builder.setOngoing(true);
        builder.setContent(contentView)
                .setSmallIcon(R.mipmap.ic_launcher);
        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        startForeground(1, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder == null ? (binder = new MusicServiceBind()) : binder;
    }

    public void playMusic(int index) {
        checkBoundary(index);
        if (currentIndex == index && mediaPlayer.isPlaying()) {
            return;
        }
        currentIndex = index;
        MusicItem musicItem = musicProvider.provideMusics().get(index);
        try {


            currentIndex = index;

            musicProvider.setPlayingMusic(index);

            dispatchOnMusicWillPlayEvent(musicItem);

            mediaPlayer.reset();

            mediaPlayer.setDataSource(musicItem.data);

            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(e, "IOException");
        } catch (Throwable throwable) {
            Logger.e(throwable, "Throwable");
        }
    }

    public void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void nextMusic() {
        int r = 0;
        if (currentIndex != -1) {
            r = currentIndex + 1;
        } else {
            r = musicProvider.getPlayingMusicIndex() + 1;
        }
        if (r >= musicProvider.provideMusics().size())
            r = 0;
        playMusic(r);
    }

    public void prevMusic() {
        int r = 0;
        if (currentIndex != -1) {
            r = currentIndex - 1;
        } else {
            r = musicProvider.getPlayingMusicIndex() - 1;
        }
        if (r < 0)
            r = musicProvider.provideMusics().size() - 1;
        playMusic(r);
    }

    public void startPlayOrResume() {
        if (currentIndex == -1) {
            playMusic(musicProvider.getPlayingMusicIndex());
        } else {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                dispatchOnMusicReadyEvent();
            }
        }
    }

    private void dispatchOnMusicWillPlayEvent(MusicItem musicItem) {
        if (musicPlayListeners != null) {
            for (final MusicPlayListener listener : musicPlayListeners) {
                listener.onMusicWillPlay(musicItem);
            }
        }
    }

    private void dispatchOnBufferingEvent(int percent) {
        if (musicProgressListeners != null) {
            for (final MusicProgressListener listener : musicProgressListeners) {
                listener.onBuffering(percent);
            }
        }
    }

    private void dispatchOnMusicReadyEvent() {
        if (musicPlayListeners != null) {
            for (MusicPlayListener listener : musicPlayListeners) {
                listener.onMusicReady(musicProvider.provideMusics().get(currentIndex));
            }
        }
    }

    private void checkBoundary(int index) {
        if (index < 0 || index >= musicProvider.provideMusics().size())
            throw new IllegalStateException("Oops,music index is out of boundary!!!! index is "
                    + index + ", but music size is " + musicProvider.provideMusics().size());
    }

    public class MusicServiceBind extends Binder {
        public void play(int index) {
            playMusic(index);
        }

        public void startPlayOrResume() {
            MusicPlayService.this.startPlayOrResume();
        }

        public void pause() {
            pauseMusic();
        }

        public int getTotalTime() {
            return mediaPlayer.getDuration() / 1000;
        }

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        public void next() {
            nextMusic();
        }

        public void prev() {
            prevMusic();
        }

        public void addMusicReadyListener(MusicPlayListener listener) {
            if (musicPlayListeners == null)
                musicPlayListeners = new ArrayList<>();
            musicPlayListeners.add(listener);
        }

        public void removeMusicReadyListener(MusicPlayListener listener) {
            musicPlayListeners.remove(listener);
        }

        public void addMusicProgressListener(MusicProgressListener listener) {
            if (musicProgressListeners == null)
                musicProgressListeners = new ArrayList<>();
            musicProgressListeners.add(listener);
        }

        public void removeMusicProgressListener(MusicProgressListener listener) {
            musicProgressListeners.remove(listener);
        }

        public void seekTo(int second) {
            mediaPlayer.seekTo(second * 1000);
        }

        public int getPlayType() {
            return playType;
        }

        public void setPlayType(int playType) {
            MusicPlayService.this.playType = playType;
        }
    }

    public int randomIndex() {
        int allIndex = musicProvider.provideMusics().size();
        if (allIndex == 1) {
            return 0;
        }
        if (random == null)
            random = new Random(System.currentTimeMillis());
        int randomIndex = random.nextInt(allIndex);
        while (randomIndex == currentIndex) {
            randomIndex = random.nextInt(allIndex);
        }
        return randomIndex;
    }

    private int playType = PLAY_TYPE_LOOP;

    private int indexByPlayType() {
        int result = 0;
        switch (playType) {
            case PLAY_TYPE_ONE:
                result = currentIndex;
            case PLAY_TYPE_RANDOM:
                result = randomIndex();
            default:
                result = currentIndex + 1;
                if (result >= musicProvider.provideMusics().size())
                    result = 0;
                break;
        }
        return (result);
    }

    private class MediaListener implements MediaPlayer.OnPreparedListener
            , MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener
            , MediaPlayer.OnErrorListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            dispatchOnMusicReadyEvent();
            if (progressTask == null) {
                progressTask = new ProgressTask();
                timer = new Timer();
                timer.schedule(new ProgressTask(), 0, 1000);
            }

            if (!hasShowNotification) {
                hasShowNotification = true;
                initNotification();
            }
            MusicItem item = musicProvider.getPlayingMusic();
            contentView.setCharSequence(R.id.notification_title, "setText", item.title);
            contentView.setCharSequence(R.id.notification_subtitle, "setText", item.artist);
        }

        private boolean hasShowNotification = false;

        @Override
        public void onCompletion(MediaPlayer mp) {
            playMusic(indexByPlayType());
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            dispatchOnBufferingEvent(percent);
            /*Logger.i("onBufferingUpdate:%d%%", percent);*/
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Logger.i("onError:what: %d, extra: %d,music:%s", what, extra, musicProvider.getPlayingMusic().title);
            return true;
        }
    }

    private class ProgressTask extends TimerTask {
        private Handler handler;

        public ProgressTask() {
            handler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void run() {
            dispatchOnProgressEvent();
        }

        private void dispatchOnProgressEvent() {
            if (musicProgressListeners != null) {
                final int current = mediaPlayer.getCurrentPosition() / 1000;
                for (final MusicProgressListener listener : musicProgressListeners) {
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
        if (musicControllerReceiver != null) {
            unregisterReceiver(musicControllerReceiver);
            musicControllerReceiver = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (musicPlayListeners != null) {
            musicPlayListeners.clear();
            musicPlayListeners = null;
        }
        if (musicProgressListeners != null) {
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
        HAS_STATED = false;
    }


    public class MusicControllerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_MUSIC_NEXT.equals(action)) {
                nextMusic();
            } else if (ACTION_MUSIC_PLAY_OR_PAUSE.equals(action)) {
                startPlayOrResume();
            }
        }
    }


    public interface MusicPlayListener {
        void onMusicWillPlay(MusicItem musicItem);

        void onMusicReady(MusicItem musicItem);
    }

    public interface MusicProgressListener {
        void onProgress(int second);

        void onBuffering(int percent);
    }
}
