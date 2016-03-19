package com.lcc.imusic.wiget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicItemAdapter;

/**
 * Created by lcc_luffy on 2016/3/19.
 */
public class Bf extends BottomSheetDialogFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.dialog_bottom_music_play,null);

        RecyclerView recyclerView = (RecyclerView) content.findViewById(R.id.dialog_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(getAdapter(inflater.getContext()));
        adapter.getLoadMoreFooter().showNoMoreView();
        return content;
    }
    MusicItemAdapter adapter;
    @NonNull
    public MusicItemAdapter getAdapter(Context context)
    {
        if(adapter == null)
            adapter = new MusicItemAdapter(context);
        return adapter;
    }
}
