package com.lcc.imusic.model;

import android.support.annotation.NonNull;

import com.lcc.imusic.bean.MusicItem;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public interface MusicProvider {
    @NonNull
    List<MusicItem> provideMusics();

    void copyToMe(@NonNull List<MusicItem> anotherData);

    void overrideToMe(@NonNull List<MusicItem> anotherData);
}
