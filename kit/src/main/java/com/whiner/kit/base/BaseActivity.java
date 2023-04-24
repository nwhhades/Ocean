package com.whiner.kit.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.bumptech.glide.Glide;
import com.hjq.permissions.XXPermissions;
import com.whiner.kit.R;
import com.whiner.kit.mmkv.DefMMKV;
import com.whiner.kit.tv.ui.fragment.LoadingDialogFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity<V extends ViewBinding> extends AppCompatActivity implements IActivity<V> {

    private static final String TAG = "BaseActivity";

    //主背景的地址缓存key
    private static final String KEY_APP_MAIN_BACKGROUND = "KEY_APP_MAIN_BACKGROUND";

    protected V viewBinding;

    protected AppCompatImageView ivAppBackground;

    @Override
    public void preInit() {
        Log.d(TAG, "preInit: setContentView前的预处理");
    }

    @Override
    public boolean enableAppBackground() {
        return true;
    }

    @Override
    public AppCompatImageView createAppBackground() {
        if (ivAppBackground == null) {
            ivAppBackground = new AppCompatImageView(this);
            ivAppBackground.setId(R.id.iv_app_background);
            ivAppBackground.setScaleType(ImageView.ScaleType.FIT_XY);
            ivAppBackground.setBackgroundResource(R.color.window_bg);
        }
        return ivAppBackground;
    }

    @Override
    public void addAppBackground() {
        if (viewBinding != null) {
            if (viewBinding.getRoot() instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) viewBinding.getRoot();
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                viewGroup.addView(createAppBackground(), 0, layoutParams);
            }
        }
    }

    @Override
    public void loadAppBackground(String url) {
        if (ivAppBackground != null && url != null) {
            Object srcTag = url.hashCode();
            Object tag = ivAppBackground.getTag();
            Log.d(TAG, "loadAppBackground: " + srcTag + " - " + tag);
            if (srcTag.equals(tag)) {
                Log.d(TAG, "loadAppBackground: 图片源的hashCode相同，跳过重新加载");
            } else {
                Glide.with(this)
                        .load(url)
                        .dontAnimate()
                        .into(ivAppBackground);
                ivAppBackground.setTag(srcTag);
            }
        }
    }

    @Override
    public String readAppBackgroundSrc() {
        return DefMMKV.ONE.get(KEY_APP_MAIN_BACKGROUND, null);
    }

    @Override
    public void writeAppBackgroundSrc(String url) {
        DefMMKV.ONE.put(KEY_APP_MAIN_BACKGROUND, url);
    }

    @Override
    public List<String> getPermissionList() {
        return null;
    }

    @Override
    public boolean waitPermissionInit() {
        return false;
    }

    @Override
    public void initPermission() {
        List<String> permissionList = getPermissionList();
        if (permissionList == null) return;
        //默认需要网络权限
        List<String> list = new ArrayList<>();
        list.add("android.permission.INTERNET");
        list.addAll(permissionList);
        XXPermissions.with(this)
                .permission(list)
                .request((permissions, allGranted) -> {
                    Log.d(TAG, "initPermission: " + permissions);
                    if (allGranted) {
                        permissionSuccess();
                    } else {
                        permissionFail();
                    }
                    if (waitPermissionInit()) initView();
                });
    }

    @Override
    public void permissionSuccess() {
        Log.d(TAG, "permissionSuccess: 权限正常");
    }

    @Override
    public void permissionFail() {
        Log.d(TAG, "permissionFail: 权限异常");
    }

    protected volatile LoadingDialogFragment loadingDialogFragment;

    @Override
    public synchronized void showLoadingView(String msg) {
        if (loadingDialogFragment == null) loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.showLoading(getSupportFragmentManager(), msg);
    }

    @Override
    public synchronized void hideLoadingView() {
        if (loadingDialogFragment != null) {
            loadingDialogFragment.hideLoading();
            loadingDialogFragment = null;
        }
    }

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 3840);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: " + this);
        super.onCreate(savedInstanceState);
        preInit();
        viewBinding = getViewBinding();
        //添加背景View
        if (enableAppBackground()) addAppBackground();
        setContentView(viewBinding.getRoot());
        initPermission();
        if (waitPermissionInit()) return;
        initView();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: " + this);
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: " + this);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: " + this);
        super.onResume();
        loadAppBackground(readAppBackgroundSrc());
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: " + this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: " + this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: " + this);
        hideLoadingView();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: " + this);
        super.onBackPressed();
    }

}
