package com.lcc.imusic.ui.musician;


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
public class MusicianMusicFragment extends AttachFragment implements LoadMoreAdapter.LoadMoreListener {

    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    SimpleMusicListAdapter adapter;

    public long musicianId;

    private int currentPageNum = 1;


    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {

        stateLayout.setInfoContentViewMargin(0, -275, 0, 0);


        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(1);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SimpleMusicListAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                playMusic(position);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPageNum = 1;
                getData(1);
            }
        });
        adapter.setLoadMoreListener(this);
        getData(currentPageNum);
    }

    private void getData(final int pageNum) {
        if (adapter.isDataEmpty())
            stateLayout.showProgressView();
        NetManager_.API().musicianSongs(musicianId, pageNum)
                .enqueue(new Callback<Msg<SongsBean>>() {
                    @Override
                    public void onResponse(Call<Msg<SongsBean>> call, Response<Msg<SongsBean>> response) {
                        SongsBean songsBean = response.body().Result;
                        if (songsBean != null) {
                            if (adapter.isDataEmpty() && songsBean.list.isEmpty()) {
                                stateLayout.showEmptyView();
                            } else {
                                stateLayout.showContentView();
                                List<MusicItem> list = RemoteMusicProvider.m2l(songsBean);
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
                            }

                        }
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<Msg<SongsBean>> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                        stateLayout.showErrorView(t.toString());
                    }
                });
    }

    @Override
    public void onPlayingIndexChange(int index) {
        super.onPlayingIndexChange(index);
        adapter.playingIndexChangeTo(index);
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
        return "TA的音乐";
    }

    @Override
    public void onLoadMore() {
        currentPageNum++;
        getData(currentPageNum);
    }
}
