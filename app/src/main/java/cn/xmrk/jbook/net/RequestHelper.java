package cn.xmrk.jbook.net;

import android.app.Activity;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

/**
 * Created by Au61 on 2016/7/12.
 */
public class RequestHelper {

    /**
     * 字体地址信息
     **/
    private static final String JSON_URL = "http://7xj62a.com1.z0.glb.clouddn.com/v1/fontlist-jt-android.json";


    /**
     * 获取字体信息列表
     **/
    public static void getFonInfos(FontInfoCallBack callBack) {
        OkHttpUtils
                .get()
                .url(JSON_URL)
                .build()
                .execute(callBack);
    }

    /**
     * 下载字体文件
     **/
    public static void downloadFontFile(String url, FileCallBack callBack, Activity activity) {
        OkHttpUtils
                .get()
                .url(url)
                .tag(activity)
                .build()
                .execute(callBack);
    }
}
