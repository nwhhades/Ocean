package com.whiner.kit.tv.ui.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.whiner.kit.base.BaseDialogFragment;
import com.whiner.kit.databinding.FragmentLoadingDialogBinding;

public class LoadingDialogFragment extends BaseDialogFragment<FragmentLoadingDialogBinding> {

    private static final String def_msg = "数据加载中...";

    private String msg = def_msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    protected FragmentLoadingDialogBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentLoadingDialogBinding.inflate(inflater, container, false);
    }

    @Override
    protected String getBackTip() {
        return msg;
    }

    @Override
    protected void initView() {
        setCancel(false);
    }

    public void showLoading(@NonNull FragmentManager manager, String msg) {
        if (msg != null) setMsg(msg);
        showDialog(manager);
    }

    public void hideLoading() {
        setMsg(def_msg);
        hideDialog();
    }

}
