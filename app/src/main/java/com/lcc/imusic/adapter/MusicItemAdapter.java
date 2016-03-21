package com.lcc.imusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;
import com.lcc.state_refresh_recyclerview.Recycler.NiceViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/17.
 */
public class MusicItemAdapter extends NiceAdapter<MusicItem>{
    LayoutInflater layoutInflater;
    public MusicItemAdapter(Context context) {
        super(context);
        layoutInflater = LayoutInflater.from(context);
    }

    public MusicItem getData(int position)
    {
        return data.get(position);
    }


    @Override
    protected NiceViewHolder onCreateNiceViewHolder(ViewGroup parent, int viewType) {
        return new Holder(layoutInflater.inflate(R.layout.item_music_list,parent,false));
    }

    class Holder extends NiceViewHolder<MusicItem>
    {
        @Bind(R.id.music_display_name)
        TextView displayName;


        @Bind(R.id.music_musician)
        TextView musician;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onBindData(MusicItem data) {
            displayName.setText(data.title);
            musician.setText(data.artist);
        }
    }
}
