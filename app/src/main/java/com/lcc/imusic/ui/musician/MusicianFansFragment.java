package com.lcc.imusic.ui.musician;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.JsonObject;
import com.lcc.imusic.R;
import com.lcc.imusic.adapter.ClubAdapter;
import com.lcc.imusic.adapter.LoadMoreAdapter;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.adapter.OnItemLongClickListener;
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
public class MusicianFansFragment extends AttachFragment implements LoadMoreAdapter.LoadMoreListener, OnItemLongClickListener {

    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    ClubAdapter adapter;

    @Bind(R.id.add_topic)
    FloatingActionButton add_topic;

    public long musicianId;
    private int currentPage = 1;

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        stateLayout.setInfoContentViewMargin(0, -275, 0, 0);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ClubAdapter();
        recyclerView.setAdapter(adapter);
        add_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishTopic();
            }
        });
        stateLayout.setEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(1);
            }
        });
        stateLayout.setEmptyClickNotice("轻触屏幕添加");
        stateLayout.setEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishTopic();
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
                Intent intent = new Intent(context, TopicActivity.class);
                intent.putExtra("topic", adapter.getData(position));
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(this);

        adapter.setLoadMoreListener(this);
        getData(1);
    }


    private void publishTopic() {
        Intent intent = new Intent(context, PublishTopicActivity.class);
        intent.putExtra("musicianId", musicianId);
        intent.putExtra("type", PublishTopicActivity.TYPE_PUBLISH);
        startActivityForResult(intent, 1234);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234) {
            recyclerView.smoothScrollToPosition(0);
            currentPage = 1;
            getData(1);
        }
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
        return R.layout.fragment_musician_fans;
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

    @Override
    public boolean onItemLongClick(final int position) {
        AlertDialog dialog = new AlertDialog
                .Builder(context)
                .setItems(new String[]{"删除", "修改"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                NetManager_.API().deleteTopic(adapter.getData(position).id)
                                        .enqueue(new Callback<Msg<JsonObject>>() {
                                            @Override
                                            public void onResponse(Call<Msg<JsonObject>> call, Response<Msg<JsonObject>> response) {
                                                if (response.body().Code == 100) {
                                                    toast("删除成功");
                                                    adapter.remove(position);
                                                } else {
                                                    toast("删除失败," + response.body().Msg);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Msg<JsonObject>> call, Throwable t) {
                                                toast("删除失败," + t.toString());
                                            }
                                        });

                                break;
                            case 1:
                                Club.TopicItem topicItem = adapter.getData(position);
                                Intent intent = new Intent(context, PublishTopicActivity.class);
                                intent.putExtra("topicId", topicItem.id);
                                intent.putExtra("title", topicItem.title);
                                intent.putExtra("content", topicItem.text);
                                intent.putExtra("type", PublishTopicActivity.TYPE_UPDATE);
                                startActivityForResult(intent, 1234);
                                break;
                        }
                        dialog.dismiss();
                    }
                })

                .create();
        dialog.show();

        return true;
    }
}
