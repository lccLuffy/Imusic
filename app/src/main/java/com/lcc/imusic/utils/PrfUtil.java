package com.lcc.imusic.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lcc.imusic.config.App;


/**
 * Created by lcc_luffy on 2016/1/26.
 */
public class PrfUtil {
    private SharedPreferences sharedPreferences;
    private static PrfUtil instance;
    private PrfUtil()
    {
        sharedPreferences = App.getApp().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
    }
    public static SharedPreferences get()
    {
        if(instance == null)
        {
            synchronized (PrfUtil.class)
            {
                if(instance == null)
                    instance = new PrfUtil();
            }
        }
        return instance.sharedPreferences;
    }
    public static SharedPreferences.Editor start()
    {
        return get().edit();
    }

}
