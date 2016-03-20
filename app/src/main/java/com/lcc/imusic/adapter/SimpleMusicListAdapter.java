package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.MusicItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/20.
 */
public class SimpleMusicListAdapter extends RecyclerView.Adapter<SimpleMusicListAdapter.MusicItemViewHolder>{

    List<MusicItem> musicItems;
    OnItemClickListener onItemClickListener;
    public SimpleMusicListAdapter()
    {
        musicItems = new ArrayList<>();
    }
    public void setData(List<MusicItem> musicItemList)
    {
        musicItems.clear();
        musicItems.addAll(musicItemList);
        notifyDataSetChanged();
    }
    @Override
    public MusicItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,parent,false));
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public void onBindViewHolder(final MusicItemViewHolder holder, final int position) {
        holder.onBindData(musicItems.get(position));
        if(onItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return musicItems.size();
    }

    protected class MusicItemViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.music_display_name)
        TextView displayName;
        @Bind(R.id.music_musician)
        TextView musician;
        public MusicItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void onBindData(MusicItem data) {
            displayName.setText(data.title);
            musician.setText(data.artist);
        }
    }
}
