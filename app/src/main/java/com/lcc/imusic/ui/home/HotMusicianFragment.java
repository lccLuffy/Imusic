package com.lcc.imusic.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicItemAdapter;
import com.lcc.imusic.base.AttachFragment;
import com.lcc.imusic.model.LocalMusicProvider;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;

import butterknife.Bind;


/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class HotMusicianFragment extends AttachFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    MusicItemAdapter musicItemAdapter;

    @Override
    public void initialize(@Nullable Bundle savedInstanceState)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        musicItemAdapter = new MusicItemAdapter(context);
        recyclerView.setAdapter(musicItemAdapter);
        musicItemAdapter.getLoadMoreFooter().showNoMoreView();
        musicItemAdapter.initData(LocalMusicProvider.getMusicProvider(context).provideMusics());
        musicItemAdapter.setOnItemClickListener(new NiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mainActivity.playMusic(position);
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.test_fragment;
    }

    @Override
    public String toString() {
        return "热门音乐人";
    }

}
