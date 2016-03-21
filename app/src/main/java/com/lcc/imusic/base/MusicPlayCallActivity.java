package com.lcc.imusic.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.EventsManager;
import com.lcc.imusic.model.CurrentMusicProviderImpl;
import com.lcc.imusic.model.PlayingIndexChangeListener;
import com.lcc.imusic.service.MusicPlayService;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public abstract class MusicPlayCallActivity extends MusicServiceBindActivity implements
        MusicPlayService.MusicPlayListener, PlayingIndexChangeListener, CurrentMusicProviderImpl.CurrentPlayingListChangeListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventsManager.get().addMusicPlayListener(this);
        EventsManager.get().addPlayingIndexChangeListener(this);
        EventsManager.get().addCurrentPlayingListChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventsManager.get().removePlayingIndexChangeListener(this);
        EventsManager.get().removeMusicPlayListener(this);
        EventsManager.get().removeCurrentPlayingListChangeListener(this);
    }

    @Override
    public void onMusicWillPlay(MusicItem musicItem) {

    }

    @Override
    public void onMusicReady(MusicItem musicItem) {

    }

    @Override
    public void onPlayingIndexChange(int index) {

    }

    @Override
    public void onCurrentPlayingListChange(@NonNull List<MusicItem> musicItems) {

    }
}


