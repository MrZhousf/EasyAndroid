package com.easyandroid.core.base;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.easyandroid.R;
import com.easyandroid.core.bean.UpdateThemeEvent;
import com.easyandroid.core.util.ThemeManager;
import com.easyandroid.core.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public abstract class BaseActivity extends RxAppCompatActivity implements View.OnClickListener{

    /**
     * 初始化视图布局文件
     */
    protected abstract int initLayout();

    /**
     * 初始化标题栏
     */
    protected abstract void initTitle(TitleBar title);

    protected TitleBar titleBar;
    protected class TitleBar{
        public boolean showToolBar = true;//显示工具栏
        public boolean showBack = true;//显示返回按钮
        public boolean showSearch = true;//显示搜索图标
        public boolean showNavigation = false;//显示侧边导航
        public String title;//标题
    }

    protected Toolbar toolbar;//工具栏

    private BaseHandler baseHandler;//Handler句柄

    @Override
    public void setTheme(int resId) {
        super.setTheme(ThemeManager.get().getCurrentTheme());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTheme(UpdateThemeEvent event){
        if(!getClass().getName().equals(event.className)){
            recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int layoutId = initLayout();
        if(layoutId != 0)
            setContentView(initLayout());
        initTitle(titleBar = new TitleBar());
        ButterKnife.bind(this);
        initToolBar();
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化标题栏
     */
    void initToolBar(){
        if(titleBar.showToolBar){
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if(null != toolbar){
                //显示标题
                if(!TextUtils.isEmpty(titleBar.title)){
                    TextView title = (TextView)findViewById(R.id.tvTitle);
                    title.setText(titleBar.title);
                }
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if(null != actionBar){
                    //隐藏默认标题
                    actionBar.setDisplayShowTitleEnabled(false);
                    //显示侧边导航按钮
                    if(titleBar.showNavigation){
                        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
                        titleBar.showBack = false;
                    }
                    //显示后退按钮
                    if(titleBar.showBack){
                        actionBar.setDisplayHomeAsUpEnabled(true);
                    }
                }
            }
        }else{
            ActionBar actionBar = getSupportActionBar();
            if(null != actionBar)
                actionBar.hide();
        }
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(null != titleBar && titleBar.showToolBar){
            getMenuInflater().inflate(R.menu.main_menu,menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            if(titleBar.showSearch){
                SearchManager searchManager =
                        (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) searchItem.getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            }else{
                searchItem.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){//后退
            finish();
            return true;
        }
        switch (item.getItemId()){
            case R.id.action_settings:
                ToastUtil.show(this,getString(R.string.settings));
                break;
            case R.id.action_about:
                ToastUtil.show(this,getString(R.string.about_us));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void startActivity(Class<?> cls){
        Intent intent = new Intent(this,cls);
        super.startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode){
        this.startActivityForResult(cls,requestCode,null);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode,Bundle bundle){
        Intent intent = new Intent(this, cls);
        if(null != bundle)
            intent.putExtras(bundle);
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取通用句柄，自动释放
     */
    protected BaseHandler getBaseHandler(BaseHandler.CallBack callBack){
        if(null == baseHandler){
            baseHandler = new BaseHandler(callBack);
        }
        return baseHandler;
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        if(null != baseHandler)
            baseHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }




}
