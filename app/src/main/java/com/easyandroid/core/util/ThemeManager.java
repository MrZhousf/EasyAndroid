package com.easyandroid.core.util;

import android.app.Activity;
import android.text.TextUtils;

/**
 * Author : zhousf
 * Description :
 * Date : 2017/12/28.
 */
public class ThemeManager {

    private static ThemeManager singleton;

    private final String THEME_DEFAULT = "theme.manager.theme.default";
    private final String THEME_CURRENT = "theme.manager.theme.current";

    private int currentTheme;

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
        int current = SPUtil.getInstance().getInt(key,-1);
        if(current != -1){
            SPUtil.getInstance().put(THEME_CURRENT,current);
            activity.recreate();
        }
    }

    public int getCurrentTheme(){
        int current = SPUtil.getInstance().getInt(THEME_CURRENT,-1);
        if(current == -1){
            current = SPUtil.getInstance().getInt(THEME_DEFAULT,-1);
        }
        return current;
    }


}
