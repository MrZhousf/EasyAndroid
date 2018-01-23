package com.easy.lib.util.common.sp;

import android.content.SharedPreferences;
import android.text.TextUtils;

public class CoreSharePreference {

    private SharedPreferences sharedPreferences;

    public CoreSharePreference(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public CoreSharePreference put(String key, Object data){
        if(TextUtils.isEmpty(key) || null == data)
            return this;
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
        return this;
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

    public boolean getBoolean(String key, boolean defValue){
        return !TextUtils.isEmpty(key) && sharedPreferences.getBoolean(key, defValue);
    }

    public long getLong(String key, long defValue){
        if(!TextUtils.isEmpty(key)){
            return sharedPreferences.getLong(key,defValue);
        }
        return 0;
    }

    public float getFloat(String key, float defValue){
        if(!TextUtils.isEmpty(key)){
            return sharedPreferences.getFloat(key,defValue);
        }
        return 0;
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }



}
