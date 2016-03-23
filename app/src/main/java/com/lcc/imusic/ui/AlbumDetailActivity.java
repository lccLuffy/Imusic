package com.lcc.imusic.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.adapter.SimpleMusicListAdapter;
import com.lcc.imusic.base.activity.PlayBarActivity;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.OnProvideMusics;
import com.lcc.imusic.model.RemoteMusicProvider;
import com.lcc.stateLayout.StateLayout;

import java.util.List;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class AlbumDetailActivity extends PlayBarActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    SimpleMusicListAdapter simpleMusicListAdapter;

    private long id = 58451795;
    RemoteMusicProvider remoteMusicProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("专辑详情");
        refreshLayout.setColorSchemeResources(R.color.selectedRed);
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        simpleMusicListAdapter = new SimpleMusicListAdapter();
        recyclerView.setAdapter(simpleMusicListAdapter);
        simpleMusicListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                playMusic(position);
            }
        });
        remoteMusicProvider = new RemoteMusicProvider(id);
        getData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_album_detail;
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
        if (simpleMusicListAdapter.getItemCount() == 0)
            stateLayout.showProgressView();
        else
            stateLayout.showContentView();
        remoteMusicProvider.provideMusics(new OnProvideMusics() {
            @Override
            public void onSuccess(List<MusicItem> musicItems) {
                simpleMusicListAdapter.setData(musicItems);
                refreshLayout.setRefreshing(false);
                if (musicItems.isEmpty()) {
                    stateLayout.showEmptyView();
                } else {
                    stateLayout.showContentView();
                }
            }

            @Override
            public void onFail(String reason) {
                stateLayout.showErrorView("网络出错");
                refreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onRefresh() {
        getData();
    }
}
