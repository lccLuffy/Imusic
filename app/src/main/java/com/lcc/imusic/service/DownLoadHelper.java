package com.lcc.imusic.service;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.lcc.imusic.bean.DlBean;
import com.lcc.imusic.bean.MusicItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public final class DownLoadHelper {
    private List<DownloadService.DownLoadEvent<MusicItem>> downLoadEvents;

    private DownLoadHelper() {
    }

    private static final class ClassHolder {
        private static DownLoadHelper downLoadHelper = new DownLoadHelper();
    }

    public static DownLoadHelper get() {
        return ClassHolder.downLoadHelper;
    }

    private static File checkRootDir() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DownLoadConfig.ROOT_DIR;
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            return file;
        } else {
            boolean r = file.mkdir();
            if (r)
                return file;
        }
        return null;
    }

    @Nullable
    public static File makeFile(String fileName) {
        final File dir = checkRootDir();
        if (dir == null)
            return null;
        final File file = new File(dir, fileName);
        if (!file.exists())
            try {
                if (file.createNewFile())
                    return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        return null;
    }

    public void dispatchFailEvent(DlBean<MusicItem> dlBean, Throwable throwable) {
        if (downLoadEvents != null) {
            dlBean.isDownloading = false;
            for (DownloadService.DownLoadEvent<MusicItem> event : downLoadEvents) {
                event.onFail(dlBean, throwable);
            }
        }
    }

    public void dispatchSuccessEvent(DlBean<MusicItem> dlBean, File file) {
        if (downLoadEvents != null) {
            dlBean.isDownloading = false;
            for (DownloadService.DownLoadEvent<MusicItem> event : downLoadEvents) {
                event.onSuccess(dlBean, file);
            }
        }
    }

    public void dispatchStartEvent(DlBean<MusicItem> dlBean) {
        if (downLoadEvents != null) {
            dlBean.isDownloading = true;
            for (DownloadService.DownLoadEvent<MusicItem> event : downLoadEvents) {
                event.onStart(dlBean);
            }
        }
    }

    public void dispatchProgressEvent(DlBean<MusicItem> dlBean, int percent) {
        if (downLoadEvents != null) {
            dlBean.isDownloading = true;
            for (DownloadService.DownLoadEvent<MusicItem> event : downLoadEvents) {
                event.onProgress(dlBean, percent);
            }
        }
    }

    public void addDownloadEvent(DownloadService.DownLoadEvent<MusicItem> event) {
        if (downLoadEvents == null)
            downLoadEvents = new ArrayList<>();
        downLoadEvents.add(event);
    }

    public void removeDownloadEvent(DownloadService.DownLoadEvent event) {
        if (downLoadEvents != null)
            downLoadEvents.remove(event);
    }

    private List<DlBean<MusicItem>> dlBeanList;

    @SafeVarargs
    public final void enqueue(DlBean<MusicItem>... dlBeans) {
        if (dlBeanList == null)
            dlBeanList = new ArrayList<>();
        Collections.addAll(dlBeanList, dlBeans);
    }



    @Nullable
    public DlBean<MusicItem> pop() {
        synchronized (DownLoadHelper.class) {
            if (dlBeanList == null || dlBeanList.isEmpty())
                return downloadingDlBean = null;
            return downloadingDlBean = dlBeanList.remove(0);
        }
    }

    public List<DlBean<MusicItem>> getDownloadQueue() {
        return dlBeanList;
    }

    private DlBean<MusicItem> downloadingDlBean;

    public DlBean<MusicItem> getDownloadingDlBean() {
        return downloadingDlBean;
    }

    public void downloadAll(Context context, List<MusicItem> list) {
        if (list != null && !list.isEmpty()) {
            for (MusicItem item : list) {
                DlBean<MusicItem> dlBean = new DlBean<>(item.data, item.title + item.artist + ".mp3");
                dlBean.data = item;
                get().enqueue(dlBean);
            }
            context.startService(new Intent(context, DownloadService.class));
        }
    }

    @Nullable
    public DlBean<MusicItem> top() {
        if (dlBeanList == null || dlBeanList.isEmpty())
            return null;
        return dlBeanList.get(0);
    }
}
