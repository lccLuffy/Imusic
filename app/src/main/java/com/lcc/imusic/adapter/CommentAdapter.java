package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcc.imusic.R;
import com.lcc.imusic.bean.CommentItem;
import com.lcc.imusic.manager.NetManager_;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/24.
 */
public class CommentAdapter extends LoadMoreAdapter<CommentItem> {
    @Override
    public Holder onCreateHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindHolder(RecyclerView.ViewHolder holder1, int position) {
        Holder holder = (Holder) holder1;
        holder.bind(data.get(position));
    }

    protected class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.comment_avatar)
        ImageView avatar;

        @Bind(R.id.comment_username)
        TextView username;

        @Bind(R.id.comment_time)
        TextView time;

        @Bind(R.id.comment_content)
        TextView content;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(CommentItem commentBean) {
            Glide.with(itemView.getContext())
                    .load(NetManager_.DOMAIN + commentBean.avatar)
                    .into(avatar);
            username.setText(commentBean.authorName);
            time.setText(commentBean.addtime);
            content.setText(commentBean.content);
        }
    }
}
