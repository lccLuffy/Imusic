package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.bean.MusicianItem;

import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public class MusicianRankAdapter extends LoadMoreAdapter<MusicianItem> {

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_musician_rank, parent, false));
    }

    @Override
    public void onBindHolder(RecyclerView.ViewHolder holder1, int position) {
        final Holder holder = (Holder) holder1;
        holder.bindData(data.get(position), position);
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

        public void bindData(MusicianItem musicianItem, int pos) {
            title.setText(musicianItem.nickname);
            subtitle.setText(String.format(Locale.CHINA, "查看次数:%d", musicianItem.views));
            Glide.with(itemView.getContext())
                    .load(musicianItem.avatar)
                    .into(avatar);
        }
    }

    static Random random = new Random();
}
