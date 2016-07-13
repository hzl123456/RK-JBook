package cn.xmrk.rkandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.DownloadListener;

import cn.xmrk.rkandroid.R;
import cn.xmrk.rkandroid.widget.BaseWebViewClient;
import cn.xmrk.rkandroid.widget.ProgressWebView;


public class WebViewActivity extends BackableBaseActivity {

    public static final String REQUEST_EXTRA_TITLE = "extraTitle";
    public static final String REQUEST_EXTRA_URL = "extraUrl";
    public static final String REQUEST_EXTRA_SHARE = "extraShare";

    protected ProgressWebView wv;

    protected String url;

    protected String title;

    public static void start(Activity activity, String title, String url, boolean share) {
        Intent _intent = new Intent(activity, WebViewActivity.class);
        _intent.putExtra(REQUEST_EXTRA_TITLE, title);
        _intent.putExtra(REQUEST_EXTRA_SHARE, share);
        _intent.putExtra(REQUEST_EXTRA_URL, url);
        activity.startActivity(_intent);
    }

    @Override
    public void onBackPressed() {
        if (wv != null && wv.canGoBack()) {
            wv.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        wv = (ProgressWebView) findViewById(R.id.wv);

        Intent intent = getIntent();
        url = intent.getStringExtra(REQUEST_EXTRA_URL);
        title = intent.getStringExtra(REQUEST_EXTRA_TITLE);

        //设置标题
        getSupportActionBar().setTitle(title);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setBuiltInZoomControls(true);

        wv.requestFocus();
        wv.loadUrl(url);

        wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        wv.setWebViewClient(new BaseWebViewClient());
    }

    @Override
    protected void onDestroy() {
        ((ViewGroup) wv.getParent()).removeView(wv);
        wv.destroy();
        super.onDestroy();
    }

}
