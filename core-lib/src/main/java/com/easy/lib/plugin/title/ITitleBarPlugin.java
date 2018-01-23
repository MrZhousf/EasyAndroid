package com.easy.lib.plugin.title;

import android.view.View;
import android.view.ViewGroup;

import com.easy.lib.CoreActivity;


public interface ITitleBarPlugin {

    /**
     * 绑定标题栏
     * @param titleBar 标题栏
     */
    void bindTitleBar(ITitleBar titleBar);

    /**
     * 初始化标题栏视图
     * @param context 上下文
     * @param onClickListener 点击监听
     */
    ViewGroup initTitleView(CoreActivity context, View.OnClickListener onClickListener);

    /**
     * 是否显示标题栏
     */
    boolean hasTitleBar();

}
