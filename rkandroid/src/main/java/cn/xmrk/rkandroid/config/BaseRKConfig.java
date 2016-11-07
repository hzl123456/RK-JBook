package cn.xmrk.rkandroid.config;

/**
 * 创建日期： 2016/1/26.
 */
public abstract class BaseRKConfig implements IRKConfig {

    @Override
    public boolean isLeakWatch() {
        return false;
    }

}
