package com.easyandroid.core.util;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * @author : zhousf
 * @description : 高德定位工具类
 * @date : 2017/3/24.
 */
public class LocationUtil {

    private static final String TAG = LocationUtil.class.getSimpleName();

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;

    //最近定位信息
    private static AMapLocation lastLocation = null;

    public interface LocationListener {
        void onLocationChanged(boolean isSuccess, AMapLocation aMapLocation);
    }

    private static LocationUtil instance;

    public static LocationUtil getInstance(){
        if(null == instance){
            synchronized (LocationUtil.class){
                if(null == instance)
                    instance = new LocationUtil();
            }
        }
        return instance;
    }

    /**
     * 开始定位
     */
    public void location(Context context, final LocationListener locationListener){
        //初始化AMapLocationClientOption对象
        if(null == mLocationOption){
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
        }
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        Log.d(TAG,"定位成功:"+aMapLocation.getCity()+",longitude="+ aMapLocation.getLongitude()
                                +",latitude="+ aMapLocation.getLatitude());
                        lastLocation = aMapLocation;
                        //定位成功回调信息，设置相关消息
                        callback(locationListener,aMapLocation,true);
                    } else {
                        callback(locationListener,aMapLocation,false);
                        destroy();
                        //定位失败
                        Log.d(TAG,"location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }else{
                    callback(locationListener,null,false);
                }
            }
        });
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        Log.d(TAG,"开始定位...");
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 最近定位信息
     */
    public AMapLocation getLastLocation(Context context,LocationListener locationListener){
        if(lastLocation == null){
            location(context,locationListener);
        } else{
            Log.d(TAG,"获取最近定位信息成功:"+lastLocation.getCity()+",longitude="+ lastLocation.getLongitude()
                    +",latitude="+ lastLocation.getLatitude());
            if(null != locationListener)
                locationListener.onLocationChanged(true, lastLocation);
        }
        return lastLocation;
    }

    private void callback(LocationListener locationListener, AMapLocation aMapLocation, boolean isSuccess){
        if(null != locationListener)
            locationListener.onLocationChanged(isSuccess, aMapLocation);
    }

    private void destroy(){
        if(null != mLocationClient){
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        }
        mLocationClient = null;
        mLocationOption = null;
    }


}
