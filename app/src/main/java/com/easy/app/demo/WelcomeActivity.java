package com.easy.app.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.easy.R;
import com.easy.amap.AMapAPI;
import com.easy.app.core.base.HomeActivity;
import com.easy.app.core.util.LocationUtil;
import com.easy.app.core.util.StringUtil;
import com.easy.app.core.view.CircleImageView;
import com.easy.app.demo.camera.activity.TestCameraActivity;
import com.easy.app.demo.customer.CarActivity;
import com.easy.app.demo.databinding.DataBindingActivity;
import com.easy.app.demo.db.EasyDBActivity;
import com.easy.app.demo.net_speed.NetWorkActivity;
import com.easy.app.demo.video.VideoActivity;
import com.easy.app.demo.weather.activity.WeatherActivity;
import com.easy.lib.util.Util;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;

public class WelcomeActivity extends HomeActivity implements LocationUtil.LocationListener {

    @Bind(R.id.tvLocation)
    TextView tvLocation;
    @Bind(R.id.fbLocation)
    FloatingActionButton fbLocation;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private WelcomeAdapter adapter;

    @Override
    protected int initLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.title = "欢迎页";
        title.showNavigation = true;
        title.showSearch = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        LinearLayoutManager layoutManager = new GridLayoutManager(this,3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WelcomeAdapter();
        recyclerView.setAdapter(adapter);
        adapter.addItem(new WelcomeAdapter.Info(getString(R.string.weatherDemo),
                (view)-> startActivity(WeatherActivity.class)));
        adapter.addItem(new WelcomeAdapter.Info(getString(R.string.cameraDemo),
                (view)-> startActivity(TestCameraActivity.class)));
        adapter.addItem(new WelcomeAdapter.Info(getString(R.string.dbDemo),
                (view)-> startActivity(EasyDBActivity.class)));
        adapter.addItem(new WelcomeAdapter.Info(getString(R.string.dataBindingDemo),
                (view)-> startActivity(DataBindingActivity.class)));
        adapter.addItem(new WelcomeAdapter.Info(getString(R.string.networkDemo),
                (view)-> startActivity(NetWorkActivity.class)));
        adapter.addItem(new WelcomeAdapter.Info(getString(R.string.amapDemo),
                (view)-> {
                    Bundle bundle = new Bundle();
                    bundle.putString("searchKey","清华大学");
                    AMapAPI.toPOISearchActivity(this,bundle);
                }));
        adapter.addItem(new WelcomeAdapter.Info(getString(R.string.customerDemo),
                (view)-> startActivity(CarActivity.class)));
        adapter.addItem(new WelcomeAdapter.Info(getString(R.string.videoDemo),
                (view)-> startActivity(VideoActivity.class)));
    }

    void initView(){
        if(super.toolbar != null){
            initDrawerLayout();
        }
        //定位按钮防抖设计：2秒中只响应一次
        RxView.clicks(fbLocation)
                .throttleFirst(2,TimeUnit.SECONDS)
                .compose(this.<Void>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe((Void aVoid)->location());
    }

    /**
     * 初始化抽屉式菜单布局
     */
    void initDrawerLayout(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        if(null == drawerLayout)
            return;
        //创建返回键，并实现打开关/闭监听
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, super.toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        initNavigationView();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * 初始化侧边导航栏
     */
    void initNavigationView(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        if(null == navigationView)
            return;
        navigationView.setItemIconTintList(null);
        CircleImageView ivHeadImg = (CircleImageView) findViewById(R.id.ivHeadImg_drawer);
        if(ivHeadImg != null){
            ivHeadImg.setOnClickListener(this);
        }
        navigationView.setNavigationItemSelectedListener((MenuItem menuItem)-> {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_email:
                        //关闭侧边菜单
                        if(null != drawerLayout)
                            drawerLayout.closeDrawer(GravityCompat.START);
                        Util.Toast.show(WelcomeActivity.this,getString(R.string.my_email));
                        break;
                    case R.id.navigation_author:
                        if(null != drawerLayout)
                            drawerLayout.closeDrawer(GravityCompat.START);
                        Util.Toast.show(WelcomeActivity.this,getString(R.string.about_author));
                        break;
                    case R.id.navigation_theme:
                        startActivity(ThemeActivity.class);
                        break;
                }
                return true;
        });
    }

    /**
     * 定位
     */
    private void location() {
        LocationUtil.getInstance().location(this,this);
    }

    @Override
    public void onLocationChanged(boolean isSuccess, AMapLocation aMapLocation) {
        if (isSuccess) {
            tvLocation.setText(StringUtil.safeText(getString(R.string.currentLocation),aMapLocation.getAddress()));
            Snackbar.make(findViewById(R.id.activity_welcome), "定位成功："+aMapLocation.getAddress(), Snackbar.LENGTH_LONG).show();
        } else {
            tvLocation.setText(StringUtil.safeText(getString(R.string.currentLocation),"定位失败"));
            Snackbar.make(findViewById(R.id.activity_welcome), "定位失败", Snackbar.LENGTH_LONG)
                    .setAction("重新定位",(View v)->location()).show();
        }
    }

    void testRx(){
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnUnsubscribe(()-> Util.Log.i("WelcomeActivity", "退订"))
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe((Long num) -> Util.Log.i("WelcomeActivity", "订阅: " + num));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
