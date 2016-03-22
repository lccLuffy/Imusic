package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcc.imusic.R;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public class MusicGridAdapter extends SimpleAdapter<MusicGridAdapter.Holder, Object> {

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_music, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bindData(data.get(position));
    }

    protected class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }

        public void bindData(Object o) {

        }
    }
}
