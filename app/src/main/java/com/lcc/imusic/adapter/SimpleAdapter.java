package com.lcc.imusic.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        if (otherData == null || otherData.isEmpty()) {
            data.clear();
        } else {
            data.clear();
            data.addAll(otherData);
        }
        notifyDataSetChanged();
    }

    @SafeVarargs
    public final void setData(DataType... otherData) {
        data.clear();
        if (!(otherData == null || otherData.length == 0)) {
            Collections.addAll(data, otherData);
        }
        notifyDataSetChanged();
    }


    public List<DataType> getData() {
        return data;
    }

    public void addData(Collection<DataType> otherData) {
        if (otherData == null || otherData.isEmpty())
            return;
        if (data.isEmpty()) {
            setData(otherData);
            return;
        }
        int s = data.size();
        data.addAll(otherData);
        notifyItemRangeInserted(s, otherData.size());
    }

    @SafeVarargs
    public final void addData(DataType... otherData) {
        if (otherData == null || otherData.length == 0) {
            return;
        }
        if (data.isEmpty()) {
            setData(otherData);
            return;
        }
        int s = data.size();
        Collections.addAll(data, otherData);
        notifyItemRangeInserted(s, otherData.length);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
