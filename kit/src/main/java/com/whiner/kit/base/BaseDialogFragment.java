package com.whiner.kit.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;

import com.hjq.toast.Toaster;
import com.whiner.kit.tv.OnTvKeyBackListener;

public abstract class BaseDialogFragment<V extends ViewBinding> extends AppCompatDialogFragment {

    private static final String TAG = "BaseDialogFragment";

    protected volatile boolean cancel = true;

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    protected volatile boolean show = false;

    public void showDialog(@NonNull FragmentManager fragmentManager) {
        if (show) return;
        show = true;
        show(fragmentManager, TAG + hashCode());
    }

    public void hideDialog() {
        show = false;
        dismissAllowingStateLoss();
    }

    protected V viewBinding;

    protected abstract V getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract String getBackTip();

    protected abstract void initView();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnTvKeyBackListener() {
            @Override
            public boolean onBack(View view, DialogInterface dialog) {
                if (isCancel()) {
                    return false;
                } else {
                    Toaster.show(getBackTip());
                    return true;
                }
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //取消默认的背景
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
        //设置View
        viewBinding = getViewBinding(inflater, container);
        initView();
        return viewBinding.getRoot();
    }

}
