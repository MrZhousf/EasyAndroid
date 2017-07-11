package com.easyandroid.core.base;

import android.app.Application;

import com.easyandroid.demo.main.util.CrashApphandler;

/**
 * @author : zhousf
 */

public class BaseApplication extends Application {

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
