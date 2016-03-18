package com.lcc.imusic.base;

import android.content.Context;

import com.lcc.imusic.MainActivity;
import com.lcc.imusic.bean.MusicItem;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public abstract class AttachFragment extends BaseFragment{
    protected MainActivity mainActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity)
        {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface OnBindActivity
    {
        void onMusicList(List<MusicItem> list);
    }
}
