package com.lcc.imusic.model;

import com.lcc.imusic.bean.SongsBean;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public interface OnProvideMusics {
    void onSuccess(SongsBean data);

    void onFail(Throwable throwable);
}
