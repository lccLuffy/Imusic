package com.lcc.imusic.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.CommentAdapter;
import com.lcc.imusic.adapter.LoadMoreAdapter;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.CommentBean;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.wiget.StateLayout;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lcc_luffy on 2016/3/24.
 */
public class CommentActivity extends BaseActivity implements LoadMoreAdapter.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.commentEditText)
    EditText commentEditView;


    private CommentAdapter adapter;

    private long songId;

    private int currentPage = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songId = getIntent().getLongExtra("songId", 0L);
        String title = getIntent().getStringExtra("songName");
        setTitle(title + "的评论");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout.setOnRefreshListener(this);

        adapter = new CommentAdapter();
        adapter.setLoadMoreListener(this);
        recyclerView.setAdapter(adapter);
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        getData(1);
    }

    public void getData(final int pageNum) {
        if (adapter.isDataEmpty()) {
            stateLayout.showProgressView();
        }

        NetManager_.API().songComment(songId, pageNum).enqueue(new Callback<Msg<CommentBean>>() {
            @Override
            public void onResponse(Call<Msg<CommentBean>> call, Response<Msg<CommentBean>> response) {
                CommentBean commentBean = response.body().Result;
                refreshLayout.setRefreshing(false);
                if (commentBean != null) {
                    stateLayout.showContentView();
                    if (pageNum == 1) {
                        if (commentBean.list.isEmpty()) {
                            stateLayout.showEmptyView("还没有评论");
                        } else {
                            adapter.setData(commentBean.list);
                        }
                    } else {
                        if (commentBean.list.isEmpty()) {
                            adapter.noMoreData();
                        } else {
                            adapter.addData(commentBean.list);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Msg<CommentBean>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                if (adapter.isDataEmpty()) {
                    stateLayout.showErrorView(t.getMessage());
                }
            }
        });
    }

    public static void jumpToMe(Context context, long songId, String songName) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("songId", songId);
        intent.putExtra("songName", songName);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        getData(currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        adapter.canLoadMore();
        getData(1);
    }
}