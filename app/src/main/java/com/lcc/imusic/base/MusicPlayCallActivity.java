package com.lcc.imusic.base;

import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.EventsManager;
import com.lcc.imusic.model.PlayingIndexChangeListener;
import com.lcc.imusic.service.MusicPlayService;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public abstract class MusicPlayCallActivity extends MusicServiceBindActivity implements
        MusicPlayService.MusicPlayListener, PlayingIndexChangeListener {
    @Override
    protected void onBind(MusicPlayService.MusicServiceBind musicServiceBind) {
        super.onBind(musicServiceBind);
        EventsManager.get().addMusicPlayListener(this);
        EventsManager.get().addPlayingIndexChangeListener(this);
    }

    @Override
    protected void unBind(MusicPlayService.MusicServiceBind musicServiceBind) {
        super.unBind(musicServiceBind);
        EventsManager.get().removeMusicPlayListener(this);
        EventsManager.get().removePlayingIndexChangeListener(this);
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
}


