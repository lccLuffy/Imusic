package com.lcc.imusic.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.LoadMoreAdapter;
import com.lcc.imusic.adapter.MusicianRankAdapter;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.base.fragment.AttachFragment;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.bean.Musician;
import com.lcc.imusic.bean.MusiciansBean;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.ui.musician.MusicianDetailActivity;
import com.lcc.imusic.wiget.StateLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public class MusicianListFragment extends AttachFragment implements LoadMoreAdapter.LoadMoreListener {

    @Bind(R.id.stateLayout)
    StateLayout stateLayout;


    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    MusicianRankAdapter musicianRankAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        musicianRankAdapter = new MusicianRankAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(musicianRankAdapter);
        musicianRankAdapter.setLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPageNum = 1;
                getData(currentPageNum);
            }
        });

        musicianRankAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(context, MusicianDetailActivity.class);
                i.putExtra("id", musicianRankAdapter.getData(position).id);
                i.putExtra("avatar", musicianRankAdapter.getData(position).avatar);
                i.putExtra("name", musicianRankAdapter.getData(position).name);
                startActivity(i);
            }
        });

        getData(1);
    }

    private int currentPageNum = 1;

    private void getData(final int pageNum) {
        if (musicianRankAdapter.isDataEmpty()) {
            stateLayout.showProgressView();
        }

        NetManager_.API().musicians(pageNum).enqueue(new Callback<Msg<MusiciansBean>>() {
            @Override
            public void onResponse(Call<Msg<MusiciansBean>> call, Response<Msg<MusiciansBean>> response) {
                MusiciansBean musiciansBean = response.body().Result;
                if (musiciansBean != null) {
                    stateLayout.showContentView();
                    List<Musician> list = new ArrayList<>();
                    for (MusiciansBean.MusicianItem item : musiciansBean.list) {
                        list.add(new Musician(item.id, NetManager_.DOMAIN + item.avatar, item.nickname));
                    }
                    if (pageNum == 1) {
                        musicianRankAdapter.setData(list);
                    } else {
                        if (list.isEmpty()) {
                            musicianRankAdapter.noMoreData();
                        } else {
                            musicianRankAdapter.addData(list);
                        }
                    }
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Msg<MusiciansBean>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                if (musicianRankAdapter.isDataEmpty()) {
                    stateLayout.showErrorView(t.toString());
                }
            }
        });
    }


    @Override
    public void onLoadMore() {
        currentPageNum++;
        getData(currentPageNum);
    }
}
