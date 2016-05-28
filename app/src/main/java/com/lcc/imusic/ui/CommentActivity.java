package com.lcc.imusic.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.lcc.imusic.R;
import com.lcc.imusic.adapter.CommentAdapter;
import com.lcc.imusic.adapter.LoadMoreAdapter;
import com.lcc.imusic.adapter.OnItemClickListener;
import com.lcc.imusic.adapter.OnItemLongClickListener;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.CommentBean;
import com.lcc.imusic.bean.CommentItem;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.manager.NetManager_;
import com.lcc.imusic.manager.UserManager;
import com.lcc.imusic.wiget.StateLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    @Bind(R.id.comment_progress)
    ProgressBar comment_progress;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.commentEditText)
    EditText commentEditView;

    @Bind(R.id.commentSubmit)
    Button commentSubmitBtn;

    private CommentAdapter adapter;

    private long songId;

    private int currentPage = 1;

    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songId = getIntent().getLongExtra("songId", 0L);
        String title = getIntent().getStringExtra("songName");
        setTitle(title + "的评论");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout.setOnRefreshListener(this);
        commentSubmitBtn.setEnabled(false);
        commentSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();
            }
        });
        commentEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    commentSubmitBtn.setEnabled(false);
                } else {
                    commentSubmitBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        adapter = new CommentAdapter();
        adapter.setLoadMoreListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showCommentDialog(adapter.getData(position));
            }
        });

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final int position) {
                new AlertDialog
                        .Builder(CommentActivity.this)
                        .setItems(new String[]{"复制"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                        clipboardManager.setPrimaryClip(ClipData.newPlainText("comment", adapter.getData(position).content));
                                        toast("已复制到粘贴板");
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });

        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        getData(1);
    }

    private void showCommentDialog(CommentItem data) {

    }

    private void comment() {
        final String content = commentEditView.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        commentSubmitBtn.setVisibility(View.GONE);
        comment_progress.setVisibility(View.VISIBLE);

        NetManager_.API().commentToSong(songId, content).enqueue(new Callback<Msg<JsonObject>>() {
            @Override
            public void onResponse(Call<Msg<JsonObject>> call, Response<Msg<JsonObject>> response) {
                commentSubmitBtn.setVisibility(View.VISIBLE);
                comment_progress.setVisibility(View.GONE);
                Msg<JsonObject> msg = response.body();
                if (msg != null) {
                    if (msg.Code == 100) {
                        stateLayout.showContentView();
                        commentEditView.setText("");
                        toast("评论成功");
                        CommentItem commentItem = new CommentItem();
                        commentItem.enable = 1;
                        commentItem.content = content;
                        commentItem.avatar = UserManager.avatarWithOutDomain();
                        commentItem.authorName = UserManager.username();
                        commentItem.songid = songId;

                        if (dateFormat == null)
                            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm::ss", Locale.CHINA);
                        commentItem.addtime = dateFormat.format(new Date());
                        adapter.insert(0, commentItem);
                        recyclerView.smoothScrollToPosition(0);
                    } else {
                        toast("评论失败," + msg.Msg);
                    }
                } else {
                    toast("评论失败");
                }
            }

            @Override
            public void onFailure(Call<Msg<JsonObject>> call, Throwable t) {
                commentSubmitBtn.setVisibility(View.VISIBLE);
                comment_progress.setVisibility(View.GONE);
                toast("评论失败," + t.getMessage());
            }
        });
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