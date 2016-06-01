package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lcc.imusic.R;

/**
 * Created by lcc_luffy on 2016/5/22.
 */
public abstract class LoadMoreAdapter<DataType> extends SimpleAdapter<RecyclerView.ViewHolder, DataType> {
    public static final int ITEM_TYPE_FOOTER = 0;
    public static final int ITEM_TYPE_NORMAL = 1;

    private int state = FooterViewHolder.STATE_LOAD_MORE;

    private LoadMoreListener loadMoreListener;
    private FooterViewHolder footerViewHolder;

    private String loadMoreMsg = "Loading";
    private String noMoreMsg = " - - - - - - - - - - - - - - - - - - - - ";

    public LoadMoreAdapter() {
        registerAdapterDataObserver(new DataObserver());
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (footerViewHolder != null) {
                if (isDataEmpty())
                    footerViewHolder.hide();
                else
                    footerViewHolder.show();
            }

        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == data.size()) {
            footerViewHolder.onBind();
        } else {
            onBindHolder(holder, position);
        }
    }

    public void onBindHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public abstract RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType);

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        if (ITEM_TYPE_FOOTER == viewType) {
            if (footerViewHolder == null) {
                footerViewHolder = new FooterViewHolder(inflater.inflate(R.layout.item_footer_load_more, parent, false));
                footerViewHolder.hide();
            }
            return footerViewHolder;
        }
        return onCreateHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_NORMAL;
    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public static final int STATE_LOAD_MORE = 0;
        public static final int STATE_NO_MORE = 1;
        public static final int STATE_GONE = 2;

        TextView footerText;

        ProgressBar footerProgressBar;


        public FooterViewHolder(View itemView) {
            super(itemView);
            footerText = (TextView) itemView.findViewById(R.id.footerText);
            footerProgressBar = (ProgressBar) itemView.findViewById(R.id.footerProgressBar);
        }

        public void onBind() {
            switch (state) {
                case STATE_LOAD_MORE:
                    loadMore();
                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore();
                    }
                    break;
                case STATE_NO_MORE:
                    noMore();
                    break;
                case STATE_GONE:
                    hide();
                    break;
            }
        }

        private void loadMore() {
            show();
            if (footerProgressBar.getVisibility() != View.VISIBLE) {
                footerProgressBar.setVisibility(View.VISIBLE);
            }
            footerText.setText(loadMoreMsg);
        }

        private void noMore() {
            show();
            if (footerProgressBar.getVisibility() != View.GONE) {
                footerProgressBar.setVisibility(View.GONE);
            }
            footerText.setText(noMoreMsg);
        }

        private void hide() {
            if (itemView.getVisibility() != View.GONE) {
                itemView.setVisibility(View.GONE);
            }
        }

        private void show() {
            if (itemView.getVisibility() != View.VISIBLE) {
                itemView.setVisibility(View.VISIBLE);
            }
        }
    }


    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * tell tha adapter there will not be data ,so the progress will hide,and the load more callback will not be invoked.
     */
    public void noMoreData() {
        state = FooterViewHolder.STATE_NO_MORE;
        if (footerViewHolder != null)
            footerViewHolder.noMore();
    }

    /**
     * @param noMoreMsg show when no more data
     *                  tell tha adapter there will not be data ,so the progress will hide,and the load more callback will not be invoked.
     */
    public void noMoreData(String noMoreMsg) {
        state = FooterViewHolder.STATE_NO_MORE;
        this.noMoreMsg = noMoreMsg;
        if (footerViewHolder != null)
            footerViewHolder.noMore();
    }


    /**
     * tell tha adapter there will be more data ,so the progress will show,and the load more callback will be invoked.
     */
    public void canLoadMore() {
        state = FooterViewHolder.STATE_LOAD_MORE;
        if (footerViewHolder != null)
            footerViewHolder.loadMore();
    }

    /**
     * tell tha adapter there will be more data ,so the progress will show,and the load more callback will be invoked.
     */
    public void canLoadMore(String loadMoreMsg) {
        state = FooterViewHolder.STATE_LOAD_MORE;
        this.loadMoreMsg = loadMoreMsg;
        if (footerViewHolder != null)
            footerViewHolder.loadMore();
    }


    /**
     * set visibility gone
     */
    public void hideFooter() {
        state = FooterViewHolder.STATE_GONE;
        if (footerViewHolder != null)
            footerViewHolder.hide();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

}
