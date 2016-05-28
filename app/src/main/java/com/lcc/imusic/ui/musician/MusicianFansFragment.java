package com.lcc.imusic.ui.musician;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.ClubAdapter;
import com.lcc.imusic.adapter.LoadMoreAdapter;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.base.fragment.AttachFragment;
import com.lcc.imusic.bean.Club;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.wiget.StateLayout;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class MusicianFansFragment extends AttachFragment implements LoadMoreAdapter.LoadMoreListener {

    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    ClubAdapter adapter;

    public long musicianId;
    private int currentPage = 1;

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        stateLayout.setInfoContentViewMargin(0, -275, 0, 0);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ClubAdapter();
        recyclerView.setAdapter(adapter);

        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(1);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                getData(1);
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        adapter.setLoadMoreListener(this);
        getData(1);
    }

    private void getData(final int pageNum) {
        if (adapter.isDataEmpty())
            stateLayout.showProgressView();
        NetManager_.API().club(musicianId, pageNum).enqueue(new Callback<Msg<Club>>() {
            @Override
            public void onResponse(Call<Msg<Club>> call, Response<Msg<Club>> response) {
                Club club = response.body().Result;
                refreshLayout.setRefreshing(false);
                if (club != null) {
                    if (pageNum == 1) {
                        adapter.setData(club.list);
                    } else {
                        if (club.list.isEmpty()) {
                            adapter.noMoreData();
                        } else {
                            adapter.addData(club.list);
                        }
                    }
                }

                if (adapter.isDataEmpty()) {
                    stateLayout.showEmptyView("TA还没有粉丝团呢");
                } else {
                    stateLayout.showContentView();
                }
            }

            @Override
            public void onFailure(Call<Msg<Club>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                if (adapter.isDataEmpty()) {
                    stateLayout.showErrorView(t.toString());
                } else {
                    adapter.noMoreData(t.toString());
                }
            }
        });
    }

    @Override
    public void onPlayingIndexChange(int index) {
        super.onPlayingIndexChange(index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public String toString() {
        return "粉丝团";
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        getData(currentPage);
    }
}
