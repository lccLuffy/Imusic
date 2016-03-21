package com.lcc.imusic.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.EventsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class CurrentMusicProviderImpl implements CurrentMusicProvide {
    List<MusicItem> musicList;
    private int playingMusicIndex;

    private static CurrentMusicProviderImpl musicProvider;

    public static CurrentMusicProviderImpl getMusicProvider(@NonNull Context context) {
        if (musicProvider == null)
            musicProvider = new CurrentMusicProviderImpl(context);
        return musicProvider;
    }

    private CurrentMusicProviderImpl(@NonNull Context context) {
        musicList = new ArrayList<>();
    }

    @NonNull
    @Override
    public List<MusicItem> provideMusics() {
        return musicList;
    }

    @Nullable
    @Override
    public MusicItem getPlayingMusic() {
        if (playingMusic != null)
            return playingMusic;
        if (musicList != null && !musicList.isEmpty()) {
            return musicList.get(0);
        }
        return null;
    }

    private MusicItem playingMusic;

    @Override
    public void setPlayingMusic(int index) {
        boolean change = playingMusicIndex != index;
        playingMusicIndex = index;
        playingMusic = musicList.get(index);
        if (change) {
            EventsManager.get().dispatchPlayingIndexChangeListener(index);
        }
    }

    @Override
    public int getPlayingMusicIndex() {
        return playingMusicIndex;
    }
}
