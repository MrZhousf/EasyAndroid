package com.easyandroid.demo.online;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.easyandroid.R;
import com.easyandroid.core.base.BaseActivity;
import com.easyandroid.core.util.LocationUtil;
import com.easyandroid.core.util.LogUtil;
import com.easyandroid.core.util.StringUtil;
import com.easyandroid.core.util.ToastUtil;

import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements LocationUtil.LocationListener {

    @Bind(R.id.tvLocation)
    TextView tvLocation;
    @Bind(R.id.webView)
    WebView webView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.title = "在线教育";
        title.showNavigation = true;
        title.showSearch = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        location();
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);

        webView.getSettings().setBlockNetworkImage(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//允许js弹出窗口
        webView.getSettings().setBuiltInZoomControls(true); // 原网页基础上缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);//支持缩放
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setBlockNetworkLoads(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);// 水平不显示
        webView.setVerticalScrollBarEnabled(false); // 垂直不显示
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        try {
            Thread.sleep(500);// 主线程暂停下，否则容易白屏，原因未知
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        webView.loadUrl("file:///android_asset/1.swf");
        webView.loadUrl("http://www.usacamp.cn/popup/bc_demo/demo.php");
        boolean is = checkFlashPlugin();
        if(is){
        }
        LogUtil.d("##","是否安装了flash："+checkFlashPlugin());
    }

    //检查机子是否安装的有Adobe Flash相关APK
    private boolean checkFlashPlugin() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> infoList = pm
                .getInstalledPackages(PackageManager.GET_SERVICES);
        for (PackageInfo info : infoList) {
            if ("com.adobe.flashplayer".equals(info.packageName)) {
                return true;
            }
        }
        return false;
    }


    void initView(){
        if(super.toolbar != null){
            initDrawerLayout();
        }
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
        navigationView.setNavigationItemSelectedListener((MenuItem menuItem)-> {
            //关闭侧边菜单
            if(null != drawerLayout)
                drawerLayout.closeDrawer(GravityCompat.START);
            switch (menuItem.getItemId()) {
                case R.id.navigation_email:
                    ToastUtil.show(MainActivity.this,getString(R.string.my_email));
                    break;
                case R.id.navigation_author:
                    ToastUtil.show(MainActivity.this,getString(R.string.about_author));
                    break;
                case R.id.navigation_file:
                    ToastUtil.show(MainActivity.this,getString(R.string.file_manager));
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
        } else {
            tvLocation.setText(StringUtil.safeText(getString(R.string.currentLocation),"定位失败"));
        }
    }

}
