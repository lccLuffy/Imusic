package com.lcc.imusic.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;
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

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DownLoadHelper.get().addDownloadEvent(this);
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
    public void onDownLoadStart() {
        toast("start");
    }

    @Override
    public void onSuccess(File file) {
        toast(file.getAbsolutePath());
    }

    @Override
    public void onFail(Throwable throwable) {
        toast(throwable.toString());
    }

    @Override
    public void onProgress(int percent) {
        toast(String.valueOf(percent));
    }
}
