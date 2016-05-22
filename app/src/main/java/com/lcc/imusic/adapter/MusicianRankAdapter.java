package com.lcc.imusic.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
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
public class MusicianRankAdapter extends LoadMoreAdapter<Musician> {

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

        @Bind(R.id.musicianRank_pos)
        TextView musicianRank_pos;

        @Bind(R.id.musicianRank_upDown)
        TextView upDown;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Musician album, int pos) {
            musicianRank_pos.setText(String.valueOf(pos + 1));
            title.setText(album.name);
            subtitle.setText(String.format(Locale.CHINA, "热度:%d", random.nextInt(1000) + 1000));
            Glide.with(itemView.getContext())
                    .load(album.avatar)
                    .into(avatar);
            if (pos <= 2) {
                musicianRank_pos.setTextColor(itemView.getResources().getColor(R.color.selectedRed));
                musicianRank_pos.setTypeface(Typeface.SERIF);
            } else {
                musicianRank_pos.setTextColor(itemView.getResources().getColor(R.color.musicTextColorSecondary));
                musicianRank_pos.setTypeface(Typeface.DEFAULT);
            }
            if (random.nextInt(100) < 50) {
                upDown.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.list_icn_up, 0, 0, 0);
            } else {
                upDown.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.list_icn_down, 0, 0, 0);
            }
        }
    }

    static Random random = new Random();
}
