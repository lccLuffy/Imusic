package com.lcc.imusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lcc.imusic.wiget.StateLayout;

/**
 * Created by lcc_luffy on 2016/3/27.
 */
public abstract class FreshAdapter<VH extends RecyclerView.ViewHolder, DataType> extends SimpleAdapter<RecyclerView.ViewHolder, DataType> {
    private final static int ITEM_TYPE_FOOTER = 1;
    private final static int ITEM_TYPE_NORMAL = 0;

    private FooterHolder footerHolder;

    public FreshAdapter(Context context) {
        footerHolder = new FooterHolder(new StateLayout(context));
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == data.size() ? ITEM_TYPE_FOOTER : ITEM_TYPE_NORMAL;
    }

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOTER) {
            return footerHolder;
        }
        return onCreateHolder(parent, viewType);
    }

    public abstract RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType);

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterHolder) {
            ((FooterHolder) holder).onBind();
            return;
        }
        onBind((VH) holder, position);
    }

    public abstract void onBind(VH holder, int position);

    public void showLoadMoreView() {
        footerHolder.showLoadMoreView();
    }

    public void showErrorView() {
        footerHolder.showErrorView();
    }

    public void hide() {
        footerHolder.hide();
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        footerHolder.setLoadMoreListener(loadMoreListener);
    }

    public static class FooterHolder extends RecyclerView.ViewHolder {

        private static final int STATE_INVISIBLE = 1;
        private static final int STATE_PROGRESS = 2;
        private static final int STATE_ERROR = 3;
        private StateLayout stateLayout;
        private int state = STATE_INVISIBLE;
        private LoadMoreListener loadMoreListener;

        public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
            this.loadMoreListener = loadMoreListener;
        }

        public FooterHolder(View itemView) {
            super(itemView);
            stateLayout = (StateLayout) itemView;
        }

        public void onBind() {
            switch (state) {
                case STATE_INVISIBLE:
                    if (itemView.getVisibility() != View.GONE)
                        itemView.setVisibility(View.GONE);
                    break;
                case STATE_PROGRESS:
                    if (itemView.getVisibility() != View.VISIBLE)
                        itemView.setVisibility(View.VISIBLE);
                    stateLayout.showProgressView();
                    if (loadMoreListener != null)
                        loadMoreListener.onLoadMore();
                    break;
                case STATE_ERROR:
                    if (itemView.getVisibility() != View.VISIBLE)
                        itemView.setVisibility(View.VISIBLE);
                    stateLayout.showErrorView();
                    break;
            }
        }

        public void showLoadMoreView() {
            state = STATE_PROGRESS;
        }

        public void showErrorView() {
            state = STATE_ERROR;
        }

        public void hide() {
            state = STATE_INVISIBLE;
        }

        public StateLayout getStateLayout() {
            return stateLayout;
        }
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
