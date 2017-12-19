package com.jy.amap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author : zhousf
 * @description : 地图模块API
 * 支持：
 * 1、POI搜索页面
 * 2、基础地图页面
 * 3、导航页面
 * @date : 2017/3/30.
 */

public class AMapAPI {


    /**
     * 进入POI搜索页面
     * @param context 上下文
     * @param bundle 参数
         入参示例：
         Bundle bundle = new Bundle();
         bundle.putString("searchKey","北京海淀区中关村");//搜索关键字
     */
    public static void toPOISearchActivity(Context context, Bundle bundle){
        Intent it = new Intent(context, PoiKeywordSearchActivity.class);
        it.putExtras(bundle);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    /**
     * 进入POI搜索页面
     * @param context 上下文
     * @param bundle 参数
         入参示例：
         Bundle bundle = new Bundle();
         bundle.putString("searchKey","北京海淀区中关村");//搜索关键字
     */
    public static void toPOISearchActivity(Fragment context, Bundle bundle){
        Intent it = new Intent(context.getActivity(), PoiKeywordSearchActivity.class);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    /**
     * 进入基础地图页面
     * @param context 上下文
     * @param bundle 参数
    入参示例：
    Bundle bundle = new Bundle();
    bundle.putDouble("toLongitude",39.2123);//经度
    bundle.putDouble("toLatitude",36.4126);//纬度
     */
    public static void toAMapActivity(Context context, Bundle bundle){
        Intent it = new Intent(context, AMapActivity.class);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    /**
     * 进入基础地图页面
     * @param context 上下文
     * @param bundle 参数
         入参示例：
         Bundle bundle = new Bundle();
         bundle.putDouble("toLongitude",39.2123);//经度
         bundle.putDouble("toLatitude",36.4126);//纬度
     */
    public static void toAMapActivity(Fragment context, Bundle bundle){
        Intent it = new Intent(context.getActivity(), AMapActivity.class);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    /**
     * 进入导航页面
     * @param context 上下文
     * @param bundle 参数
    入参示例：
    Bundle bundle = new Bundle();
    bundle.putDouble("fromLongitude", 39.2123);//起始经度
    bundle.putDouble("fromLatitude", 39.1123);//起始纬度
    bundle.putDouble("toLongitude", 40.1123);//目的地经度
    bundle.putDouble("toLatitude", 39.2123);//目的地纬度
    或者：
    bundle.putDouble("toLongitude", 40.1123);//目的地经度
    bundle.putDouble("toLatitude", 39.2123);//目的地纬度
     */
    public static void toNavigationActivity(Context context, Bundle bundle){
        Intent it = new Intent(context,SingleRouteCalculateActivity.class);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    /**
     * 进入导航页面
     * @param context 上下文
     * @param bundle 参数
         入参示例：
         Bundle bundle = new Bundle();
         bundle.putDouble("fromLongitude", 39.2123);//起始经度
         bundle.putDouble("fromLatitude", 39.1123);//起始纬度
         bundle.putDouble("toLongitude", 40.1123);//目的地经度
         bundle.putDouble("toLatitude", 39.2123);//目的地纬度
         或者：
         bundle.putDouble("toLongitude", 40.1123);//目的地经度
         bundle.putDouble("toLatitude", 39.2123);//目的地纬度
     */
    public static void toNavigationActivity(Fragment context, Bundle bundle){
        Intent it = new Intent(context.getActivity(),SingleRouteCalculateActivity.class);
        it.putExtras(bundle);
        context.startActivity(it);
    }






}
