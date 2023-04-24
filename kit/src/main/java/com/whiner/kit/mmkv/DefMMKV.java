package com.whiner.kit.mmkv;

import android.content.Context;
import android.util.Log;

import com.tencent.mmkv.MMKV;

public enum DefMMKV implements IKVCache {
    ONE;

    private static final String TAG = "DefMMKV";

    public static void init(final Context context) {
        String root = MMKV.initialize(context);
        Log.d(TAG, "initMMKV: MMKV root is " + root);
    }

    @Override
    public MMKV getMMKV() {
        return MMKV.defaultMMKV();
    }

    @Override
    public long getNotExpireTime() {
        return -1;
    }

    @Override
    public String getKeySuffix() {
        return "_time";
    }

    @Override
    public String get(String key, String def) {
        if (key == null) return def;
        long outTime = getMMKV().getLong(key + getKeySuffix(), 0);
        if (outTime > System.currentTimeMillis() || outTime == getNotExpireTime()) {
            return getMMKV().getString(key, def);
        } else {
            getMMKV().removeValueForKey(key);
            getMMKV().removeValueForKey(key + getKeySuffix());
        }
        return def;
    }

    @Override
    public void put(String key, String val, long keep_time) {
        if (key == null || val == null) return;
        getMMKV().putString(key, val);
        if (keep_time > 0) {
            getMMKV().putLong(key + getKeySuffix(), System.currentTimeMillis() + keep_time);
        } else {
            getMMKV().putLong(key + getKeySuffix(), getNotExpireTime());
        }
    }

    @Override
    public void put(String key, String val) {
        put(key, val, getNotExpireTime());
    }

}
