package com.lcc.imusic.model;

import com.lcc.imusic.bean.M163;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public interface OnProvideMusics {
    void onSuccess(M163 data);

    void onFail(Throwable throwable);
}
