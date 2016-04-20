package com.lcc.imusic.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class Json {
    private Gson gson;

    private Json() {
        gson = new Gson();
    }

    private static final class ClassHolder {
        private static final Json JSON = new Json();
    }

    public static Gson getGson() {
        return ClassHolder.JSON.gson;
    }

    public static String toJson(Object src) {
        return getGson().toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return getGson().fromJson(json, classOfT);
    }
}
