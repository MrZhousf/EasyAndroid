package com.easyandroid.demo.video;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseActivity;
import com.easyandroid.demo.weather.adapter.HomePageAdapter;

import butterknife.Bind;

/**
 * 天气模块
 */
public class VideoActivity extends BaseActivity {


    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected int initLayout() {
        return R.layout.activity_video;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.title = getString(R.string.videoDemo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    void initView() {
        HomePageAdapter adapter = new HomePageAdapter(getSupportFragmentManager());
        adapter.addFragment(new VideoFragment(),"1");
        adapter.addFragment(new VideoFragment(),"2");
//        adapter.addFragment(new VideoFragment(),"3");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                LogUtil.e("VideoFragment","选择 "+(tab.getPosition()+1));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
