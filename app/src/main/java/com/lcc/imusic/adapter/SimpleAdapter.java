package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
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

    public DataType getData(int position) {
        return data.get(position);
    }

    public SimpleAdapter() {
        data = new ArrayList<>();
    }

    public void setData(Collection<DataType> otherData) {
        data.clear();
        data.addAll(otherData);
        notifyDataSetChanged();
    }

    public void addData(Collection<DataType> otherData) {
        if (otherData == null || otherData.isEmpty())
            return;
        if (otherData.isEmpty()) {
            setData(otherData);
            return;
        }
        int s = data.size();
        data.addAll(otherData);
        notifyItemRangeInserted(s, otherData.size());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
