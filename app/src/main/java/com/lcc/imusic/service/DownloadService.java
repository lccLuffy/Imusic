package com.lcc.imusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.lcc.imusic.bean.DlBean;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by lcc_luffy on 2016/3/21.
 */
public class DownloadService extends Service {
    OkHttpClient okHttpClient;
    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("DownloadService @ onCreate");
    }

    private void check() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient
                    .Builder()
                    .build();
        }
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleIntent(Intent intent) {
        startDownload();
    }

    private void startDownload() {
        DlBean dlBean = DownLoadHelper.get().top();
        if (dlBean == null) {
            DownLoadHelper.get().dispatchFailEvent(DlBean.EMPTY_DL_BEAN, new Exception("cannot download empty!"));
            return;
        }
        check();
        new DownloadThread().start();
    }


    private class DownloadThread extends Thread {
        @Override
        public void run() {
            DlBean dlBean = DownLoadHelper.get().pop();
            while (dlBean != null) {
                download(dlBean);
                dlBean = DownLoadHelper.get().pop();
            }
        }
    }

    @WorkerThread
    private void download(@NonNull final DlBean dlBean) {
        if (!dlBean.url.startsWith("http")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    DownLoadHelper.get().dispatchFailEvent(dlBean, new IllegalArgumentException("unexpected url:" + dlBean.url));
                }
            });
            return;
        }
        final File file = DownLoadHelper.makeFile(dlBean.fileName);
        if (file == null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    DownLoadHelper.get().dispatchFailEvent(dlBean, new Exception("create file failed!"));
                }
            });
            return;
        }
        final Request request = new Request.Builder()
                .url(dlBean.url)
                .build();
        FileOutputStream fos = null;
        InputStream is = null;
        Throwable fail = null;
        String failMsg = "";
        handler.post(new Runnable() {
            @Override
            public void run() {
                DownLoadHelper.get().dispatchStartEvent(dlBean);
            }
        });
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                long downloadLen = 0;
                int readLen = 0;
                byte[] buffer = new byte[2048];

                long totalLen = response.body().contentLength();
                is = response.body().byteStream();
                fos = new FileOutputStream(file);
                while ((readLen = is.read(buffer)) != -1) {
                    downloadLen += readLen;
                    fos.write(buffer, 0, readLen);
                    Logger.i("downloading:%d,(%d of %d)", readLen, downloadLen, totalLen);
                    final int percent = (int) (downloadLen * 1.0f / totalLen * 100);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            DownLoadHelper.get().dispatchProgressEvent(dlBean, percent);
                        }
                    });
                }
                fos.flush();
                Logger.i("download finish");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        DownLoadHelper.get().dispatchSuccessEvent(dlBean, file);
                    }
                });
                return;
            } else {
                failMsg = response.message();
            }
        } catch (IOException e) {
            fail = e;
            e.printStackTrace();
        } catch (Throwable throwable) {
            fail = throwable;
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
                fail = e;
            }
        }
        final Throwable finalFail = fail;
        final String finalFailMsg = failMsg;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (finalFail == null) {
                    DownLoadHelper.get().dispatchFailEvent(dlBean, new Exception(finalFailMsg));
                } else {
                    DownLoadHelper.get().dispatchFailEvent(dlBean, finalFail);
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static class DownLoadEventAdapter<T> implements DownLoadEvent<T> {
        @Override
        public void onStart(DlBean<T> dlBean) {

        }

        @Override
        public void onSuccess(DlBean<T> dlBean, File file) {

        }

        @Override
        public void onFail(DlBean<T> dlBean, Throwable throwable) {

        }

        @Override
        public void onProgress(DlBean<T> dlBean, int percent) {

        }
    }

    public interface DownLoadEvent<T> {
        void onStart(DlBean<T> dlBean);

        void onSuccess(DlBean<T> dlBean, File file);

        void onFail(DlBean<T> dlBean, Throwable throwable);

        void onProgress(DlBean<T> dlBean, int percent);
    }
}
