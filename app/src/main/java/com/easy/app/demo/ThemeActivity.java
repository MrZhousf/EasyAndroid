package com.easy.app.demo;

import android.os.Bundle;
import android.view.View;

import com.easy.R;
import com.easy.app.core.base.BaseActivity;
import com.easy.app.core.plugin.theme.ThemeConstant;
import com.easy.app.core.plugin.theme.ThemeManager;
import com.easy.app.core.plugin.title.TitleBar;

public class ThemeActivity extends BaseActivity<TitleBar> {

    @Override
    protected void initTitle(TitleBar titleBar) {
        titleBar.title = getString(R.string.theme_manager);
    }

    @Override
    protected Object initLayout() {
        return R.layout.activity_theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void changeTheme(View v){
        switch (v.getId()){
            case R.id.theme_default:
                ThemeManager.get().setCurrentTheme(this, ThemeConstant.THEME_DEFAULT);
                break;
            case R.id.theme_night:
                ThemeManager.get().setCurrentTheme(this,ThemeConstant.THEME_NIGHT);
                break;
        }
    }

}
