package com.lcc.imusic.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.EventsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class CurrentMusicProviderImpl implements CurrentMusicProvider {

    public final static int NO_PLAYING_INDEX = -1;

    private List<MusicItem> musicList;
    private int playingMusicIndex = NO_PLAYING_INDEX;

    private static CurrentMusicProviderImpl musicProvider;

    public static CurrentMusicProviderImpl getMusicProvider() {
        if (musicProvider == null)
            musicProvider = new CurrentMusicProviderImpl();
        return musicProvider;
    }

    private CurrentMusicProviderImpl() {
        musicList = new ArrayList<>();
    }

    @NonNull
    @Override
    public List<MusicItem> provideMusics() {
        return musicList;
    }

    @Override
    public void provideMusics(OnProvideMusics onProvideMusics) {
        if (onProvideMusics != null)
            onProvideMusics.onSuccess(null);
    }

    @Override
    public void copyToMe(@NonNull List<MusicItem> anotherData) {
        musicList.addAll(anotherData);
        if (playingMusicIndex == NO_PLAYING_INDEX && !musicList.isEmpty())
            playingMusicIndex = 0;
        EventsManager.get().dispatchCurrentPlayingListChangeEvent(musicList);
    }

    @Override
    public void overrideToMe(@NonNull List<MusicItem> anotherData) {
        musicList.clear();
        musicList.addAll(anotherData);
        if (playingMusicIndex == NO_PLAYING_INDEX && !musicList.isEmpty())
            playingMusicIndex = 0;
        EventsManager.get().dispatchCurrentPlayingListChangeEvent(musicList);
    }

    @Nullable
    @Override
    public MusicItem getPlayingMusic() {
        if (playingMusicIndex >= 0 && playingMusicIndex < musicList.size()) {
            return musicList.get(playingMusicIndex);
        }
        return null;
    }

    @Override
    public void setPlayingMusic(int index) {
        boolean change = playingMusicIndex != index;
        if ((change) && index >= 0 && index < musicList.size()) {
            playingMusicIndex = index;
            EventsManager.get().dispatchPlayingIndexChangeEvent(index);
        }
    }

    @Override
    public int getPlayingMusicIndex() {
        return playingMusicIndex;
    }

    public interface CurrentPlayingListChangeListener {
        void onCurrentPlayingListChange(@NonNull List<MusicItem> musicItems);
    }
}