package com.lcc.imusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

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
        if (intent != null) {
            String url = intent.getStringExtra("url");
            String fileName = intent.getStringExtra("fileName");
            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(fileName)) {
                DownLoadHelper.get().dispatchFailEvent(new Exception("url and fileName cannot ne null"));
                return;
            }
            if (!url.startsWith("http")) {
                DownLoadHelper.get().dispatchFailEvent(new Exception("url is not right"));
                return;
            }
            download(url, fileName);
        }
    }

    private void download(String url, String fileName) {
        check();

        final File file = DownLoadHelper.makeFile(fileName);
        if (file == null) {
            DownLoadHelper.get().dispatchFailEvent(new Exception("create file failed!"));
            return;
        }
        DownLoadHelper.get().dispatchStartEvent();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Logger.i("start download");
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                InputStream is = null;
                Throwable fail = null;
                String failMsg = "";
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
                                    DownLoadHelper.get().dispatchProgressEvent(percent);
                                }
                            });
                        }
                        fos.flush();
                        Logger.i("download finish");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                DownLoadHelper.get().dispatchSuccessEvent(file);
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
                            DownLoadHelper.get().dispatchFailEvent(new Exception(finalFailMsg));
                        } else {
                            DownLoadHelper.get().dispatchFailEvent(finalFail);
                        }
                    }
                });
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static class DownLoadEventAdapter implements DownLoadEvent {

        @Override
        public void onDownLoadStart() {

        }

        @Override
        public void onSuccess(File file) {

        }

        @Override
        public void onFail(Throwable throwable) {

        }

        @Override
        public void onProgress(int percent) {

        }
    }

    public interface DownLoadEvent {
        void onDownLoadStart();

        void onSuccess(File file);

        void onFail(Throwable throwable);

        void onProgress(int percent);
    }
}
