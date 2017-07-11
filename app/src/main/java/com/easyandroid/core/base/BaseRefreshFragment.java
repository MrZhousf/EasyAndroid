package com.easyandroid.core.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyandroid.R;
import com.easyandroid.core.view.swiperefreshlayout.SwipeRefreshLayout;
import com.easyandroid.core.view.swiperefreshlayout.SwipeRefreshLayoutDirection;

import butterknife.ButterKnife;


public abstract class BaseRefreshFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 初始化视图布局文件
     */
    protected abstract int initLayout();

    /**
     * 下拉刷新
     */
    protected abstract void onRefreshTop();

    /**
     * 上拉加载
     */
    protected abstract void onRefreshBottom();

    protected View view;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            final int layoutId = initLayout();
            if(layoutId != 0){
                view = inflater.inflate(layoutId, container, false);
            }
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSwipeRefreshLayout(view);
    }

    /**
     * 初始化刷新样式
     */
    private void initSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        if(null != mSwipeRefreshLayout){
            mSwipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_blue_light,
                    android.R.color.holo_red_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
            //设置刷新方式：BOTH上拉和下拉、TOP下拉、BOTTOM上拉、NONE禁止刷新
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
