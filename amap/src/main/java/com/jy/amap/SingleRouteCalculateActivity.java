package com.jy.amap;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.jy.amap.util.LocationUtil;

/**
 * @author : zhousf
 * @description : 高德单路径导航页
 * @date : 2017/3/27.
 */
public class SingleRouteCalculateActivity extends AMapBaseActivity implements LocationUtil.LocationListener {

    private static String TAG = SingleRouteCalculateActivity.class.getSimpleName();

    private double toLongitude;//目的地经度
    private double toLatitude;//目的地纬度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        double fromLongitude = getIntent().getDoubleExtra("fromLongitude",0.0);
        double fromLatitude = getIntent().getDoubleExtra("fromLatitude",0.0);
        toLongitude = getIntent().getDoubleExtra("toLongitude",0.0);
        toLatitude = getIntent().getDoubleExtra("toLatitude",0.0);
        if(fromLatitude != 0d && fromLongitude != 0d){
            setPoint(fromLongitude,fromLatitude,toLongitude,toLatitude);
        }else{
            location();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_route_calculate);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
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
            double fromLongitude = aMapLocation.getLongitude();//经度
            double fromLatitude = aMapLocation.getLatitude();//纬度
            setPoint(fromLongitude,fromLatitude,toLongitude,toLatitude);
            calculateDriveRoute();
        } else {
            Log.d(TAG,"定位失败:默认北京市");
        }
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        calculateDriveRoute();
    }

    /**
     * 计算路线
     */
    private void calculateDriveRoute(){
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            if (mAMapNavi != null) {
                strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //默认驾车导航
        if (mAMapNavi != null) {
            mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
        }
    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        startNavigation();
    }

    /**
     * 开始GPS导航
     */
    private void startNavigation(){
        if (mAMapNavi != null) {
            mAMapNavi.startNavi(NaviType.GPS);
            Log.d(TAG,"开始导航");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
    }


}
