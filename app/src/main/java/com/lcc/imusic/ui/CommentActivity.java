package com.lcc.imusic.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.CommentAdapter;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.CommentBean;
import com.lcc.imusic.wiget.StateLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/24.
 */
public class CommentActivity extends BaseActivity {
    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    CommentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("songName");
        setTitle(title + "的评论");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter();
        recyclerView.setAdapter(adapter);

        List<CommentBean> commentBeen = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            commentBeen.add(new CommentBean("lcc_luffy",
                    "http://upload.jianshu.io/users/upload_avatars/1438934/e9fe359cbaf2.jpeg",
                    "commentcommentcommentcommentcommentcommentcommentcommentcommentcomment", "3月25日"));
        }
        adapter.setData(commentBeen);
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
}
