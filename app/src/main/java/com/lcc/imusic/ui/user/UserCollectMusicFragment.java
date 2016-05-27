package com.lcc.imusic.ui.user;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.LoadMoreAdapter;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.adapter.SimpleMusicListAdapter;
import com.lcc.imusic.base.fragment.AttachFragment;
import com.lcc.imusic.bean.CollectedSongs;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.wiget.StateLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class UserCollectMusicFragment extends AttachFragment implements LoadMoreAdapter.LoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    SimpleMusicListAdapter adapter;

    private int currentPage = 1;

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        stateLayout.setInfoContentViewMargin(0, -275, 0, 0);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SimpleMusicListAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                playMusic(position);
            }
        });
        adapter.setLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        getData(1);
    }

    @Override
    public void onPlayingIndexChange(int index) {
        super.onPlayingIndexChange(index);
        adapter.playingIndexChangeTo(index);
    }

    private void getData(final int pageNum) {
        if (adapter.isDataEmpty())
            stateLayout.showProgressView();

        NetManager_.API().collectedSongs(pageNum).enqueue(new Callback<Msg<CollectedSongs>>() {
            @Override
            public void onResponse(Call<Msg<CollectedSongs>> call, Response<Msg<CollectedSongs>> response) {
                CollectedSongs collectedSongs = response.body().Result;
                refreshLayout.setRefreshing(false);
                if (collectedSongs != null) {
                    if (userCenterActivity != null) {
                        userCenterActivity.setCollectedSongsNum(collectedSongs.totalRow);
                    }
                    List<MusicItem> list = new ArrayList<>();
                    for (CollectedSongs.CollectedSongItem item : collectedSongs.list) {
                        MusicItem musicItem = new MusicItem();
                        musicItem.id = item.songid;
                        musicItem.title = item.name;
                        list.add(musicItem);
                    }
                    if (pageNum == 1) {
                        adapter.canLoadMore();
                        adapter.setData(list);
                    } else {
                        if (list.isEmpty()) {
                            adapter.noMoreData();
                        } else {
                            adapter.addData(list);
                        }
                    }

                    if (adapter.isDataEmpty()) {
                        stateLayout.showEmptyView();
                    } else {
                        stateLayout.showContentView();
                    }
                }
            }

            @Override
            public void onFailure(Call<Msg<CollectedSongs>> call, Throwable t) {
                if (adapter.isDataEmpty()) {
                    stateLayout.showErrorView("网络出错");
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private UserCenterActivity userCenterActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserCenterActivity) {
            userCenterActivity = (UserCenterActivity) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.onDestroy();
        adapter = null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public String toString() {
        return "我的收藏";
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        getData(currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        getData(1);
    }
}
