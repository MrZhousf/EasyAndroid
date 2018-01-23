package com.easy.lib.plugin.title;

import android.view.View;
import android.view.ViewGroup;

import com.easy.lib.CoreActivity;


public interface ITitleBar {

    /**
     * 绑定标题栏插件
     */
    Class bindPlugin();

    /**
     * 初始化标题栏视图
     * @param context 上下文
     * @param parentView 父类容器
     * @param onClickListener 点击监听
     */
    void initView(CoreActivity context, ViewGroup parentView, View.OnClickListener onClickListener);

    /**
     * 标题栏更新
     */
    void updateTitle();

}
