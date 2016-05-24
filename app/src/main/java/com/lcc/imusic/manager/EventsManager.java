package com.lcc.imusic.manager;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.CurrentMusicProviderImpl;
import com.lcc.imusic.model.PlayingIndexChangeListener;
import com.lcc.imusic.service.MusicPlayService;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public class EventsManager {
    private List<PlayingIndexChangeListener> playingIndexChangeListeners;

    private List<MusicPlayService.MusicPlayListener> musicPlayListeners;

    private List<MusicPlayService.MusicProgressListener> musicProgressListeners;
    private List<CurrentMusicProviderImpl.CurrentPlayingListChangeListener> currentPlayingListChangeListeners;

    private EventsManager() {
    }

    private static final class ClassHolder {
        private static EventsManager eventsManager = new EventsManager();
    }

    public static EventsManager get() {
        return ClassHolder.eventsManager;
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

    public void dispatchPlayingIndexChangeEvent(int index) {
        if (playingIndexChangeListeners != null) {
            for (PlayingIndexChangeListener listener : playingIndexChangeListeners) {
                listener.onPlayingIndexChange(index);
            }
        }
    }

    public void dispatchCurrentPlayingListChangeEvent(@NonNull List<MusicItem> musicItems) {
        if (currentPlayingListChangeListeners != null) {
            for (CurrentMusicProviderImpl.CurrentPlayingListChangeListener listener : currentPlayingListChangeListeners) {
                listener.onCurrentPlayingListChange(musicItems);
            }
        }
    }

    public void addMusicPlayListener(MusicPlayService.MusicPlayListener listener) {
        if (musicPlayListeners == null)
            musicPlayListeners = new ArrayList<>();
        musicPlayListeners.add(listener);
    }

    public void removeMusicPlayListener(MusicPlayService.MusicPlayListener listener) {
        if (musicPlayListeners != null) {
            musicPlayListeners.remove(listener);
            //Logger.i("remove @ " + listener);
        }
    }

    public void addMusicProgressListener(MusicPlayService.MusicProgressListener listener) {
        if (musicProgressListeners == null)
            musicProgressListeners = new ArrayList<>();
        musicProgressListeners.add(listener);
    }

    public void removeMusicProgressListener(MusicPlayService.MusicProgressListener listener) {
        if (musicProgressListeners != null) {
            musicProgressListeners.remove(listener);
            //Logger.i("remove @ " + listener);
        }
    }

    public void addCurrentPlayingListChangeListener(CurrentMusicProviderImpl.CurrentPlayingListChangeListener listener) {
        if (currentPlayingListChangeListeners == null) {
            currentPlayingListChangeListeners = new ArrayList<>();
            currentPlayingListChangeListeners.add(listener);
        }
    }

    public void removeCurrentPlayingListChangeListener(CurrentMusicProviderImpl.CurrentPlayingListChangeListener listener) {
        if (currentPlayingListChangeListeners != null) {
            currentPlayingListChangeListeners.remove(listener);
        }
    }

    public void addPlayingIndexChangeListener(PlayingIndexChangeListener listener) {
        if (playingIndexChangeListeners == null)
            playingIndexChangeListeners = new ArrayList<>();
        playingIndexChangeListeners.add(listener);
    }

    public void removePlayingIndexChangeListener(PlayingIndexChangeListener listener) {
        if (playingIndexChangeListeners != null) {
            playingIndexChangeListeners.remove(listener);
            Logger.i("remove @ " + listener);
        }
    }

    public void clearAllEvents() {
        if (musicPlayListeners != null) {
            musicPlayListeners.clear();
            musicPlayListeners = null;
        }
        if (musicProgressListeners != null) {
            musicProgressListeners.clear();
            musicProgressListeners = null;
        }

        if (currentPlayingListChangeListeners != null) {
            currentPlayingListChangeListeners.clear();
            currentPlayingListChangeListeners = null;
        }
    }
}
