package com.easy.lib;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.easy.lib.plugin.PluginManager;
import com.easy.lib.plugin.common.DBPlugin;
import com.easy.lib.plugin.common.HttpPlugin;
import com.easy.lib.util.Util;

public abstract class CoreApplication extends MultiDexApplication {

    static CoreApplication coreApplication;

    protected final String TAG = getClass().getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        coreApplication = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Util.Log.d(TAG,TAG+"已启动");
        //初始化插件管理
        initPluginManager().init(getApplicationContext(),false);
    }

    /**
     * 初始化插件管理
     */
    private PluginManager initPluginManager(){
        PluginManager pluginManager = PluginManager.get();
        //注册核心插件
        pluginManager.registerPlugin(new HttpPlugin())
                .registerPlugin(new DBPlugin());
        //注册App插件
        registerPlugin(pluginManager);
        return pluginManager;
    }

    /**
     * 注册插件
     * @param pluginManager 插件管理
     */
    protected abstract void registerPlugin(PluginManager pluginManager);

    public static CoreApplication get(){
        return coreApplication;
    }


}
