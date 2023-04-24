package com.whiner.kit.base;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.hjq.toast.Toaster;
import com.whiner.kit.mmkv.DefMMKV;
import com.whiner.kit.toaster.BigBlackToastStyle;

public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        DefMMKV.init(this);
        initToaster();
        init();
    }

    protected void initToaster() {
        Toaster.init(this, new BigBlackToastStyle());
    }

    protected abstract void init();

}
