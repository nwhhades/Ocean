package com.whiner.kit.tv.ui.fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.whiner.kit.R;
import com.whiner.kit.base.BaseDialogFragment;
import com.whiner.kit.databinding.FragmentDownloadDialogBinding;

import java.io.File;

public abstract class DownloadDialogFragment extends BaseDialogFragment<FragmentDownloadDialogBinding> implements View.OnClickListener {

    protected final Handler handler = new Handler();

    @Override
    protected FragmentDownloadDialogBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentDownloadDialogBinding.inflate(inflater, container, false);
    }

    @Override
    protected String getBackTip() {
        return "下载安装中请等待";
    }

    @Override
    protected void initView() {
        setCancel(false);
        viewBinding.tvTitle.setText(title);
        viewBinding.tvContent.setText(content);
        viewBinding.tvProgress.setText(getString(R.string.tv_progress, progress));
        viewBinding.progressBar.setProgress(progress);
        //点击事件
        viewBinding.btn1.setOnClickListener(this);
        viewBinding.btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn2) {
            startDownload();
        } else {
            stopDownload();
        }
    }

    protected String title;

    public void setTitle(final String title) {
        this.title = title;
        handler.post(() -> {
            if (viewBinding != null) viewBinding.tvTitle.setText(title);
        });
    }

    protected String content;

    public void setContent(final String content) {
        this.content = content;
        handler.post(() -> {
            if (viewBinding != null) viewBinding.tvContent.setText(content);
        });
    }

    protected int progress;

    public void setProgress(final int progress) {
        this.progress = progress;
        handler.post(() -> {
            if (viewBinding != null) {
                viewBinding.tvProgress.setText(getString(R.string.tv_progress, progress));
                viewBinding.progressBar.setProgress(progress);
            }
        });
    }

    protected void setBtn1Text(final String text, boolean btnClick) {
        handler.post(() -> {
            if (viewBinding != null) {
                viewBinding.btn1.setText(text);
                if (btnClick) {
                    viewBinding.btn1.setOnClickListener(DownloadDialogFragment.this);
                } else {
                    viewBinding.btn1.setOnClickListener(null);
                }
            }
        });
    }

    protected void setBtn2Text(final String text, boolean btnClick) {
        handler.post(() -> {
            if (viewBinding != null) {
                viewBinding.btn2.setText(text);
                if (btnClick) {
                    viewBinding.btn2.setOnClickListener(DownloadDialogFragment.this);
                } else {
                    viewBinding.btn2.setOnClickListener(null);
                }
            }
        });
    }

    protected abstract void setUrl(final String url, final String path);

    protected abstract void startDownload();

    protected abstract void stopDownload();

    protected abstract void downloadStart();

    protected abstract void downloadProgress(final long cur, final long total);

    protected abstract void downloadSuccess(final File file);

    protected abstract void downloadFail();

    protected abstract void downloadEnd();

}
