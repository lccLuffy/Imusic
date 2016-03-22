package com.lcc.imusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


/**
 * Created by lcc_luffy on 2016/3/21.
 */
public class DownloadService extends Service {
    OkHttpClient okHttpClient = new OkHttpClient
            .Builder()
            /*.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    Logger.i("intercept");
                    return response.newBuilder()
                            .body(new ProgressResponse(response.body()))
                            .build();
                }
            })*/
            .build();

    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                int l = mp.getDuration() / 1000;
                Logger.i("onPrepared,%d:%d", l / 60, l % 60);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Logger.i("error:%d,%d", what, extra);
                return true;
            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Logger.i("onCompletion");
            }
        });

        final File file = new File(Environment.getExternalStorageDirectory(), "lcctest.mp3");
        file.deleteOnExit();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.e(e, "");
            }
        }

        final Handler handler = new Handler(Looper.getMainLooper());


        final Request request = new Request.Builder()
                .url("http://m2.music.126.net/ki94ZEXzV3GPc7gSA60aOA==/996157534774260.mp3")
                .build();
        Logger.i("start download");
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                InputStream is = null;
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Logger.i("isSuccessful");
                        long ed = 0;
                        int len = 0;
                        byte[] buf = new byte[2048];

                        long l = response.body().contentLength();
                        is = response.body().byteStream();
                        fos = new FileOutputStream(file);


                        boolean can = true;
                        while ((len = is.read(buf)) != -1) {
                            ed += len;
                            fos.write(buf, 0, len);
                            if (can && ed * 1.0f / l > 0.1f) {
                                can = false;
                                Logger.i("post");

                                final FileOutputStream finalFos = fos;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            mediaPlayer.setDataSource(file.getAbsolutePath());
                                            mediaPlayer.prepareAsync();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Logger.i("downloading:%d,(%d of %d)", len, ed, l);
                            }

                        }
                        fos.flush();
                        Logger.i("download finish");

                    } else {
                        Logger.i("fail,%s", response.message());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.e(e, "exception");
                } catch (Throwable throwable) {
                    Logger.e(throwable, "exception");
                } finally {

                    try {
                        if (fos != null)
                            fos.close();
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private static class ProgressResponse extends ResponseBody {

        private final ResponseBody responseBody;
        private BufferedSource bufferedSource;

        public ProgressResponse(ResponseBody responseBody) {
            this.responseBody = responseBody;
        }

        @Override
        public MediaType contentType() {
            Logger.i(responseBody.contentType().toString());
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    Logger.i("%d%% done\n", (100 * totalBytesRead) / responseBody.contentLength());
                    return bytesRead;
                }
            };
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
