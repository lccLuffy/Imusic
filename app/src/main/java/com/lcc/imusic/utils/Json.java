package com.lcc.imusic.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class Json {
    private static Gson gson;

    public static Gson getGson()
    {
        if(gson == null)
        {
            synchronized (Json.class)
            {
                if(gson == null)
                {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
    public static String toJson(Object src) {
        return getGson().toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return getGson().fromJson(json,classOfT);
    }
}
