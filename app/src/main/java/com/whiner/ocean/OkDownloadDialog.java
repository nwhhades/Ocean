package com.whiner.ocean;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hjq.toast.Toaster;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener1;
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist;
import com.whiner.kit.install.ApkReceiver;
import com.whiner.kit.install.InstallEvent;
import com.whiner.kit.install.InstallUtils;
import com.whiner.kit.tv.ui.fragment.DownloadDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OkDownloadDialog extends DownloadDialogFragment {

    private static final String TAG = "OkDownloadDialog";
    private final Lock lock = new ReentrantLock();
    private DownloadTask downloadTask;
    private final DownloadListener1 downloadListener = new DownloadListener1() {
        @Override
        public void taskStart(@NonNull DownloadTask task, @NonNull Listener1Assist.Listener1Model model) {
            downloadStart();
        }

        @Override
        public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

        }

        @Override
        public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {

        }

        @Override
        public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {
            downloadProgress(currentOffset, totalLength);
        }

        @Override
        public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull Listener1Assist.Listener1Model model) {
            Log.d(TAG, "taskEnd: " + cause);
            if (cause == EndCause.CANCELED) {
                downloadEnd();
            } else if (cause == EndCause.COMPLETED) {
                setProgress(100);
                downloadSuccess(task.getFile());
            } else {
                downloadFail();
            }
            if (realCause != null) {
                realCause.printStackTrace();
            }
        }
    };

    @Override
    protected void setUrl(String url, String path) {
        if (downloadTask != null) {
            downloadTask.cancel();
            downloadTask = null;
        }
        downloadTask = new DownloadTask.Builder(url, path, "")
                .setConnectionCount(1)
                .setMinIntervalMillisCallbackProcess(50)
                .setPassIfAlreadyCompleted(false)
                .build();
    }

    @Override
    protected void startDownload() {
        //在新建线程中操作
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (lock.tryLock()) {
                    try {
                        if (downloadTask != null) {
                            setBtn2Text("启动中...", false);
                            downloadTask.cancel();
                            sleep(1000);
                            downloadTask.enqueue(downloadListener);
                        } else {
                            Toaster.showShort("下载地址为空！");
                            hideDialog();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                } else {
                    Log.d(TAG, "run: 获取锁失败");
                }
            }
        }.start();
    }

    @Override
    protected void stopDownload() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (lock.tryLock()) {
                    try {
                        setBtn1Text("停止中...", false);
                        if (downloadTask != null) {
                            downloadTask.cancel();
                        }
                        sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                        hideDialog();
                    }
                } else {
                    Log.d(TAG, "run: 获取锁失败");
                }
            }
        }.start();
    }

    @Override
    protected void downloadStart() {
        setBtn2Text("下载中...", false);
    }

    @Override
    protected void downloadProgress(long cur, long total) {
        int p = (int) (cur * 100f / total);
        setProgress(p);
    }

    @Override
    protected void downloadSuccess(File file) {
        setBtn2Text("安装中...", false);
        installApk(file);
    }

    @Override
    protected void downloadFail() {
        setBtn2Text("重试", true);
    }

    @Override
    protected void downloadEnd() {
        setBtn2Text("继续", true);
    }

    protected volatile String installPackageName = null;

    private void installApk(File file) {
        Log.d(TAG, "installApk: 开始安装" + file);
        installPackageName = InstallUtils.install(file);
        if (installPackageName == null) {
            hideDialog();
            return;
        }
        ApkReceiver apkReceiver = new ApkReceiver(getActivity());
        getLifecycle().addObserver(apkReceiver);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInstallEvent(InstallEvent installEvent) {
        try {
            Log.d(TAG, "onInstallEvent: " + installEvent);
            if (installEvent.getPackageName().equals(installPackageName)) {
                if (installEvent.isSuccess()) {
                    Log.d(TAG, "onInstallEvent: 应用安装成功了！");
                    hideDialog();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
