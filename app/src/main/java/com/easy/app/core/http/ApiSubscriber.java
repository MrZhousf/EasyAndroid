package com.easy.app.core.http;

import android.net.ParseException;
import android.text.TextUtils;

import com.easy.app.core.util.LogUtil;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * @author : zhousf
 */

public abstract class ApiSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    /**
     * 对 onError进行处理
     */
    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        while (throwable.getCause() != null){
            e = throwable;
            throwable = throwable.getCause();
        }
        if(e instanceof HttpException){//对网络异常
            HttpException httpException = (HttpException) e;
            if(TextUtils.isEmpty(httpException.getMessage())){
                LogUtil.d(ApiSubscriber.class.getName(),"网络异常");
            }else {
                String errorMsg = httpException.getMessage();
                LogUtil.d(ApiSubscriber.class.getName(),""+errorMsg);
            }
        }else if(e instanceof ApiException){//服务器返回的错误
            onResultError((ApiException) e);
        }else if(e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){//解析异常
            LogUtil.d(ApiSubscriber.class.getName(),"解析异常");
        }else if(e instanceof UnknownHostException){
            LogUtil.d(ApiSubscriber.class.getName(),"请求地址错误");
        }else if(e instanceof SocketTimeoutException) {
            LogUtil.d(ApiSubscriber.class.getName(),"请求超时");
        }else {//未知错误
            e.printStackTrace();
        }
    }
    /**
     * 服务器返回的错误
     * @param ex
     */
    protected  void onResultError(ApiException ex){
        LogUtil.d(ApiSubscriber.class.getName()," "+ex.getMessage());
        ex.printStackTrace();
    }


}
