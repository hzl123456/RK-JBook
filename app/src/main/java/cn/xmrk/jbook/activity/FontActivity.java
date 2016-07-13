package cn.xmrk.jbook.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import cn.xmrk.jbook.R;
import cn.xmrk.jbook.adapter.FontInfoAdapter;
import cn.xmrk.jbook.helper.FontHelper;
import cn.xmrk.jbook.pojo.FontInfo;
import cn.xmrk.jbook.net.FontInfoCallBack;
import cn.xmrk.jbook.net.RequestHelper;
import cn.xmrk.rkandroid.activity.BackableBaseActivity;
import cn.xmrk.rkandroid.adapter.OnViewHolderClickListener;
import okhttp3.Call;

/**
 * Created by Au61 on 2016/7/12.
 */
public class FontActivity extends BackableBaseActivity {

    private RecyclerView rvContent;
    private LinearLayoutManager layoutManager;
    private FontInfoAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);
        initTitle();
        initRvContent();
        getFontInfos();
    }

    private void initTitle() {
        getTitlebar().setBackgroundResource(R.color.color_text_black);
        getSupportActionBar().setTitle("字体管理");
    }

    private void initRvContent() {
        rvContent = (RecyclerView) findViewById(R.id.rv_content);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new FontInfoAdapter(null);

        rvContent.setAdapter(mAdapter);
        rvContent.setLayoutManager(layoutManager);

        mAdapter.setOnViewHolderClickListener(new OnViewHolderClickListener() {
            @Override
            public void OnViewHolderClick(RecyclerView.ViewHolder viewHolder) {
                final FontInfoAdapter.FontInfoViewHolder holder = (FontInfoAdapter.FontInfoViewHolder) viewHolder;
                final FontInfo info = holder.info;
                //隐藏下载按钮，显示进度条
                holder.ibDownload.setVisibility(View.GONE);
                holder.progress.setVisibility(View.VISIBLE);
                holder.progress.setPercent(0);
                RequestHelper.downloadFontFile(info.getResource_url(), new FileCallBack(FontHelper.getFatherPath(), FontHelper.getFileName(info)) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        holder.progress.setPercent((int) (progress * 100));
                        if (holder.progress.getPercent() == 100) {
                            //下载完成了，然后改变状态，并且改变文件名称，用id来表示
                            holder.ibDownload.setVisibility(View.VISIBLE);
                            holder.progress.setVisibility(View.GONE);
                            holder.ibDownload.setImageResource(R.drawable.font_complete);
                            //不允许再次下载了
                            holder.ibDownload.setEnabled(false);
                            // 改变文件的名称
                            FontHelper.changeFontFileName(info);
                            //添加一个字体
                            FontHelper.addTypeface(info);
                        }
                    }
                },FontActivity.this);
            }

            @Override
            public void OnViewHolderLongClick(RecyclerView.ViewHolder holder) {

            }

            @Override
            public void OnViewHolderRemove(RecyclerView.ViewHolder holder) {

            }
        });
    }

    public void getFontInfos() {
        getPDM().showProgress();
        RequestHelper.getFonInfos(new FontInfoCallBack() {
            @Override
            public void onResponse(List<FontInfo> response, int id) {
                getPDM().dismiss();
                mAdapter.reflush(response);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消所有的请求
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
