package com.easy.lib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easy.lib.plugin.title.ITitleBar;
import com.easy.lib.util.Util;

import java.util.List;

import easyandroid.com.core.lib.R;

public abstract class CoreFragment<T> extends Fragment {

    protected final String TAG = getClass().getSimpleName();
    protected T titleBar;

    /**
     * 初始化标题栏
     */
    protected abstract void initTitle(T titleBar);

    public void bindTitle(T titleBar){
        this.titleBar = titleBar;
    }

    @Override
    public void onResume() {
        super.onResume();
        //自动更新标题栏
        if(titleBar != null){
            initTitle(titleBar);
            ((ITitleBar) titleBar).updateTitle();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void startActivity(Class<?> cls){
        Intent intent = new Intent(getActivity(),cls);
        super.startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode){
        this.startActivityForResult(cls,requestCode,null);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode,Bundle bundle){
        Intent intent = new Intent(getActivity(), cls);
        if(null != bundle)
            intent.putExtras(bundle);
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * 加载Fragment
     * @param layoutId 绑定控件
     * @param fragment 目标Fragment
     * @param arguments 传值
     * @param addToBackStack 是否压栈
     */
    protected void addFragment(int layoutId, CoreFragment fragment, Bundle arguments,boolean addToBackStack) {
        if(fragment.isAdded()){
            Util.Log.w(TAG,getString(R.string.core_fragment_added,fragment.getClass().getName()));
            return ;
        }
        if(arguments != null){
            fragment.setArguments(arguments);
        }
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layoutId, fragment);
        if(addToBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 显示Fragment
     * @param fragment 目标Fragment
     */
    protected void showFragment(CoreFragment fragment){
        FragmentManager manager = getChildFragmentManager();
        List<Fragment> list =  manager.getFragments();
        FragmentTransaction transaction = manager.beginTransaction();
        for(Fragment f : list){
            if(f == fragment){
                transaction.show(fragment);
            }else{
                transaction.hide(f);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    protected void addFragment(int layoutId, CoreFragment fragment){
        addFragment(layoutId,fragment,null,false);
    }

    protected void addFragment(int layoutId, CoreFragment fragment,Bundle arguments){
        addFragment(layoutId,fragment,arguments,false);
    }

    protected void replaceFragment(int layoutId, CoreFragment fragment, Bundle arguments){
        replaceFragment(layoutId,fragment,arguments,false);
    }

    /**
     * 绑定Fragment
     * @param layoutId 绑定控件
     * @param fragment 目标Fragment
     * @param arguments 传值
     * @param addToBackStack 是否压栈
     */
    protected void replaceFragment(int layoutId, CoreFragment fragment, Bundle arguments,boolean addToBackStack) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(arguments != null){
            fragment.setArguments(arguments);
        }
        transaction.replace(layoutId, fragment);
        if(addToBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }

}
