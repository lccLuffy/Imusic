package com.lcc.imusic.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.adapter.SimpleMusicListAdapter;
import com.lcc.imusic.base.AttachFragment;
import com.lcc.imusic.model.LocalMusicProvider;

import butterknife.Bind;


/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class HotMusicianFragment extends AttachFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    SimpleMusicListAdapter simpleMusicListAdapter;

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        simpleMusicListAdapter = new SimpleMusicListAdapter();
        recyclerView.setAdapter(simpleMusicListAdapter);

        simpleMusicListAdapter.setData(LocalMusicProvider.getMusicProvider(context).provideMusics());
        simpleMusicListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mainActivity.playMusic(position);
            }
        });
    }

    @Override
    public void onPlayingIndexChange(int index) {
        super.onPlayingIndexChange(index);
        simpleMusicListAdapter.playingIndexChangeTo(index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        simpleMusicListAdapter.onDestroy();
        simpleMusicListAdapter = null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.test_fragment;
    }

    @Override
    public String toString() {
        return "本地音乐";
    }

}
