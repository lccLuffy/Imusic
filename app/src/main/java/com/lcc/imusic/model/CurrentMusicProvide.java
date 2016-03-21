package com.lcc.imusic.model;

import com.lcc.imusic.bean.MusicItem;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public interface CurrentMusicProvide extends MusicProvider {
    MusicItem getPlayingMusic();

    void setPlayingMusic(int index);

    int getPlayingMusicIndex();
}
