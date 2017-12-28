package com.easyandroid.demo.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.easyandroid.R;
import com.easyandroid.core.base.BaseActivity;
import com.easyandroid.core.base.RxBus;
import com.easyandroid.core.util.LocationUtil;
import com.easyandroid.core.util.LogUtil;
import com.easyandroid.core.util.StringUtil;
import com.easyandroid.core.util.ThemeManager;
import com.easyandroid.core.util.ToastUtil;
import com.easyandroid.core.view.CircleImageView;
import com.easyandroid.demo.camera.activity.TestCameraActivity;
import com.easyandroid.demo.car.CarActivity;
import com.easyandroid.demo.databinding.DataBindingActivity;
import com.easyandroid.demo.db.TestDBActivity;
import com.easyandroid.demo.net_speed.NetWorkActivity;
import com.easyandroid.demo.video.VideoActivity;
import com.easyandroid.demo.weather.activity.WeatherActivity;
import com.jakewharton.rxbinding.view.RxView;
import com.jy.amap.AMapAPI;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

public class WelcomeActivity extends BaseActivity implements LocationUtil.LocationListener {

    @Bind(R.id.tvLocation)
    TextView tvLocation;
    @Bind(R.id.fbLocation)
    FloatingActionButton fbLocation;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

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
//        testRx();
        testRxBus();

    }


    @OnClick({R.id.btnWeather,R.id.btnCamera,R.id.btnDB,R.id.dataBinDemo,R.id.btnNetWork,
            R.id.btnAmap,R.id.btnCustomer,R.id.btnVideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivHeadImg_drawer:
                RxBus.get().post("test",new Info("点击了头像"));
                break;
            case R.id.btnWeather:
                startActivity(WeatherActivity.class);
                break;
            case R.id.btnCamera:
                startActivity(TestCameraActivity.class);
                break;
            case R.id.btnDB:
                startActivity(TestDBActivity.class);
                break;
            case R.id.dataBinDemo:
                startActivity(DataBindingActivity.class);
                break;
            case R.id.btnNetWork:
                startActivity(NetWorkActivity.class);
                break;
            case R.id.btnAmap:
                Bundle bundle = new Bundle();
                bundle.putString("searchKey","清华大学");
                AMapAPI.toPOISearchActivity(this,bundle);
                break;
            case R.id.btnCustomer:
                startActivity(CarActivity.class);
                break;
            case R.id.btnVideo:
                startActivity(VideoActivity.class);
                break;

        }
    }


    void testRxBus() {
        RxBus.get().register("test",Info.class).doOnUnsubscribe(()-> LogUtil.i("WelcomeActivity", "退订"))
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe((Info msg)->{
            ToastUtil.show(this,msg.msg);
        });
    }


    void testRx(){
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnUnsubscribe(()-> LogUtil.i("WelcomeActivity", "退订"))
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe((Long num) -> LogUtil.i("WelcomeActivity", "订阅: " + num));
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
                //关闭侧边菜单
                if(null != drawerLayout)
                    drawerLayout.closeDrawer(GravityCompat.START);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_email:
                        ToastUtil.show(WelcomeActivity.this,getString(R.string.my_email));
                        break;
                    case R.id.navigation_author:
                        ToastUtil.show(WelcomeActivity.this,getString(R.string.about_author));
                        break;
                    case R.id.navigation_theme:
                        ThemeManager.get().setCurrentTheme(this,"theme.manager.theme.night");
                        break;
                }
                return true;
        });
    }

    static class Info{
        String msg;
        public Info(String msg){
            this.msg = msg;
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
