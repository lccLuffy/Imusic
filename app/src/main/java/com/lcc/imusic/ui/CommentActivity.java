package com.lcc.imusic.ui;

import android.support.v7.widget.RecyclerView;

import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.wiget.StateLayout;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/24.
 */
public class CommentActivity extends BaseActivity{
    @Bind(R.id.stateLayout)
    StateLayout stateLayout;


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }
}
