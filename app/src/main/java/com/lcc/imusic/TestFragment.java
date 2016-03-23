package com.lcc.imusic;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lcc.imusic.base.fragment.BaseFragment;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class TestFragment extends BaseFragment {

    /*@Bind(R.id.testImageView)
    @Nullable ImageView imageView;*/

    @Override
    public void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.test;
    }
}
