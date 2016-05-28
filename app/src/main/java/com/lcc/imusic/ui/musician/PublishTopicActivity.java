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

    @Bind(R.id.topic_title)
    EditText topicTitle;

    @Bind(R.id.topic_content)
    EditText topicContent;

    @Bind(R.id.topic_publish)
    Button topicPublish;

    @Bind(R.id.topic_progress)
    ProgressBar topic_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicianId = getIntent().getLongExtra("musicianId", 0L);
        topicPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_topic;
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
