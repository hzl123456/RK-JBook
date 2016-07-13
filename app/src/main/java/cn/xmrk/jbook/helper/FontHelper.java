package cn.xmrk.jbook.helper;

import android.graphics.Typeface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.xmrk.jbook.pojo.FontInfo;
import cn.xmrk.rkandroid.utils.CommonUtil;
import cn.xmrk.rkandroid.utils.StringUtil;

/**
 * Created by Au61 on 2016/7/12.
 */
public class FontHelper {

    private static List<Typeface> typeFaces;

    private static int typePosition;

    /**
     * 字体文件的后缀
     **/
    private static final String fontArea = ".jbook";

    /**
     * 字体的文件夹路径
     **/
    public static String getFatherPath() {
        String path = CommonUtil.getDir() + File.separator + "font";
        File file = new File(path);
        if (!file.exists()) { // 如果目录不存在，则创建一个名为"sddVoice"的目录
            file.mkdir();
        }
        return path;
    }

    /**
     * 下载时候的名称
     **/
    public static String getFileName(FontInfo info) {
        return StringUtil.getFileName(info.getResource_url());
    }

    /**
     * 判断是否已经下载了该文件
     **/
    public static boolean hasCompleteFileName(FontInfo info) {
        File file = new File(getFatherPath() + File.separator + info.getId() + fontArea);
        return file.exists();
    }

    /**
     * 改变文件的名称
     **/
    public static void changeFontFileName(FontInfo info) {
        File file = new File(getFatherPath() + File.separator + getFileName(info));
        file.renameTo(new File(getFatherPath() + File.separator + info.getId() + fontArea));
    }

    /**
     * 加载一个Typeface
     **/
    public static void loadTypeface() {
        if (typeFaces == null) {
            typeFaces = new ArrayList<>();
            /**
             * 首先把默认的加进去
             * **/
            typeFaces.add(Typeface.DEFAULT);
        }
        Typeface typeFace = null;
        //遍历取出结尾为fontArea的文件，然后加载为Typeface
        File mkFile = new File(getFatherPath());
        for (File file : mkFile.listFiles()) {
            if (file.getAbsolutePath().contains(fontArea)) {
                typeFaces.add(Typeface.createFromFile(file));
            }
        }
    }

    public static List<Typeface> getTypefaces() {
        return typeFaces;
    }

    /**
     * 添加一个字体
     **/
    public static void addTypeface(FontInfo info) {
        synchronized (typeFaces) {
            typeFaces.add(Typeface.createFromFile(new File(getFatherPath() + File.separator + info.getId() + fontArea)));
        }
    }

    /**
     * 改变一个字体
     **/
    public static Typeface changeTypeface() {
        typePosition++;
        if (typePosition > typeFaces.size() - 1) {
            typePosition = 0;
        }
        return typeFaces.get(typePosition);
    }
}
