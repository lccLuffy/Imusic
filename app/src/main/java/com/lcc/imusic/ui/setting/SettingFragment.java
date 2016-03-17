package com.lcc.imusic.ui.setting;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.lcc.imusic.R;

/**
 * Created by lcc_luffy on 2016/3/8.
 */
public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings);
    }
}
