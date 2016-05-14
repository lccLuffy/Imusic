package com.lcc.imusic.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.DownloadAdapter;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.DlBean;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.service.DownLoadHelper;
import com.lcc.imusic.service.DownloadService;

import java.io.File;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class DownLoadActivity extends BaseActivity implements DownloadService.DownLoadEvent<MusicItem> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.download_title)
    TextView title;

    @Bind(R.id.download_subtitle)
    TextView subtitle;

    @Bind(R.id.download_percent)
    TextView percent;

    @Bind(R.id.download_progress)
    ProgressBar progressBar;

    DownloadAdapter downloadAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DownLoadHelper.get().addDownloadEvent(this);
        downloadAdapter = new DownloadAdapter();
        recyclerView.setAdapter(downloadAdapter);
        downloadAdapter.addData(DownLoadHelper.get().getDownloadQueue());
        DlBean<MusicItem> dlBean = DownLoadHelper.get().getDownloadingDlBean();
        if (dlBean != null) {
            title.setText(dlBean.data.title);
            subtitle.setText(dlBean.data.artist);
        } else {
            title.setText("没有下载的任务");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownLoadHelper.get().removeDownloadEvent(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    public void onStart(DlBean<MusicItem> dlBean) {
        title.setText(dlBean.data.title);
        subtitle.setText(dlBean.data.artist);
    }

    @Override
    public void onSuccess(DlBean<MusicItem> dlBean, File file) {
        downloadAdapter.remove(0);
    }

    @Override
    public void onFail(DlBean<MusicItem> dlBean, Throwable throwable) {
        toast("download " + dlBean.fileName + " failed," + throwable.getMessage());
    }

    @Override
    public void onProgress(DlBean<MusicItem> dlBean, int percent) {
        progressBar.setProgress(percent);
        this.percent.setText(percent+"%");
    }
}
