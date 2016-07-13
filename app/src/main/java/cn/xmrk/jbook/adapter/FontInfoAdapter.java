package cn.xmrk.jbook.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import cn.xmrk.jbook.R;
import cn.xmrk.jbook.application.JBookApplication;
import cn.xmrk.jbook.helper.FontHelper;
import cn.xmrk.jbook.pojo.FontInfo;
import cn.xmrk.jbook.widget.CircleProgressbar;
import cn.xmrk.rkandroid.adapter.BaseRecycleAdapter;
import cn.xmrk.rkandroid.adapter.viewholder.BaseRecycleViewHolder;
import cn.xmrk.rkandroid.utils.StringUtil;

/**
 * Created by Au61 on 2016/7/12.
 */
public class FontInfoAdapter extends BaseRecycleAdapter<FontInfo> {

    public FontInfoAdapter(List<FontInfo> mData) {
        super(mData);
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FontInfoViewHolder(View.inflate(parent.getContext(), R.layout.item_font, null));
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        final FontInfoViewHolder holder = (FontInfoViewHolder) viewHolder;
        FontInfo info = holder.info;
        if (!StringUtil.isEmptyString(info.getThumbnail_url().getX3())) {
            Glide.with(JBookApplication.getInstance())
                    .load(info.getThumbnail_url().getX3())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            holder.ivFont.setImageBitmap(resource);
                        }
                    });
        }
        if (position == mData.size() - 1) {
            holder.diver.setVisibility(View.GONE);
        } else {
            holder.diver.setVisibility(View.VISIBLE);
        }

        /**
         * 判断该字体文件是否存在
         * **/
        if(FontHelper.hasCompleteFileName(info)){
            holder.ibDownload.setImageResource(R.drawable.font_complete);
        }else{
            holder.ibDownload.setImageResource(R.drawable.font_cloud);
            holder.ibDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnViewHolderClickListener != null) {
                        mOnViewHolderClickListener.OnViewHolderClick(holder);
                    }
                }
            });
        }
    }

    public static class FontInfoViewHolder extends BaseRecycleViewHolder<FontInfo> {
        public ImageView ivFont;
        public View diver;
        public ImageButton ibDownload;
        public CircleProgressbar progress;

        public FontInfoViewHolder(View itemView) {
            super(itemView);
            ivFont = (ImageView) itemView.findViewById(R.id.iv_font);
            diver = itemView.findViewById(R.id.diver);
            ibDownload = (ImageButton) itemView.findViewById(R.id.ib_download);
            progress= (CircleProgressbar) itemView.findViewById(R.id.progress);
        }
    }
}
