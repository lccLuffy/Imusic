package com.lcc.imusic.wiget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcc.imusic.R;
import com.lcc.imusic.adapter.SimpleMusicListAdapter;

/**
 * Created by lcc_luffy on 2016/3/19.
 */
public class MusicListDialog extends BottomSheetDialog {
    public MusicListDialog(@NonNull Context context) {
        super(context);
    }

    public MusicListDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected MusicListDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private SimpleMusicListAdapter adapter;
    RecyclerView recyclerView;
    View emptyView;

    public MusicListDialog init() {
        View content = getLayoutInflater().inflate(R.layout.dialog_bottom_music_play, null);
        emptyView = content.findViewById(R.id.dialog_empty);
        recyclerView = (RecyclerView) content.findViewById(R.id.dialog_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(getAdapter());
        setContentView(content);
        return this;
    }

    @NonNull
    public SimpleMusicListAdapter getAdapter() {
        if (adapter == null)
            adapter = new SimpleMusicListAdapter();
        return adapter;
    }

    @Override
    public void show() {
        if (emptyView != null) {
            if (getAdapter().getItemCount() == 0 && emptyView.getVisibility() != View.VISIBLE) {
                emptyView.setVisibility(View.VISIBLE);
            } else if (emptyView.getVisibility() != View.GONE) {
                emptyView.setVisibility(View.GONE);
            }
        }
        super.show();
    }
}
