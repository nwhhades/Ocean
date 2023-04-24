package com.whiner.kit.utils;

import android.util.Log;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.StringUtils;
import com.whiner.kit.mmkv.DefMMKV;

import java.util.UUID;

public class UuidUtils {

    private static final String TAG = "UuidUtils";

    private static String getUuidPath() {
        return PathUtils.getExternalDownloadsPath() + "/ocean/device.uuid";
    }

    public static String getUUID() {
        String uniqueID = readUUID();
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            //保存
            saveUUID(uniqueID);
        }
        return uniqueID;
    }

    private static void saveUUID(String uuid) {
        if (SysUtils.isSysApp()) SysUtils.setValue(TAG, uuid);
        boolean b = FileIOUtils.writeFileFromString(getUuidPath(), uuid);
        if (b) return;
        DefMMKV.ONE.put(TAG, uuid);
    }

    private static String readUUID() {
        String uniqueID = null;
        try {
            if (SysUtils.isSysApp()) {
                Log.d(TAG, "readUUID: 从系统数据库中读取");
                uniqueID = SysUtils.getValue(TAG);
            }
            if (StringUtils.isEmpty(uniqueID)) {
                Log.d(TAG, "readUUID: 从文件中读取");
                uniqueID = FileIOUtils.readFile2String(getUuidPath());
            }
            if (StringUtils.isEmpty(uniqueID)) {
                Log.d(TAG, "readUUID: 从MMKV中读取");
                uniqueID = DefMMKV.ONE.get(TAG, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uniqueID;
    }

}
