package cn.xmrk.rkandroid.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Au61 on 2016/7/12.
 */
public class BaseRecycleViewHolder<T> extends RecyclerView.ViewHolder {
    public int position;
    public T info;

    public BaseRecycleViewHolder(View itemView) {
        super(itemView);
    }
}
