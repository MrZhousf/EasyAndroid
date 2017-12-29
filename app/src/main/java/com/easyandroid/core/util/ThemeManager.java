package com.easyandroid.core.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.easyandroid.core.bean.UpdateThemeEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Author : zhousf
 * Description :
 * Date : 2017/12/28.
 */
public class ThemeManager {

    private static ThemeManager singleton;

    public static final String THEME_DEFAULT = "theme.manager.theme.default";
    private static final String THEME_CURRENT = "theme.manager.theme.current";
    private final int DEFAULT_VALUE = -1;

    public static ThemeManager get() {
        if (null == singleton) {
            synchronized (ThemeManager.class) {
                if (null == singleton) {
                    singleton = new ThemeManager();
                }
            }
        }
        return singleton;
    }

    private ThemeManager() {
    }

    public ThemeManager registerThemeDefault(int value){
        SPUtil.getInstance().put(THEME_DEFAULT,value);
        return this;
    }

    public ThemeManager registerTheme(String key,int value){
        if(TextUtils.isEmpty(key))
            return this;
        if(THEME_DEFAULT.equals(key))
            return this;
        SPUtil.getInstance().put(key,value);
        return this;
    }

    public void setCurrentTheme(Activity activity,String key){
        int current = SPUtil.getInstance().getInt(key,DEFAULT_VALUE);
        if(current != SPUtil.getInstance().getInt(THEME_CURRENT,DEFAULT_VALUE)){
            if(current == DEFAULT_VALUE)
                return ;
            EventBus.getDefault().post(new UpdateThemeEvent(activity.getClass().getName()));
            SPUtil.getInstance().put(THEME_CURRENT,current);
            activity.startActivity(new Intent(activity, activity.getClass()));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            activity.finish();
        }
    }


    public int getCurrentTheme(){
        int current = SPUtil.getInstance().getInt(THEME_CURRENT,DEFAULT_VALUE);
        if(current == DEFAULT_VALUE){
            current = SPUtil.getInstance().getInt(THEME_DEFAULT,DEFAULT_VALUE);
        }
        return current;
    }


}
