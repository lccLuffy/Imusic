package com.lcc.imusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.EventsManager;
import com.lcc.imusic.manager.MusicNotificationManager;
import com.lcc.imusic.model.CurrentMusicProvider;
import com.lcc.imusic.model.MusicProvider;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayService extends Service {
    public static final int PLAY_TYPE_LOOP = 1;
    public static final int PLAY_TYPE_ONE = 2;
    public static final int PLAY_TYPE_RANDOM = 3;

    private MediaPlayer mediaPlayer;
    private Timer timer;
    private ProgressTask progressTask;
    private IBinder binder;

    public static boolean HAS_STATED = false;

    MusicNotificationManager musicNotificationManager;

    private MusicProvider musicProvider;

    private boolean lockPrepared = false;

    @Override
    public void onCreate() {
        super.onCreate();
        HAS_STATED = true;
        initMediaPlayer();
        initLocalMusicList();
        musicNotificationManager = new MusicNotificationManager(this);
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
        musicProvider = CurrentMusicProvider.getMusicProvider(getApplicationContext());
    }

    private int currentIndex = -1;

    private Random random;

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

            musicProvider.setPlayingMusic(index);

            mediaPlayer.reset();

            EventsManager.get().dispatchOnMusicWillPlayEvent(musicItem);

            mediaPlayer.setDataSource(musicItem.data);
            lockPrepared = false;
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
            musicNotificationManager.pause();
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
            if (lockPrepared && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
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
        return result;
    }

    private class MediaListener implements MediaPlayer.OnPreparedListener
            , MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener
            , MediaPlayer.OnErrorListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            lockPrepared = true;
            mp.start();
            EventsManager.get().dispatchOnMusicReadyEvent(musicProvider.getPlayingMusic());
            if (progressTask == null) {
                progressTask = new ProgressTask();
                timer = new Timer();
                timer.schedule(progressTask, 0, 1000);
            }
            if (!musicNotificationManager.hasNotified()) {
                musicNotificationManager.notifyNotification();
            }
            musicNotificationManager.update(musicProvider.getPlayingMusic());
        }


        @Override
        public void onCompletion(MediaPlayer mp) {
            playMusic(indexByPlayType());
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            EventsManager.get().dispatchOnBufferingEvent(percent);
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
            EventsManager.get().dispatchOnProgressEvent(mediaPlayer.getCurrentPosition() / 1000, handler);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        musicNotificationManager.onDestroy();
        musicNotificationManager = null;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        EventsManager.get().clearAllEvents();

        mediaPlayer.release();
        binder = null;
        progressTask = null;
        musicProvider = null;
        mediaPlayer = null;
        Logger.i("MusicPlayService onDestroy");
        HAS_STATED = false;
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
