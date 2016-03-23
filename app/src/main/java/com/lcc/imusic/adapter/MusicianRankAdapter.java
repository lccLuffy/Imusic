package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.bean.Musician;

import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public class MusicianRankAdapter extends SimpleAdapter<MusicianRankAdapter.Holder, Musician> {

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musician_rank, parent, false));
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
        @Bind(R.id.musicianRank_avatar)
        ImageView avatar;

        @Bind(R.id.musicianRank_title)
        TextView title;

        @Bind(R.id.musicianRank_subtitle)
        TextView subtitle;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Musician album) {
            title.setText(album.name);
            subtitle.setText(String.format(Locale.CHINA, "热度:%d", random.nextInt() + 1000));
            Glide.with(itemView.getContext())
                    .load(album.avatar)
                    .into(avatar);
        }
    }

    static Random random = new Random();
}
