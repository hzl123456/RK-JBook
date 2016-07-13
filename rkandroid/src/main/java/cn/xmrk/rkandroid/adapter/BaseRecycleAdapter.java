package cn.xmrk.rkandroid.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import cn.xmrk.rkandroid.adapter.viewholder.BaseRecycleViewHolder;

/**
 * Created by Au61 on 2016/6/15.
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleViewHolder> {

    protected OnViewHolderClickListener mOnViewHolderClickListener;

    public void setOnViewHolderClickListener(OnViewHolderClickListener mOnViewHolderClickListener) {
        this.mOnViewHolderClickListener = mOnViewHolderClickListener;
    }

    protected List<T> mData;

    public BaseRecycleAdapter(List<T> mData) {
        this.mData = mData;
    }

    public List<T> getmData() {
        return mData;
    }

    public void reflush(List<T> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder viewHolder, int position) {
        viewHolder.info = mData.get(position);
        viewHolder.position = position;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

}
