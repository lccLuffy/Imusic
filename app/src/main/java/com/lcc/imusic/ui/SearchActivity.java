package com.lcc.imusic.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;

import com.lcc.imusic.R;
import com.lcc.imusic.base.activity.BaseActivity;
import com.lcc.imusic.wiget.StateLayout;

import butterknife.Bind;

/**
 * Created by lcc_luffy on 2016/3/26.
 */
public class SearchActivity extends BaseActivity {

    @Bind(R.id.stateLayout)
    StateLayout stateLayout;


    @Bind(R.id.searchView)
    SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                toast("searching:" + query);
                stateLayout.showProgressView();
                stateLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stateLayout.showEmptyView("nothing had been searched");
                    }
                }, 1000);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }
}
