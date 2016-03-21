package com.lcc.imusic.base;

import com.lcc.imusic.manager.EventsManager;
import com.lcc.imusic.service.MusicPlayService;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public abstract class MusicProgressCallActivity extends MusicServiceBindActivity implements MusicPlayService.MusicProgressListener {
    @Override
    protected void onBind(MusicPlayService.MusicServiceBind musicServiceBind) {
        super.onBind(musicServiceBind);
        EventsManager.get().addMusicProgressListener(this);
    }

    @Override
    protected void unBind(MusicPlayService.MusicServiceBind musicServiceBind) {
        super.unBind(musicServiceBind);
        EventsManager.get().removeMusicProgressListener(this);
    }

    @Override
    public void onProgress(int second) {

    }

    @Override
    public void onBuffering(int percent) {

    }
}


