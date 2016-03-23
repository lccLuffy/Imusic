package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.bean.Album;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public class MusicGridAdapter extends SimpleAdapter<MusicGridAdapter.Holder, Album> {

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_music, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.bindData(data.get(position));
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    protected class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.musicGrid_cover)
        ImageView cover;

        @Bind(R.id.musicGrid_title)
        TextView title;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Album album) {
            title.setText(album.title);
            Glide.with(itemView.getContext())
                    .load(album.cover)
                    .into(cover);
        }
    }
}