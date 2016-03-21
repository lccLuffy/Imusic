package com.lcc.imusic.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lcc.imusic.MainActivity;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.EventsManager;
import com.lcc.imusic.model.PlayingIndexChangeListener;
import com.lcc.imusic.service.MusicPlayService;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public abstract class AttachFragment extends BaseFragment implements MusicPlayService.MusicPlayListener, PlayingIndexChangeListener {
    protected MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventsManager.get().addMusicPlayListener(this);
        EventsManager.get().addPlayingIndexChangeListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
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
