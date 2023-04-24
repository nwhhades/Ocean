package com.whiner.kit.install;

import com.blankj.utilcode.util.AppUtils;

import java.io.File;

public class InstallUtils {

    public static String install(final File file) {
        AppUtils.AppInfo appInfo = AppUtils.getApkInfo(file);
        if (appInfo == null) {
            return null;
        }
        AppUtils.installApp(file);
        return appInfo.getPackageName();
    }

}
