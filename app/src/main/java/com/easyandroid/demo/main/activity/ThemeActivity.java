package com.easyandroid.demo.main.activity;

import android.os.Bundle;
import android.view.View;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseActivity;
import com.easyandroid.core.constant.ThemeConstant;
import com.easyandroid.core.util.ThemeManager;

public class ThemeActivity extends BaseActivity {

    @Override
    protected int initLayout() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.title = getString(R.string.theme_manager);
        title.showSearch = false;
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
