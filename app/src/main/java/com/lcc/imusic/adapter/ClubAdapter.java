package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.Club;

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

    class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.club_name)
        TextView clubName;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Club.ClubItem clubItem) {
            clubName.setText(clubItem.title);
        }
    }
}
