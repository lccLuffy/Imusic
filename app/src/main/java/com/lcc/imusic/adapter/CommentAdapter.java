package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcc.imusic.R;
import com.lcc.imusic.bean.CommentBean;

/**
 * Created by lcc_luffy on 2016/3/24.
 */
public class CommentAdapter extends SimpleAdapter<CommentAdapter.Holder,CommentBean> {

    LayoutInflater inflater;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.getContext());

        return new Holder(inflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(data.get(position));
    }

    protected class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
        public void bind(CommentBean commentBean)
        {

        }
    }
}
