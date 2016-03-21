package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.MusicItem;
import com.lcc.imusic.model.CurrentMusicProviderImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/20.
 */
public class SimpleMusicListAdapter extends RecyclerView.Adapter<SimpleMusicListAdapter.MusicItemViewHolder> {

    public static List<SimpleMusicListAdapter> simpleMusicListAdapters;

    List<MusicItem> musicItems;
    OnItemClickListener onItemClickListener;

    public void setCurrentPlayingIndex(int currentPlayingIndex) {
        this.currentPlayingIndex = currentPlayingIndex;
    }

    public int getCurrentPlayingIndex() {
        return currentPlayingIndex;
    }

    private final static int NO_POSITION = -1;

    private int currentPlayingIndex = NO_POSITION;

    public SimpleMusicListAdapter() {
        musicItems = new ArrayList<>();
        if (simpleMusicListAdapters == null)
            simpleMusicListAdapters = new ArrayList<>();
        simpleMusicListAdapters.add(this);
    }

    public void setData(List<MusicItem> musicItemList) {
        musicItems.clear();
        musicItems.addAll(musicItemList);
        notifyDataSetChanged();
    }


    public void setData(List<MusicItem> musicItemList, int currentPlayingIndex) {
        this.currentPlayingIndex = currentPlayingIndex;
        musicItems.clear();
        musicItems.addAll(musicItemList);
        notifyDataSetChanged();
    }

    @Override
    public MusicItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_list, parent, false));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final MusicItemViewHolder holder, final int position) {
        holder.onBindData(musicItems.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayingIndex == NO_POSITION) {
                    currentPlayingIndex = holder.getAdapterPosition();
                    CurrentMusicProviderImpl.getMusicProvider().overrideToMe(musicItems);
                    playingIndexChangeTo(currentPlayingIndex);
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(holder.getAdapterPosition());
                    for (SimpleMusicListAdapter adapter : simpleMusicListAdapters) {
                        if (adapter != SimpleMusicListAdapter.this) {
                            adapter.notPlayAnyMore();
                        }
                    }
                } else if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    public void onDestroy() {
        simpleMusicListAdapters.remove(this);
    }

    @Override

    public int getItemCount() {
        return musicItems.size();
    }

    protected class MusicItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.music_display_name)
        TextView displayName;

        @Bind(R.id.music_musician)
        TextView musician;

        @Bind(R.id.music_playing)
        ImageView music_playing;

        public MusicItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBindData(MusicItem data) {
            displayName.setText(data.title);
            musician.setText(data.artist);
            if (isPlaying() && currentPlayingIndex == getAdapterPosition()) {
                playing();
            } else {
                notPlaying();
            }

        }

        private void notPlaying() {
            if (music_playing.getVisibility() != View.GONE)
                music_playing.setVisibility(View.GONE);

            displayName.setTextColor(itemView.getContext().getResources().getColor(R.color.musicTextColorPrimary));
            musician.setTextColor(itemView.getContext().getResources().getColor(R.color.musicTextColorSecondary));
        }

        private void playing() {
            if (music_playing.getVisibility() != View.VISIBLE)
                music_playing.setVisibility(View.VISIBLE);
            displayName.setTextColor(itemView.getContext().getResources().getColor(R.color.selectedRed));
            musician.setTextColor(itemView.getContext().getResources().getColor(R.color.selectedRed));
        }
    }

    public boolean isPlaying() {
        return currentPlayingIndex != NO_POSITION;
    }

    public void playingIndexChangeTo(int index) {
        if (isPlaying()) {
            int i = currentPlayingIndex;
            currentPlayingIndex = index;
            notifyItemChanged(index);
            notifyItemChanged(i);
        }
    }


    public void notPlayAnyMore() {
        if (isPlaying()) {
            currentPlayingIndex = NO_POSITION;
            notifyDataSetChanged();
        }
    }
}
