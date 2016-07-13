package cn.xmrk.jbook.helper;

import android.content.Context;
import android.graphics.Color;

import cn.xmrk.rkandroid.utils.CommonUtil;
import cn.xmrk.rkandroid.utils.SharedPreferencesUtil;

/**
 * 保存用户上次的画笔颜色和大小
 */
public class PaintHelper extends SharedPreferencesUtil {

    public PaintHelper(Context context) {
        super(context, "paint");
    }

    public void saveTextSize(float size) {
        putFloat("size", size);
    }

    public float getTextSize() {
        return getFloat("size", CommonUtil.dip2px(20));
    }


    public void saveTextColor(int color) {
        putInt("color", color);
    }

    public int getTextColor() {
        return getInt("color", Color.GRAY);
    }
}
