package com.easy.app.core.plugin.theme;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.easy.lib.util.Util;

import org.greenrobot.eventbus.EventBus;

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

    /**
     * 注册默认主题
     * @param value 主题
     */
    public ThemeManager registerThemeDefault(int value){
        Util.SP.theme().put(THEME_DEFAULT,value);
        return this;
    }

    /**
     * 注册主题
     * @param key 主题标识
     * @param value 主题
     */
    public ThemeManager registerTheme(String key, int value){
        if(TextUtils.isEmpty(key))
            return this;
        if(THEME_DEFAULT.equals(key))
            return this;
        Util.SP.theme().put(key,value);
        return this;
    }

    /**
     * 设置当前主题
     * @param activity 上下文
     * @param key 主题标识
     */
    public void setCurrentTheme(Activity activity, String key){
        int current = Util.SP.theme().getInt(key,DEFAULT_VALUE);
        if(current != Util.SP.theme().getInt(THEME_CURRENT,DEFAULT_VALUE)){
            if(current == DEFAULT_VALUE)
                return ;
            EventBus.getDefault().post(new ThemeEvent(activity.getClass().getName()));
            Util.SP.theme().put(THEME_CURRENT,current);
            activity.startActivity(new Intent(activity, activity.getClass()));
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            activity.finish();
        }
    }

    /**
     * 获取当前主题
     */
    public int getCurrentTheme(){
        int current = Util.SP.theme().getInt(THEME_CURRENT,DEFAULT_VALUE);
        if(current == DEFAULT_VALUE){
            current = Util.SP.theme().getInt(THEME_DEFAULT,DEFAULT_VALUE);
        }
        return current;
    }


}
