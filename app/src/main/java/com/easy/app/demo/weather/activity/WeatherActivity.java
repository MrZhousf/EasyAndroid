package com.easy.app.demo.weather.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.easy.R;
import com.easy.app.core.base.HomeActivity;
import com.easy.app.demo.weather.adapter.HomePageAdapter;
import com.easy.app.demo.weather.fragment.CurrentCityFragment;
import com.easy.app.demo.weather.fragment.MultiCityFragment;

import butterknife.Bind;

/**
 * 天气模块
 */
public class WeatherActivity extends HomeActivity {


    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected int initLayout() {
        return R.layout.activity_weather;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.title = getString(R.string.weatherDemo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    void initView() {
        HomePageAdapter adapter = new HomePageAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrentCityFragment(),"当前城市");
        adapter.addFragment(new MultiCityFragment(),"自定义城市");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
