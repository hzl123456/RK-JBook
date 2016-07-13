package cn.xmrk.jbook.helper;

import cn.xmrk.jbook.R;

/**
 * Created by Au61 on 2016/7/7.
 */
public class JBookHelper {

    private static int texturePosition;

    private static int colorPosition;

    private static int[] textureRes = {
            R.drawable.texture1, R.drawable.texture2, R.drawable.texture3,
            R.drawable.texture4, R.drawable.texture5, R.drawable.texture6,
            R.drawable.texture7, R.drawable.texture8, R.drawable.texture9,
            R.drawable.texture10, R.drawable.texture11};

    private static int[] colorRes = {
            R.color.color_jb1, R.color.color_jb2, R.color.color_jb3,
            R.color.color_jb4, R.color.color_jb5, R.color.color_jb6,
            R.color.color_jb7, R.color.color_jb8, R.color.color_jb9,
            R.color.color_jb10, R.color.color_jb11};


    public static int getTextureRes() {
        return textureRes[texturePosition];
    }

    public static int getNextTextureRes() {
        texturePosition++;
        if (texturePosition > textureRes.length - 1) {
            texturePosition = 0;
        }
        return textureRes[texturePosition];
    }


    public static int getColorRes() {
        return colorRes[colorPosition];
    }

    public static int getNextColorRes() {
        colorPosition++;
        if (colorPosition > colorRes.length - 1) {
            colorPosition = 0;
        }
        return colorRes[colorPosition];
    }
}
