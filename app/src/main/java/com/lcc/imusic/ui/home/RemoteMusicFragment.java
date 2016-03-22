package com.lcc.imusic.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.adapter.SimpleMusicListAdapter;
import com.lcc.imusic.base.fragment.AttachFragment;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.OnProvideMusics;
import com.lcc.imusic.model.RemoteMusicProvider;

import java.util.List;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class RemoteMusicFragment extends AttachFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    SimpleMusicListAdapter simpleMusicListAdapter;

    private long id = 58451795;
    RemoteMusicProvider remoteMusicProvider;

    public static RemoteMusicFragment newInstance(int id) {
        RemoteMusicFragment fragment = new RemoteMusicFragment();
        fragment.id = id;
        return fragment;
    }

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        refreshLayout.setColorSchemeResources(R.color.selectedRed);
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        simpleMusicListAdapter = new SimpleMusicListAdapter();
        recyclerView.setAdapter(simpleMusicListAdapter);
        simpleMusicListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mainActivity.playMusic(position);
            }
        });
        remoteMusicProvider = new RemoteMusicProvider(id);
        getData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        simpleMusicListAdapter.onDestroy();
        simpleMusicListAdapter = null;
    }


    @Override
    public void onPlayingIndexChange(int index) {
        if (simpleMusicListAdapter != null)
            simpleMusicListAdapter.playingIndexChangeTo(index);
    }

    public void getData() {
        remoteMusicProvider.provideMusics(new OnProvideMusics() {
            @Override
            public void onSuccess(List<MusicItem> musicItems) {
                simpleMusicListAdapter.setData(musicItems);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFail(String reason) {
                toast(reason);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_musiclist;
    }

    @Override
    public String toString() {
        return "联网音乐";
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
