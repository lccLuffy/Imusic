package com.lcc.imusic.base.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.EventsManager;
import com.lcc.imusic.model.PlayingIndexChangeListener;
import com.lcc.imusic.service.MusicPlayService;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public abstract class AttachFragment extends BaseFragment implements MusicPlayService.MusicPlayListener, PlayingIndexChangeListener {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventsManager.get().addMusicPlayListener(this);
        EventsManager.get().addPlayingIndexChangeListener(this);
    }

    public void playMusic(int id) {
        Intent i = new Intent(context, MusicPlayService.class);
        i.putExtra("index", id);
        i.setAction(MusicPlayService.ACTION_PLAY_MUSIC_AT_INDEX);
        context.startService(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    public interface OnBindActivity {
        void onMusicList(List<MusicItem> list);
    }
}
