package com.lcc.imusic.model;

import android.support.annotation.NonNull;

import com.lcc.imusic.bean.MusicItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public class SimpleMusicProviderImpl implements MusicProvider {
    protected List<MusicItem> musicList;

    public SimpleMusicProviderImpl() {
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
    }

    @Override
    public void overrideToMe(@NonNull List<MusicItem> anotherData) {
        musicList.clear();
        musicList.addAll(anotherData);
    }


}
