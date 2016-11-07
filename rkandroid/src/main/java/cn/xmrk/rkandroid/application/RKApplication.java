package cn.xmrk.rkandroid.application;

import android.app.Application;

import cn.xmrk.rkandroid.config.IRKConfig;
import cn.xmrk.rkandroid.config.RKConfigHelper;

public abstract class RKApplication extends Application {

    private static RKApplication mApplication;

    public static final RKApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        init();
    }

    private void init() {
        RKConfigHelper.getInstance().init(this, getRKConfig());
    }

    protected abstract IRKConfig getRKConfig();

}
