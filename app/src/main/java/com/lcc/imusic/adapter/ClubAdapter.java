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
public class ClubAdapter extends LoadMoreAdapter<Club.ClubItem> {
    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_club, parent, false));
    }

    @Override
    public void onBindHolder(RecyclerView.ViewHolder holder1, int position) {
        Holder holder = (Holder) holder1;
        holder.bind(data.get(position));
    }

    class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.club_name)
        TextView clubName;

        @Bind(R.id.club_text)
        TextView clubText;

        @Bind(R.id.auth_name)
        TextView auth_name;


        @Bind(R.id.club_avatar)
        ImageView club_avatar;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Club.ClubItem clubItem) {
            clubName.setText(clubItem.title);
            clubText.setText(clubItem.text);
            auth_name.setText(clubItem.authorNmae);
            Glide.with(itemView.getContext())
                    .load(NetManager_.DOMAIN + clubItem.avatar)
                    .into(club_avatar);
        }
    }
}
