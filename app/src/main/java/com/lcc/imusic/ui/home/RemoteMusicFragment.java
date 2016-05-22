package com.lcc.imusic.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.LoadMoreAdapter;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.adapter.SimpleMusicListAdapter;
import com.lcc.imusic.base.fragment.AttachFragment;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.bean.SongsBean;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.model.RemoteMusicProvider;
import com.lcc.imusic.wiget.StateLayout;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class RemoteMusicFragment extends AttachFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreAdapter.LoadMoreListener {
    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    SimpleMusicListAdapter simpleMusicListAdapter;

    private int currentPageNum = 1;


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
        getData(1);
        simpleMusicListAdapter.setLoadMoreListener(this);
        simpleMusicListAdapter.canLoadMore();
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

    public void getData(final int pageNum) {
        if (simpleMusicListAdapter.getItemCount() == 0)
            stateLayout.showProgressView();
        else
            stateLayout.showContentView();

        NetManager_.API().songs(pageNum).enqueue(new Callback<Msg<SongsBean>>() {
            @Override
            public void onResponse(Call<Msg<SongsBean>> call, Response<Msg<SongsBean>> response) {
                SongsBean songsBean = response.body().Result;
                if (songsBean != null) {
                    List<MusicItem> list = RemoteMusicProvider.m2l(songsBean);
                    if (pageNum == 1) {
                        simpleMusicListAdapter.canLoadMore();
                        simpleMusicListAdapter.setData(list);
                    } else {
                        if (list.isEmpty()) {
                            simpleMusicListAdapter.noMoreData();
                        } else {
                            simpleMusicListAdapter.addData(list);
                        }
                    }
                    refreshLayout.setRefreshing(false);
                    if (simpleMusicListAdapter.isDataEmpty()) {
                        stateLayout.showEmptyView();
                    } else {
                        stateLayout.showContentView();
                    }
                }

            }

            @Override
            public void onFailure(Call<Msg<SongsBean>> call, Throwable t) {
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
        currentPageNum = 1;
        getData(currentPageNum);
    }

    @Override
    public void onLoadMore() {
        currentPageNum++;
        getData(currentPageNum);
    }
}
