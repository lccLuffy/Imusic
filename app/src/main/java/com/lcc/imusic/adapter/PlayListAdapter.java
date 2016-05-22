package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.MusicList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/5/20.
 */
public class PlayListAdapter extends SimpleAdapter<PlayListAdapter.Holder, MusicList> {

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        return new Holder(inflater.inflate(R.layout.item_play_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        holder.onBind(data.get(position));
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(MusicList musicList) {
            name.setText(musicList.musicListId + "");
        }
    }
}
