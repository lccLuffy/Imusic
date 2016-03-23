package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public abstract class SimpleAdapter<Holder extends RecyclerView.ViewHolder, DataType> extends RecyclerView.Adapter<Holder> {

    protected List<DataType> data;

    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SimpleAdapter() {
        data = new ArrayList<>();
    }

    public void setData(List<DataType> musicItemList) {
        data.clear();
        data.addAll(musicItemList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
