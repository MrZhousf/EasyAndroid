package com.easy.app.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.easy.app.core.base.BaseApplication;


public class SPUtil {

    //存储的文件名
    private static final String FILE_NAME = "easy_android_data";

    private static SPUtil spUtil;
    private static SharedPreferences sharedPreferences;

    public static SPUtil getInstance(){
        if(null == spUtil){
            synchronized (SPUtil.class){
                if(null == spUtil)
                    spUtil = new SPUtil();
            }
        }
        return spUtil;
    }

    private SPUtil(){
        sharedPreferences = BaseApplication.get().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
    }

    public void put(String key, Object data){
        if(TextUtils.isEmpty(key) || null == data)
            return ;
        String type = data.getClass().getSimpleName();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if ("Integer".equals(type)){
            editor.putInt(key, (Integer)data);
        }else if ("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)data);
        }else if ("String".equals(type)){
            editor.putString(key, (String)data);
        }else if ("Float".equals(type)){
            editor.putFloat(key, (Float)data);
        }else if ("Long".equals(type)){
            editor.putLong(key, (Long)data);
        }
        editor.apply();//异步提交
    }

    public int getInt(String key, int defValue){
        if(TextUtils.isEmpty(key))
            return -1;
        return sharedPreferences.getInt(key, defValue);
    }

    public String getString(String key, String defValue){
        if(TextUtils.isEmpty(key))
            return null;
        return sharedPreferences.getString(key, defValue);
    }

    public static void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


}
