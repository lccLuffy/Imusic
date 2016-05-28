package com.lcc.imusic.ui.musician;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.adapter.LoadMoreAdapter;
import com.lcc.imusic.adapter.TopicReplyAdapter;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.Club;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.bean.TopicReply;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.utils.Json;
import com.lcc.imusic.wiget.StateLayout;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicActivity extends BaseActivity implements LoadMoreAdapter.LoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.club_name)
    TextView clubName;

    @Bind(R.id.club_text)
    TextView clubText;

    @Bind(R.id.club_viewCount)
    TextView club_viewCount;

    @Bind(R.id.club_replyCount)
    TextView club_replyCount;

    @Bind(R.id.club_time)
    TextView club_time;

    @Bind(R.id.auth_name)
    TextView auth_name;


    @Bind(R.id.club_avatar)
    ImageView club_avatar;


    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private long topicId;

    private int currentPage = 1;

    TopicReplyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Club.TopicItem topicItem = (Club.TopicItem) getIntent().getSerializableExtra("topic");
        topicId = topicItem.id;
        setTitle(topicItem.title);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicReplyAdapter();
        adapter.setLoadMoreListener(this);
        recyclerView.setAdapter(adapter);

        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        bind(topicItem);
        getTopicReplies(1);
    }

    private void bind(Club.TopicItem topicItem) {
        clubName.setText(topicItem.title);
        clubText.setText(topicItem.text);
        club_replyCount.setText("回复:" + topicItem.replycount);
        club_viewCount.setText("查看次数:" + topicItem.viewscount);
        club_time.setText(topicItem.addtime);
        auth_name.setText(topicItem.authorNmae);

        Glide.with(this)
                .load(NetManager_.DOMAIN + topicItem.avatar)
                .into(club_avatar);
    }

    public void getTopicReplies(final int pageNum) {
        if (adapter.isDataEmpty())
            stateLayout.showProgressView();
        NetManager_.API().topicReplies(topicId, pageNum).enqueue(new Callback<Msg<TopicReply>>() {
            @Override
            public void onResponse(Call<Msg<TopicReply>> call, Response<Msg<TopicReply>> response) {
                TopicReply topicReply = response.body().Result;

                Logger.i(Json.toJson(topicReply));
                refreshLayout.setRefreshing(false);
                if (topicReply != null) {
                    if (pageNum == 1) {
                        adapter.setData(topicReply.list);
                    } else {
                        if (topicReply.list.isEmpty()) {
                            adapter.noMoreData();
                        } else {
                            adapter.addData(topicReply.list);
                        }
                    }
                }

                if (adapter.isDataEmpty()) {
                    stateLayout.showEmptyView("还没有评论");
                } else {
                    stateLayout.showContentView();
                }
            }

            @Override
            public void onFailure(Call<Msg<TopicReply>> call, Throwable t) {
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
    public void onLoadMore() {
        currentPage++;
        getTopicReplies(currentPage);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_topic;
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        getTopicReplies(1);
    }
}
