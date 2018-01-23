package com.easy.lib.plugin.common;

import android.content.Context;
import android.os.Environment;

import com.easy.lib.plugin.CorePlugin;
import com.easy.lib.util.Util;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.cookie.PersistentCookieJar;
import com.okhttplib.cookie.cache.SetCookieCache;
import com.okhttplib.cookie.persistence.SharedPrefsCookiePersistor;

public class HttpPlugin extends CorePlugin {

    @Override
    public boolean initPlugin(Context context) {
        String downloadFileDir = Environment.getExternalStorageDirectory().getPath()+"/okHttp_download/";
        OkHttpUtil.init(context.getApplicationContext())
                .setConnectTimeout(30)//连接超时时间
                .setWriteTimeout(30)//写超时时间
                .setReadTimeout(30)//读超时时间
                .setMaxCacheSize(10 * 1024 * 1024)//缓存空间大小
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setShowHttpLog(true)//显示请求日志
                .setHttpLogTAG("HttpLog")
                .setShowLifecycleLog(false)//显示Activity销毁日志
                .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setDownloadFileDir(downloadFileDir)//文件下载保存目录
                .addResultInterceptor(HttpInterceptor.ResultInterceptor)//请求结果拦截器
                .addExceptionInterceptor(HttpInterceptor.ExceptionInterceptor)//请求链路异常拦截器
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)))//持久化cookie
                .build();
        Util.Log.d(TAG,"网络库初始化成功");
        return true;
    }

}
