package com.lcc.imusic.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lcc.imusic.manager.EventsManager;
import com.lcc.imusic.service.MusicPlayService;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public abstract class MusicProgressCallActivity extends MusicPlayCallActivity implements MusicPlayService.MusicProgressListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventsManager.get().addMusicProgressListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventsManager.get().removeMusicProgressListener(this);

    }

    @Override
    public void onProgress(int second) {

    }

    @Override
    public void onBuffering(int percent) {

    }
}


