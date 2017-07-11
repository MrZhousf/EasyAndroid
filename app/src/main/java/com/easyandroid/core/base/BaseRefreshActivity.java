package com.easyandroid.core.base;


import android.os.Bundle;

import com.easyandroid.R;
import com.easyandroid.core.view.swiperefreshlayout.SwipeRefreshLayout;
import com.easyandroid.core.view.swiperefreshlayout.SwipeRefreshLayoutDirection;

public abstract class BaseRefreshActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * 下拉刷新
     */
    protected abstract void onRefreshTop();

    /**
     * 上拉加载
     */
    protected abstract void onRefreshBottom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSwipeRefreshLayout();
    }

    /**
     * 初始化刷新样式
     */
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        if(null != mSwipeRefreshLayout){
            mSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_light,
                    android.R.color.holo_red_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            mSwipeRefreshLayout.setDirection(SwipeRefreshLayoutDirection.BOTH);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
    }

    @Override
    public void onRefresh(SwipeRefreshLayoutDirection direction) {
        if(direction == SwipeRefreshLayoutDirection.TOP){
            onRefreshTop();
        }else{
            onRefreshBottom();
        }
    }


    /**
     * 停止刷新：在调用刷新方法后调用该方法结束刷新显示
     */
    protected void stopRefresh(){
        if(null != mSwipeRefreshLayout && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }


}
