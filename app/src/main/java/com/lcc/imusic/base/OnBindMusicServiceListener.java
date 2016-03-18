package com.lcc.imusic.base;

import com.lcc.imusic.service.MusicPlayService;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public interface OnBindMusicServiceListener
{
    void onBind(MusicPlayService.MusicServiceBind musicServiceBind);
}
