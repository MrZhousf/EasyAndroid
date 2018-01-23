package com.easy.app.core.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easy.R;
import com.easy.app.core.plugin.theme.ThemeEvent;
import com.easy.app.core.plugin.theme.ThemeManager;
import com.easy.lib.CoreActivity;
import com.easy.lib.plugin.PluginManager;
import com.easy.lib.plugin.title.ITitleBar;
import com.easy.lib.plugin.title.ITitleBarPlugin;
import com.easy.lib.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<T> extends CoreActivity<T> implements View.OnClickListener{

    /**
     * 初始化标题栏
     */
    protected abstract void initTitle(T titleBar);

    /**
     * 初始化视图布局文件
     */
    protected abstract Object initLayout();


    //标题栏插件
    protected ITitleBarPlugin titleBarPlugin;

    //页面内容
    private ViewGroup contentView;

    //DataBinding绑定视图
    protected View bindView;


    @Override
    public void setTheme(int resId) {
        super.setTheme(ThemeManager.get().getCurrentTheme());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //竖屏，禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //初始化标题栏
        initTitleBar();
        //初始化页面布局
        initContentView(initLayout());
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar(){
        titleBar = fetchTitleBar();
        if(titleBar == null)
            return ;
        ITitleBar iTitleBar = (ITitleBar) titleBar;
        //初始化标题栏属性
        titleBarPlugin = PluginManager.get().getPlugin(iTitleBar.bindPlugin());
        if(titleBarPlugin == null){
            Util.Log.e(TAG,getString(R.string.plugin_not_init, titleBar.getClass().getSimpleName()));
            return ;
        }
        initTitle(titleBar);
        //绑定标题栏
        titleBarPlugin.bindTitleBar(iTitleBar);
        //初始化标题栏视图
        initTitleView(titleBarPlugin);
    }

    /**
     * 初始化标题栏视图
     * @param titleBarPlugin 主题插件
     */
    private void initTitleView(ITitleBarPlugin titleBarPlugin){
        if(!titleBarPlugin.hasTitleBar())
            return ;
        try {
            contentView = titleBarPlugin.initTitleView(this,this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化标题栏视图
     */
    private void initContentView(Object view){
        if(view == null){
            view = 0;
        }
        if(view instanceof Integer){
            setContentView((Integer) view);
        } else if(view instanceof View){
            bindView = (View) view;
            setContentView(0);
        }
    }

    @Override
    public void setContentView(View view) {
        //当绑定了DataBinding视图则阻止手动设置视图
        if(bindView == null){
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if(contentView != null){
            ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
            viewGroup.removeAllViews();
            if(bindView != null){
                //加载DataBinding视图
                contentView.addView(bindView);
                viewGroup.addView(contentView);
            }else{
                View view = LayoutInflater.from(this).inflate(layoutResID, contentView, true);
                viewGroup.addView(view);
            }
        }else{
            if(layoutResID != 0){
                super.setContentView(layoutResID);
            }
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTheme(ThemeEvent event){
        if(!getClass().getName().equals(event.className)){
            recreate();
        }
    }

    @SuppressWarnings(value = "unchecked")
    private Class<T> initClazz(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if(params.length > 0){
            if(params[0] instanceof Class){
                return (Class<T>) params[0];
            }
        }
        return null;
    }

    /**
     * 获取标题实例
     */
    private T fetchTitleBar(){
        T titleBar = null;
        try {
            Class<T> title = initClazz();
            if(title != null){
                titleBar = title.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titleBar;
    }



}
