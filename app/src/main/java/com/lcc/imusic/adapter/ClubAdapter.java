package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.bean.Club;
import com.lcc.imusic.manager.NetManager_;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/5/28.
 */
public class ClubAdapter extends LoadMoreAdapter<Club.TopicItem> {
    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_club, parent, false));
    }

    @Override
    public void onBindHolder(RecyclerView.ViewHolder holder1, final int position) {
        Holder holder = (Holder) holder1;
        holder.bind(data.get(position));
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onItemLongClick(position);
                }
            });
        }
    }

    protected class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.club_name)
        TextView clubName;

        @Bind(R.id.club_text)
        TextView clubText;

        @Bind(R.id.club_viewCount)
        TextView club_viewCount;

        @Bind(R.id.club_replyCount)
        TextView club_replyCount;

        @Bind(R.id.club_time)
        TextView club_time;

        @Bind(R.id.auth_name)
        TextView auth_name;


        @Bind(R.id.club_avatar)
        ImageView club_avatar;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Club.TopicItem topicItem) {
            clubName.setText(topicItem.title);
            clubText.setText(topicItem.text);
            club_replyCount.setText("回复:" + topicItem.replycount);
            club_viewCount.setText("查看次数:" + topicItem.viewscount);
            club_time.setText(topicItem.addtime);
            auth_name.setText(topicItem.authorNmae);

            Glide.with(itemView.getContext())
                    .load(NetManager_.DOMAIN + topicItem.avatar)
                    .into(club_avatar);
        }
    }
}
