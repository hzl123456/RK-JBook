package cn.xmrk.jbook.net;

import android.app.Activity;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.apache.log4j.Logger;

import cn.xmrk.rkandroid.config.RKConfigHelper;

/**
 * Created by Au61 on 2016/7/12.
 */
public class RequestHelper {

    private static final Logger logger = Logger.getLogger("Request");
    /**
     * 字体地址信息
     **/
    private static final String JSON_URL = "http://7xj62a.com1.z0.glb.clouddn.com/v1/fontlist-jt-android.json";


    public static void Request(String url, Callback callback, Object tag) {
        if (RKConfigHelper.getInstance().getRKConfig().isDebug()) {
            logger.debug("Request-->" + url);
        }
        OkHttpUtils
                .get()
                .url(url)
                .tag(tag)
                .build()
                .execute(callback);
    }


    /**
     * 获取字体信息列表
     **/
    public static void getFonInfos(FontInfoCallBack callBack) {
        Request(JSON_URL, callBack, null);
    }

    /**
     * 下载字体文件
     **/
    public static void downloadFontFile(String url, FileCallBack callBack, Activity activity) {
        Request(url, callBack, activity);
    }
}
