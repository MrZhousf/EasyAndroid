package com.easy.app.core.base;

import com.easy.app.core.http.Repository;
import com.easy.app.core.plugin.theme.ThemePlugin;
import com.easy.app.core.plugin.title.TitleBarPlugin;
import com.easy.app.core.util.CrashApphandler;
import com.easy.lib.CoreApplication;
import com.easy.lib.plugin.PluginManager;

/**
 * @author : zhousf
 */
public class BaseApplication extends CoreApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        initRetrofit();
//        initCrashAppLog();
    }

    @Override
    protected void registerPlugin(PluginManager pluginManager) {
        pluginManager
                .registerPlugin(new ThemePlugin())
                .registerPlugin(new TitleBarPlugin());
    }

    void initCrashAppLog(){
        CrashApphandler.getInstance().init(this);
    }


    void initRetrofit(){
        Repository.init(null);
    }



}
