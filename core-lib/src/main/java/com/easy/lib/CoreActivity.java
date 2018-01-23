package com.easy.lib;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.easy.lib.plugin.PluginManager;
import com.easy.lib.util.Util;

import java.util.ArrayList;
import java.util.List;

import easyandroid.com.core.lib.R;

public class CoreActivity<T> extends FragmentActivity{

    protected final String TAG = getClass().getSimpleName();

    private List<Fragment> currentFragments;
    private Fragment currentFragment;
    protected T titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载延迟插件
        PluginManager.get().init(getApplicationContext(),true);
    }


    protected void startActivity(Class<?> cls){
        Intent intent = new Intent(this,cls);
        super.startActivity(intent);
    }

    protected void startActivity(Class<?> cls,Bundle bundle){
        Intent intent = new Intent(this,cls);
        if(null != bundle)
            intent.putExtras(bundle);
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
     * 加载Fragment
     * @param layoutId 绑定控件
     * @param fragment 目标Fragment
     * @param arguments 传值
     * @param addToBackStack 是否压栈
     */
    protected void addFragment(int layoutId, CoreFragment fragment, Bundle arguments, boolean addToBackStack){
        if(currentFragments == null){
            currentFragments = new ArrayList<>();
        }
        if(fragment.isAdded()){
            Util.Log.w(TAG,getString(R.string.core_fragment_added,fragment.getClass().getName()));
            return ;
        }
        if(!currentFragments.contains(fragment)){
            currentFragments.add(fragment);
        }
        if(arguments != null){
            fragment.setArguments(arguments);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layoutId,fragment,fragment.getClass().getName());
        if(addToBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }

    protected void addFragment(int layoutId, CoreFragment fragment, Bundle arguments){
        addFragment(layoutId,fragment,arguments,false);
    }

    /**
     * 显示Fragment
     * @param fragment 目标Fragment
     */
    @SuppressWarnings(value = "unchecked")
    protected void showFragment(CoreFragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(currentFragments != null && !currentFragments.isEmpty()){
            for(Fragment f : currentFragments){
                if(f == fragment){
                    fragment.bindTitle(titleBar);
                    transaction.show(fragment);
                }else{
                    transaction.hide(f);
                }
            }
        }
        transaction.commitAllowingStateLoss();
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
    @SuppressWarnings(value = "unchecked")
    protected void replaceFragment(int layoutId, CoreFragment fragment, Bundle arguments, boolean addToBackStack){
        if(fragment == null || fragment == currentFragment){
            return ;
        }
        currentFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(arguments != null){
            fragment.setArguments(arguments);
        }
        if(addToBackStack){
            transaction.addToBackStack(null);
        }
        fragment.bindTitle(titleBar);
        transaction.replace(layoutId,fragment,fragment.getTag());
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
        currentFragment = null;
    }


}
