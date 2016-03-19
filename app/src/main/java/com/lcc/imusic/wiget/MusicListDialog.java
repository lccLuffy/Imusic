package com.lcc.imusic.wiget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.MusicItemAdapter;

/**
 * Created by lcc_luffy on 2016/3/19.
 */
public class MusicListDialog extends BottomSheetDialog{
    public MusicListDialog(@NonNull Context context) {
        super(context);
    }

    public MusicListDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected MusicListDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private MusicItemAdapter adapter;

    public MusicListDialog init()
    {
        View content = getLayoutInflater().inflate(R.layout.dialog_bottom_music_play,null);

        RecyclerView recyclerView = (RecyclerView) content.findViewById(R.id.dialog_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(getAdapter());
        adapter.getLoadMoreFooter().showNoMoreView();
        setContentView(content);
        return this;
    }

    @NonNull
    public MusicItemAdapter getAdapter()
    {
        if(adapter == null)
            adapter = new MusicItemAdapter(getContext());
        return adapter;
    }
}