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
import com.lcc.imusic.bean.SongsBean;
import com.lcc.imusic.model.OnProvideMusics;
import com.lcc.imusic.model.RemoteMusicProvider;
import com.lcc.imusic.utils.PrfUtil;
import com.lcc.imusic.wiget.StateLayout;

import java.util.List;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class RemoteMusicFragment extends AttachFragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    SimpleMusicListAdapter simpleMusicListAdapter;

    private long id = 147085039;
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
                playMusic(position);
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
    public void onCreateView() {
        long tmpId = PrfUtil.get().getLong("current_play_list", id);
        if (tmpId != id) {
            id = tmpId;
            remoteMusicProvider.setId(id);
            getId();
        }
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
            public void onSuccess(SongsBean songsBean) {
                List<MusicItem> list = RemoteMusicProvider.m2l(songsBean);
                simpleMusicListAdapter.setData(list);
                refreshLayout.setRefreshing(false);
                if (list.isEmpty()) {
                    stateLayout.showEmptyView();
                } else {
                    stateLayout.showContentView();
                }
            }

            @Override
            public void onFail(Throwable reason) {
                stateLayout.showErrorView("网络出错");
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
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
