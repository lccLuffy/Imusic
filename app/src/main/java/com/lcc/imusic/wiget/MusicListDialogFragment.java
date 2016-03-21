package com.lcc.imusic.wiget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * Created by lcc_luffy on 2016/3/19.
 */
public class MusicListDialogFragment extends AppCompatDialogFragment {
    private MusicListDialog musicListDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MusicListDialog(getActivity(), getTheme());
    }
}
