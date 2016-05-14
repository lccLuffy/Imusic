package com.lcc.imusic.bean;

import android.support.annotation.NonNull;

/**
 * Created by lcc_luffy on 2016/3/24.
 */
public class DlBean<Data> {
    public String url;
    public String fileName;
    public Data data;
    public boolean isDownloading = false;

    public DlBean(@NonNull String url, @NonNull String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    public DlBean(@NonNull String url, @NonNull String fileName, @NonNull Data data) {
        this.url = url;
        this.fileName = fileName;
        this.data = data;
    }

    public static final DlBean<MusicItem> EMPTY_DL_BEAN = new DlBean<>("invilate url", "invailate file fileName");
}
