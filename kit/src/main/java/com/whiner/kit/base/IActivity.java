package com.whiner.kit.base;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewbinding.ViewBinding;

import java.util.List;

public interface IActivity<V extends ViewBinding> {

    /**
     * 获取ViewBinding
     *
     * @return ViewBinding
     */
    V getViewBinding();

    /**
     * 预初始化
     */
    void preInit();

    /**
     * 初始化View
     */
    void initView();

    /**
     * 是否启用APP全局背景
     *
     * @return true false
     */
    boolean enableAppBackground();

    /**
     * 创建背景的ImageView
     *
     * @return ImageView
     */
    AppCompatImageView createAppBackground();

    /**
     * 把全局背景添加到Root视图
     */
    void addAppBackground();

    /**
     * 加载url到ImageView
     *
     * @param url 图片地址
     */
    void loadAppBackground(String url);

    /**
     * 读取APP背景地址
     *
     * @return 背景地址
     */
    String readAppBackgroundSrc();

    /**
     * 写入APP背景地址到缓存中
     *
     * @param url 背景地址
     */
    void writeAppBackgroundSrc(String url);

    /**
     * 请求的权限列表
     *
     * @return 权限列表
     */
    List<String> getPermissionList();


    /**
     * 是否等待权限请求结果
     *
     * @return true false
     */
    boolean waitPermissionInit();

    /**
     * 初始化应用权限
     */
    void initPermission();

    /**
     * 权限成功
     */
    void permissionSuccess();

    /**
     * 权限失败
     */
    void permissionFail();

    /**
     * 显示LoadingView
     *
     * @param msg 按返回键提示文本
     */
    void showLoadingView(String msg);

    /**
     * 隐藏LoadingView
     */
    void hideLoadingView();

}
