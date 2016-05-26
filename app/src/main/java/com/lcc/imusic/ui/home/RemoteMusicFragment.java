package com.lcc.imusic.ui.home;


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
public class RemoteMusicFragment extends AttachFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreAdapter.LoadMoreListener {
    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    SimpleMusicListAdapter adapter;

    private int currentPageNum = 1;


    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        refreshLayout.setColorSchemeResources(R.color.selectedRed);
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new SimpleMusicListAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                playMusic(position);
            }
        });
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        getData(1);
        adapter.setLoadMoreListener(this);
        adapter.canLoadMore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.onDestroy();
        adapter = null;
    }


    @Override
    public void onPlayingIndexChange(int index) {
        if (adapter != null)
            adapter.playingIndexChangeTo(index);
    }

    public void getData(final int pageNum) {
        if (adapter.getItemCount() == 0)
            stateLayout.showProgressView();

        NetManager_.API().songs(pageNum).enqueue(new Callback<Msg<SongsBean>>() {
            @Override
            public void onResponse(Call<Msg<SongsBean>> call, Response<Msg<SongsBean>> response) {
                SongsBean songsBean = response.body().Result;
                refreshLayout.setRefreshing(false);
                if (songsBean != null) {
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
                    if (adapter.isDataEmpty()) {
                        stateLayout.showEmptyView();
                    } else {
                        stateLayout.showContentView();
                    }
                }

            }

            @Override
            public void onFailure(Call<Msg<SongsBean>> call, Throwable t) {
                if (adapter.isDataEmpty()) {
                    stateLayout.showErrorView("网络出错");
                }
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
