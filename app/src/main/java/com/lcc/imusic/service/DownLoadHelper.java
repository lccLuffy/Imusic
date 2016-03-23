package com.lcc.imusic.service;

import android.os.Environment;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public final class DownLoadHelper {
    private List<DownloadService.DownLoadEvent> downLoadEvents;

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

    @MainThread
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

    public void dispatchFailEvent(Throwable throwable) {
        if (downLoadEvents != null) {
            for (DownloadService.DownLoadEvent event : downLoadEvents) {
                event.onFail(throwable);
            }
        }
    }

    public void dispatchSuccessEvent(File file) {
        if (downLoadEvents != null) {
            for (DownloadService.DownLoadEvent event : downLoadEvents) {
                event.onSuccess(file);
            }
        }
    }

    public void dispatchStartEvent() {
        if (downLoadEvents != null) {
            for (DownloadService.DownLoadEvent event : downLoadEvents) {
                event.onDownLoadStart();
            }
        }
    }

    public void dispatchProgressEvent(int percent) {
        if (downLoadEvents != null) {
            for (DownloadService.DownLoadEvent event : downLoadEvents) {
                event.onProgress(percent);
            }
        }
    }

    public void addDownloadEvent(DownloadService.DownLoadEvent event) {
        if (downLoadEvents == null)
            downLoadEvents = new ArrayList<>();
        downLoadEvents.add(event);
    }

    public void removeDownloadEvent(DownloadService.DownLoadEvent event) {
        if (downLoadEvents != null)
            downLoadEvents.remove(event);
    }
}
