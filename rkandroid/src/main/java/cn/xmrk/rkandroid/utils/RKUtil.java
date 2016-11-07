package cn.xmrk.rkandroid.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.xmrk.rkandroid.application.RKApplication;
import cn.xmrk.rkandroid.config.RKConfigHelper;

/**
 * 创建日期： 2015/11/12.
 */
public class RKUtil {

    //加载本地的图片
    public static void displayFileImage(String url, ImageView iv, int res, Activity activity) {
        if (url != null) {
            url = url.startsWith("file://") ? url : "file://" + url;
        }
        Glide.with(activity).load(url).dontAnimate().placeholder(generateDisplayDrawable(res)).into(iv);
    }
    //加载本地的图片
    public static void displayFileImage(String url, ImageView iv, int res, Fragment fragment) {
        if (url != null) {
            url = url.startsWith("file://") ? url : "file://" + url;
        }
        Glide.with(fragment).load(url).dontAnimate().placeholder(generateDisplayDrawable(res)).into(iv);
    }

    //加载网络的图片
    public static void displayImage(String url, ImageView iv, int res, Activity activity) {
        if (url != null) {
            url = url.startsWith("http://") ? url : RKConfigHelper.getInstance().getRKConfig().getBaseUrl() + url;
        }
        Glide.with(activity).load(url).dontAnimate().placeholder(generateDisplayDrawable(res)).into(iv);
    }

    //加载网络的图片
    public static void displayImage(String url, ImageView iv, int res, Fragment fragment) {
        if (url != null) {
            url = url.startsWith("http://") ? url : RKConfigHelper.getInstance().getRKConfig().getBaseUrl() + url;
        }
        Glide.with(fragment).load(url).dontAnimate().placeholder(generateDisplayDrawable(res)).into(iv);
    }

    /**
     * @param def 默认图片
     * @return
     */
    public static Drawable generateDisplayDrawable(int def) {
        if (def == 0) {
            return new ColorDrawable(Color.WHITE);
        } else {
            return RKApplication.getInstance().getResources().getDrawable(def);
        }

    }
}
