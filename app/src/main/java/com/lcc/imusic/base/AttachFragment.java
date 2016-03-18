package com.lcc.imusic.base;

import android.content.Context;

import com.lcc.imusic.service.MusicPlayService;
import com.orhanobut.logger.Logger;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public abstract class AttachFragment extends BaseFragment implements OnBindMusicServiceListener{
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MusicBindActivity)
        {
            MusicBindActivity musicBindActivity = (MusicBindActivity) context;
            musicBindActivity.addOnBindMusicServiceListener(this);
        }
    }
    @Override
    public void onBind(MusicPlayService.MusicServiceBind musicServiceBind) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
