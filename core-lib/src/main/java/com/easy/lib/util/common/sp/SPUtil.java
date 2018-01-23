package com.easy.lib.util.common.sp;

import android.content.Context;

import com.easy.lib.CoreApplication;


public class SPUtil {

    /**
     * 主题SP定制
     */
    public CoreSharePreference theme(){
        return new CoreSharePreference(CoreApplication.get().getSharedPreferences("sp_core_theme", Context.MODE_PRIVATE));
    }


    /**
     * 天气SP定制
     */
    public CoreSharePreference weather(){
        return new CoreSharePreference(CoreApplication.get().getSharedPreferences("sp_core_weather", Context.MODE_PRIVATE));
    }




}
