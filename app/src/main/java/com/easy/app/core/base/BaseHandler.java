package com.easy.app.core.base;

import android.os.Handler;
import android.os.Message;

/**
 * 通用句柄
 */
public class BaseHandler  extends Handler {

    private CallBack callBack;

    public interface CallBack{
        void handleMessage(Message msg);
    }

    public BaseHandler(CallBack callBack){
        this.callBack = callBack;
    }

    @Override
    public void handleMessage(Message msg) {
        if(null != callBack){
            callBack.handleMessage(msg);
        }
    }

}
