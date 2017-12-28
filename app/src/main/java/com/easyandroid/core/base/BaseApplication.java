package com.easyandroid.core.base;

import android.support.multidex.MultiDexApplication;

import com.easyandroid.R;
import com.easyandroid.core.util.ThemeManager;
import com.easyandroid.demo.main.util.CrashApphandler;

/**
 * @author : zhousf
 */

public class BaseApplication extends MultiDexApplication {

    static BaseApplication baseApplication;

    public static BaseApplication getApplication() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        initRetrofit();
        initCrashAppLog();
        ThemeManager.get().registerThemeDefault(R.style.ThemeDefault)
                .registerTheme("theme.manager.theme.night",R.style.ThemeNight);
    }
    void initCrashAppLog(){
        CrashApphandler.getInstance().init(this);
    }


    void initRetrofit(){
        Repository.init(getFilesDir());
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }



}
