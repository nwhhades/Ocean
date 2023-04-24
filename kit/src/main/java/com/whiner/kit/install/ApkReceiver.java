package com.whiner.kit.install;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

public class ApkReceiver extends BroadcastReceiver implements LifecycleEventObserver {

    private static final String TAG = "ApkReceiver";

    protected final WeakReference<Activity> weakReference;

    public ApkReceiver(Activity activity) {
        this.weakReference = new WeakReference<>(activity);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        if (intent != null) {
            Log.d(TAG, "onReceive: " + intent.getAction());
            String packageName = intent.getData().getSchemeSpecificPart();
            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_PACKAGE_ADDED:
                    Log.d(TAG, "onReceive: " + packageName + " - 安装成功");
                    sendApkEvent(packageName, 1);
                    break;
                case Intent.ACTION_PACKAGE_REMOVED:
                    Log.d(TAG, "onReceive: " + packageName + " - 安装成功");
                    sendApkEvent(packageName, 2);
                    break;
                default:
                    break;
            }
        }
    }

    private void sendApkEvent(String packageName, int i) {
        InstallEvent installEvent = new InstallEvent(packageName, i == 1);
        EventBus.getDefault().post(installEvent);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        Activity activity = weakReference.get();
        if (activity == null) return;
        if (event == Lifecycle.Event.ON_CREATE) {
            register(activity);
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            unregister(activity);
        }
    }

    private void register(Activity activity) {
        Log.d(TAG, "onStateChanged: 开始注册");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        activity.registerReceiver(this, intentFilter);
    }

    private void unregister(Activity activity) {
        Log.d(TAG, "onStateChanged: 注销注册");
        activity.unregisterReceiver(this);
    }

}
