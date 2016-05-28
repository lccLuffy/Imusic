package com.lcc.imusic.ui.musician;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.Msg;
import com.lcc.imusic.manager.NetManager_;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishTopicActivity extends BaseActivity {

    private long musicianId;
    private long topicId;
    @Bind(R.id.topic_title)
    EditText topicTitle;

    @Bind(R.id.topic_content)
    EditText topicContent;

    @Bind(R.id.topic_publish)
    Button topicPublish;

    @Bind(R.id.topic_progress)
    ProgressBar topic_progress;

    public static final int TYPE_PUBLISH = 0;
    public static final int TYPE_UPDATE = 1;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicianId = getIntent().getLongExtra("musicianId", 0L);
        type = getIntent().getIntExtra("type", TYPE_PUBLISH);

        topicPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_UPDATE) {
                    update();
                } else {
                    publish();
                }
            }
        });

        if (type == TYPE_UPDATE) {
            topicId = getIntent().getLongExtra("topicId", 0);
            String title = getIntent().getStringExtra("title");
            String content = getIntent().getStringExtra("content");
            topicTitle.setText(title);
            topicContent.setText(content);
            topicPublish.setText("修改");
            setTitle("修改话题");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_topic;
    }

    public void update() {
        String title = topicTitle.getText().toString().trim();
        String content = topicContent.getText().toString().trim();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            toast("不能为空");
            return;
        }

        topicPublish.setVisibility(View.GONE);
        topic_progress.setVisibility(View.VISIBLE);
        NetManager_.API().updateTopic(topicId, title, content).enqueue(new Callback<Msg<String>>() {
            @Override
            public void onResponse(Call<Msg<String>> call, Response<Msg<String>> response) {
                if (response.body().Code == 100) {
                    toast("修改成功");
                    finish();
                } else {
                    toast("修改失败," + response.body().Msg);
                }
            }

            @Override
            public void onFailure(Call<Msg<String>> call, Throwable t) {
                toast("修改失败," + t.toString());
            }
        });

    }

    public void publish() {
        String title = topicTitle.getText().toString().trim();
        String content = topicContent.getText().toString().trim();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            toast("不能为空");
            return;
        }

        topicPublish.setVisibility(View.GONE);
        topic_progress.setVisibility(View.VISIBLE);

        NetManager_.API().publishTopic(musicianId, title, content).enqueue(new Callback<Msg<JsonObject>>() {
            @Override
            public void onResponse(Call<Msg<JsonObject>> call, Response<Msg<JsonObject>> response) {
                topicPublish.setVisibility(View.VISIBLE);
                topic_progress.setVisibility(View.GONE);
                if (response.body().Code == 100) {
                    toast("发布成功");
                    finish();
                } else {
                    toast("发布失败");
                }
            }

            @Override
            public void onFailure(Call<Msg<JsonObject>> call, Throwable t) {
                topicPublish.setVisibility(View.VISIBLE);
                topic_progress.setVisibility(View.GONE);
                toast("发布失败," + t.toString());
            }
        });
    }
}
