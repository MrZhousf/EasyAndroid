package com.jy.amap;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jy.amap.util.AMapUtil;
import com.jy.amap.util.LocationUtil;

import java.util.List;

/**
 * @author : zhousf
 * @description : 高德基础地图：支持长按标记、导航入口
 * @date : 2017/3/24.
 */
public class AMapActivity extends CheckPermissionsActivity
        implements LocationUtil.LocationListener,AMap.OnMarkerClickListener,AMap.InfoWindowAdapter, AMap.OnMapLongClickListener
        ,GeocodeSearch.OnGeocodeSearchListener,AMap.CancelableCallback {

    private static final String TAG = AMapActivity.class.getSimpleName();
    private MapView mapView;
    private AMap aMap;
    private LatLng toLatLng;
    private String title="";//标题
    private String snippet="";//地址
    private String currentCity = "";//当前城市
    private double fromLongitude;//当前经度
    private double fromLatitude;//当前纬度
    private double toLongitude = 0;//目的地经度
    private double toLatitude = 0;//目的地纬度
    private MyLocationStyle myLocationStyle;//初始化定位蓝点样式类
    private GeocodeSearch geoCoderSearch;//逆地址解析搜索器
    private Handler handler;
    private TextView navigation_btn;//开始导航按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        checkFrom();
    }

    /**
     * 初始化视图组件
     */
    private void initView() {
        if (aMap == null) {
            aMap = mapView.getMap();
            geoCoderSearch = new GeocodeSearch(this);
            geoCoderSearch.setOnGeocodeSearchListener(this);
            aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
            aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
            aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
            UiSettings uiSettings = aMap.getUiSettings();//实例化UiSettings类对象
            uiSettings.setCompassEnabled(true);//显示指南针
            uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        }
        navigation_btn = (TextView) findViewById(R.id.navigation_btn);
        navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNavigation();
            }
        });
        navigation_btn.setVisibility(View.INVISIBLE);
    }

    /**
     * 检查请求来源
     */
    private void checkFrom(){
        //是否来自POI搜索
        if(getIntent().getBooleanExtra("isFromPOI",false)){
            title = getIntent().getStringExtra("title");
            snippet = getIntent().getStringExtra("snippet");
            currentCity = getIntent().getStringExtra("currentCity");
            toLongitude = getIntent().getDoubleExtra("toLongitude",0d);
            toLatitude = getIntent().getDoubleExtra("toLatitude",0d);
            fromLongitude = getIntent().getDoubleExtra("fromLongitude",0d);
            fromLatitude = getIntent().getDoubleExtra("fromLatitude",0d);
            toLatLng = new LatLng(toLatitude,toLongitude);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveCamera(true,
                            CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                    toLatLng, 16f, 0f, 0f)), new AMap.CancelableCallback() {
                                @Override
                                public void onFinish() {
                                    showDestination(toLatLng,title,snippet);
                                }

                                @Override
                                public void onCancel() {
                                    Log.d(TAG,"镜头俯视缩进动画被打断");
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            moveCamera(false,
                                                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                                            toLatLng, 16f, 0f, 0f)),null);
                                            showDestination(toLatLng,title,snippet);
                                        }
                                    },200L);
                                }
                            });
                }
            },600L);
        }else{
            location();
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
            fromLongitude = aMapLocation.getLongitude();//经度
            fromLatitude = aMapLocation.getLatitude();//纬度
            currentCity = aMapLocation.getCity();
            showCurrentLocation();
        } else {
            Log.d(TAG,"定位失败:默认北京市");
            currentCity = "北京市";
        }
    }

    /**
     * 显示目的地
     */
    private void showDestination(LatLng latLng, String title, String snippet){
        //清除所有标记
        aMap.clear();
        //绘制目的地标记
        aMap.addMarker(new MarkerOptions()
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latLng))
                .showInfoWindow();
        navigation_btn.setVisibility(View.VISIBLE);
        if(latLng != null){
            toLongitude = latLng.longitude;
            toLatitude = latLng.latitude;
        }
    }

    /**
     * 调用函数animateCamera来改变可视区域
     */
    private void moveCamera(boolean showAnimate, CameraUpdate update, AMap.CancelableCallback callback) {
        if(showAnimate){
            aMap.animateCamera(update, 1200L, callback);
        }else{
            aMap.moveCamera(update);
        }
    }

    /**
     * 显示当前的位置
     */
    private void showCurrentLocation(){
        if(myLocationStyle == null){
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
            //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(10000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        }
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    /**
     * 点击标记
     * @param marker 标记
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG,"onMarkerClick title="+marker.getTitle()+",toLongitude="+toLongitude+",toLatitude="+toLatitude);
        if(marker.getPosition() != null){
            toLongitude = marker.getPosition().longitude;
            toLatitude = marker.getPosition().latitude;
        }
        return false;
    }

    /**
     * 长按地图
     * @param latLng 经纬度
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d(TAG,"onMapLongClick "+latLng);
        if (aMap != null) {
            geocodeSearch(latLng);
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.map_poikeywordsearch_uri,
                null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());
        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNavigation();
            }
        });
        return view;
    }

    /**
     * 跳转导航页面
     */
    private void gotoNavigation(){
        if(toLongitude == 0 && toLatitude ==0){
            Toast.makeText(this,"请先选择目的地！",Toast.LENGTH_SHORT).show();
            return ;
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("fromLongitude", fromLongitude);//起始经度
        bundle.putDouble("fromLatitude", fromLatitude);//起始纬度
        bundle.putDouble("toLongitude", toLongitude);//目的地经度
        bundle.putDouble("toLatitude", toLatitude);//目的地纬度
        AMapAPI.toNavigationActivity(this,bundle);
    }

    /**
     * 逆地址解析
     * @param latLng 经纬度
     */
    private void geocodeSearch(LatLng latLng){
        geoCoderSearch.getFromLocationAsyn(new RegeocodeQuery(AMapUtil.convertToLatLonPoint(latLng), 20, GeocodeSearch.AMAP));
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<PoiItem> poiItems = result.getRegeocodeAddress().getPois();
            if(poiItems != null && poiItems.size() > 0){
                PoiItem poiItem = poiItems.get(0);
                showDestination(AMapUtil.convertToLatLng(poiItem.getLatLonPoint()),
                        poiItem.getTitle(),
                        poiItem.getSnippet());
                StringBuilder log = new StringBuilder();
                for (PoiItem poi : poiItems){
                    log.append(poi.getTitle());
                    log.append(",");
                    log.append(poi.getSnippet());
                    log.append("\n");
                }
                Log.d(TAG,"---逆地址解析成功：");
                Log.d(TAG,""+log.toString());
                Log.d(TAG,"------------------");

            }
        }else{
            Log.d(TAG,"##逆地址解析失败："+rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(mapView != null)
            mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(mapView != null)
            mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mapView != null)
            mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mapView != null)
            mapView.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 地图动画效果完成回调方法
     */
    @Override
    public void onFinish() {
        Log.d(TAG,"onFinish动画完成");
    }

    /**
     * 地图动画效果终止回调方法
     */
    @Override
    public void onCancel() {

    }

}
