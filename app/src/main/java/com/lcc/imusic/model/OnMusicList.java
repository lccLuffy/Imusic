package com.lcc.imusic.model;

import com.lcc.imusic.bean.MusicItem;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public interface OnMusicList {
    void onSuccess(List<MusicItem> musicItems);

    void onFail(String reason);
}
