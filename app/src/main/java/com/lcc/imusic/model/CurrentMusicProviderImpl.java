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
    private List<MusicItem> musicList;
    private int playingMusicIndex;

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
    public void copyToMe(@NonNull List<MusicItem> anotherData) {
        musicList.addAll(anotherData);
        EventsManager.get().dispatchCurrentPlayingListChangeEvent(musicList);
    }

    @Override
    public void overrideToMe(@NonNull List<MusicItem> anotherData) {
        musicList.clear();
        musicList.addAll(anotherData);
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
        playingMusicIndex = index;
        if (index >= 0 && index < musicList.size()) {
            boolean change = playingMusicIndex != index;
            if (change) {
                EventsManager.get().dispatchPlayingIndexChangeEvent(index);
            }
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