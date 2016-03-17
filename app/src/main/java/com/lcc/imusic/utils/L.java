package com.lcc.imusic.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class L {
    public static void i(String msg,Object... args)
    {
        Logger.i(msg,args);
    }
    public static void json(String json)
    {
        Logger.json(json);
    }
}
