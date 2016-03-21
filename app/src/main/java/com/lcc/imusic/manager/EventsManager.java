package com.lcc.imusic.manager;

import android.os.Handler;

import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.PlayingIndexChangeListener;
import com.lcc.imusic.service.MusicPlayService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public class EventsManager {
    private static EventsManager eventsManager;
    private List<PlayingIndexChangeListener> playingIndexChangeListeners;
    private List<MusicPlayService.MusicPlayListener> musicPlayListeners;
    private List<MusicPlayService.MusicProgressListener> musicProgressListeners;

    private EventsManager() {
    }

    public static EventsManager get() {
        if (eventsManager == null)
            eventsManager = new EventsManager();
        return eventsManager;
    }


    public void dispatchOnMusicWillPlayEvent(MusicItem musicItem) {
        if (musicPlayListeners != null) {
            for (final MusicPlayService.MusicPlayListener listener : musicPlayListeners) {
                listener.onMusicWillPlay(musicItem);
            }
        }
    }

    public void dispatchOnMusicReadyEvent(MusicItem musicItem) {
        if (musicPlayListeners != null) {
            for (MusicPlayService.MusicPlayListener listener : musicPlayListeners) {
                listener.onMusicReady(musicItem);
            }
        }
    }

    public void dispatchOnBufferingEvent(int percent) {
        if (musicProgressListeners != null) {
            for (final MusicPlayService.MusicProgressListener listener : musicProgressListeners) {
                listener.onBuffering(percent);
            }
        }
    }

    public void dispatchOnProgressEvent(final int currentSecond, Handler handler) {
        if (musicProgressListeners != null) {
            for (final MusicPlayService.MusicProgressListener listener : musicProgressListeners) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onProgress(currentSecond);
                    }
                });
            }
        }
    }


    public void addMusicPlayListener(MusicPlayService.MusicPlayListener listener) {
        if (musicPlayListeners == null)
            musicPlayListeners = new ArrayList<>();
        musicPlayListeners.add(listener);
    }

    public void removeMusicPlayListener(MusicPlayService.MusicPlayListener listener) {
        musicPlayListeners.remove(listener);
    }

    public void addMusicProgressListener(MusicPlayService.MusicProgressListener listener) {
        if (musicProgressListeners == null)
            musicProgressListeners = new ArrayList<>();
        musicProgressListeners.add(listener);
    }

    public void removeMusicProgressListener(MusicPlayService.MusicProgressListener listener) {
        musicProgressListeners.remove(listener);
    }


    public void addPlayingIndexChangeListener(PlayingIndexChangeListener listener) {
        if (playingIndexChangeListeners == null)
            playingIndexChangeListeners = new ArrayList<>();
        playingIndexChangeListeners.add(listener);
    }

    public void removePlayingIndexChangeListener(PlayingIndexChangeListener listener) {
        if (playingIndexChangeListeners != null) {
            playingIndexChangeListeners.remove(listener);
        }
    }

    public void dispatchPlayingIndexChangeListener(int index) {
        if (playingIndexChangeListeners != null) {
            for (PlayingIndexChangeListener listener : playingIndexChangeListeners) {
                listener.onPlayingIndexChange(index);
            }
        }
    }


    public void clearAllEvents() {
        if (musicPlayListeners != null)
            musicPlayListeners.clear();
        if (musicProgressListeners != null)
            musicProgressListeners.clear();
    }
}
