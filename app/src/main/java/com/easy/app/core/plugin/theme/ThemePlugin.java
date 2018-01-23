package com.easy.app.core.plugin.theme;

import android.content.Context;

import com.easy.R;
import com.easy.lib.plugin.CorePlugin;
import com.easy.lib.util.Util;


public class ThemePlugin extends CorePlugin {

    @Override
    public boolean initPlugin(Context context) {
        ThemeManager.get()
                .registerThemeDefault(R.style.ThemeDefault)//默认主题
                .registerTheme(ThemeConstant.THEME_NIGHT,R.style.ThemeNight);//夜色主题
        Util.Log.d(TAG,"主题插件初始化成功");
        return true;
    }

}
