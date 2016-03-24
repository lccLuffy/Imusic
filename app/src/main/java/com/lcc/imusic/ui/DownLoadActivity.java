package com.lcc.imusic.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.DownloadAdapter;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.bean.DlBean;
import com.lcc.imusic.service.DownLoadHelper;
import com.lcc.imusic.service.DownloadService;
import com.lcc.imusic.wiget.StateLayout;

import java.io.File;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class DownLoadActivity extends BaseActivity implements DownloadService.DownLoadEvent {
    @Bind(R.id.stateLayout)
    StateLayout stateLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    DownloadAdapter downloadAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DownLoadHelper.get().addDownloadEvent(this);
        downloadAdapter = new DownloadAdapter();
        recyclerView.setAdapter(downloadAdapter);
        downloadAdapter.addData(DownLoadHelper.get().getDownloadingDlBean());
        downloadAdapter.addData(DownLoadHelper.get().getDownloadQueue());
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
    public void onStart(DlBean dlBean) {
        toast("start download " + dlBean.fileName);
    }

    @Override
    public void onSuccess(DlBean dlBean, File file) {
        toast("download " + dlBean.fileName + "success");
        downloadAdapter.addData(DownLoadHelper.get().getDownloadingDlBean());
        downloadAdapter.addData(DownLoadHelper.get().getDownloadQueue());

    }

    @Override
    public void onFail(DlBean dlBean, Throwable throwable) {
        toast("download " + dlBean.fileName + " failed," + throwable.getMessage());
    }

    @Override
    public void onProgress(DlBean dlBean, int percent) {
        downloadAdapter.setProgress(percent);
        downloadAdapter.notifyItemChanged(0);
    }
}
