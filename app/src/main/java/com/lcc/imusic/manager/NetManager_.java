package com.lcc.imusic.manager;


import com.lcc.imusic.api.MusicApi;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class NetManager_ {
    public static final String DOMAIN = "http://uestc.xyz:8080/api/";
    private Retrofit retrofit;

    private static class ClassHolder {
        private static NetManager_ NET_MANAGER = new NetManager_();
    }

    private NetManager_() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client =
                new OkHttpClient
                        .Builder()
                        .addNetworkInterceptor(new AutInterceptor())
                        .build();

        retrofit = new Retrofit
                .Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private MusicApi musicApi;

    public static MusicApi API() {
        return ClassHolder.NET_MANAGER.musicApi != null ? ClassHolder.NET_MANAGER.musicApi
                : (ClassHolder.NET_MANAGER.musicApi = create(MusicApi.class));
    }

    public static <T> T create(final Class<T> service) {
        return ClassHolder.NET_MANAGER.retrofit.create(service);
    }

    private static class AutInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originRequest = chain.request();
            if (UserManager.isLogin()) {
                originRequest = originRequest
                        .newBuilder()
                        .addHeader("token", UserManager.token())
                        .build();
                Logger.i("token");
            }
            return chain.proceed(originRequest);
        }
    }
}
