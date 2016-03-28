package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.DlBean;
import com.lcc.imusic.bean.MusicItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/24.
 */
public class DownloadAdapter extends SimpleAdapter<DownloadAdapter.Holder, DlBean<MusicItem>> {

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        return new Holder(inflater.inflate(R.layout.item_download, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bindData(data.get(position));
    }

    protected class Holder extends RecyclerView.ViewHolder {

        @Bind(R.id.download_title)
        TextView title;

        @Bind(R.id.download_subtitle)
        TextView subtitle;

        @Bind(R.id.download_progress)
        ProgressBar progressBar;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(DlBean<MusicItem> dlBean) {
            title.setText(dlBean.data.title);
            if (dlBean.isDownloading) {
                subtitle.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                subtitle.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                subtitle.setText(dlBean.data.artist);
            }
        }
    }
}
