package com.easyandroid.demo.weather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.easyandroid.R;
import com.easyandroid.core.base.ApiSubscriber;
import com.easyandroid.core.base.BaseRefreshFragment;
import com.easyandroid.core.base.Repository;
import com.easyandroid.core.util.LocationUtil;
import com.easyandroid.core.util.LogUtil;
import com.easyandroid.core.util.SPUtil;
import com.easyandroid.core.util.StringUtil;
import com.easyandroid.core.view.LoadingDialog;
import com.easyandroid.core.view.swiperefreshlayout.SwipeRefreshLayoutDirection;
import com.easyandroid.demo.weather.adapter.WeatherAdapter;
import com.easyandroid.demo.weather.bean.Weather;
import com.google.gson.Gson;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.Callback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 当前城市
 */
public class CurrentCityFragment extends BaseRefreshFragment implements LocationUtil.LocationListener{

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private WeatherAdapter adapter;
    private Weather weather = new Weather();
    private LoadingDialog loadingDialog;

    private String currentCity;

    @Override
    protected int initLayout() {
        return R.layout.fragment_current_city;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initIcon();
        loadingDialog = new LoadingDialog(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        //定位
        LocationUtil.getInstance().location(getActivity(),this);
        mSwipeRefreshLayout.setDirection(SwipeRefreshLayoutDirection.TOP);
    }

    void initView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WeatherAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRefreshTop() {
//        http(currentCity);
        doHttp(currentCity);
        loadingDialog.show();
    }

    @Override
    protected void onRefreshBottom() {
        http(currentCity);
    }

    @Override
    public void onLocationChanged(boolean isSuccess, AMapLocation aMapLocation) {
        if(isSuccess){
            currentCity = replaceCity(aMapLocation.getCity());
        }else{
            currentCity = "上海";
            toast("定位失败！默认显示"+currentCity);
        }
        onRefreshTop();
    }

    /**
     * 网络请求
     */
    void doHttp(String city){
        HttpInfo info = new HttpInfo.Builder()
                .setUrl("https://api.heweather.com/x3/weather")
                .addParam("key","282f3846df6b41178e4a2218ae083ea7")
                .addParam("city",city)
                .build();
        OkHttpUtil.getDefault(this).doGetAsync(info, new Callback() {
            @Override
            public void onSuccess(HttpInfo info) throws IOException {
                stopRefresh();
                loadingDialog.dismiss();
                try {
                    Gson gson = new Gson();
                    JSONObject jo = new JSONObject(info.getRetDetail());
                    JSONArray array = jo.optJSONArray("HeWeather data service 3.0");
                    if(null != array && array.length() > 0){
                        weather = gson.fromJson(array.get(0).toString(),Weather.class);
                        String status = weather.status;
                        if("ok".equals(status)){
                            adapter.clear();
                            List<Weather> list = new ArrayList<Weather>();
                            List<Weather.DailyForecastEntity> l = weather.dailyForecast;
                            l.addAll(l);
                            for (int i = 0; i < 10; i++) {
                                list.add(weather);
                            }
                            adapter.addAll(list);
//                            adapter.addItem(weather);
                        }else{
                            toast("获取("+info.getParams().get("city")+")天气失败："+status);
                        }
                    }
                }catch (Exception e){
                    toast("解析数据失败");
                    LogUtil.e("CurrentCityFragment","解析数据失败"+e.getMessage());
                }
            }

            @Override
            public void onFailure(HttpInfo info) throws IOException {
                stopRefresh();
                loadingDialog.dismiss();
                toast("获取("+info.getParams().get("city")+")天气失败："+info.getRetDetail());
            }
        });

    }

    void http(String city){
        Repository.getPublicApi()
                .getWeather(city)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriber<Weather>() {
                    @Override
                    public void onNext(Weather weather) {
                        String status = weather.status;
                        if("ok".equals(status)){
                            adapter.clear();
                            adapter.addItem(weather);
                        }else{
                            toast("获取("+city+")天气失败："+status);
                        }
                        stopRefresh();
                    }
                });



    }


    void toast(String msg){
        Snackbar.make(view.findViewById(R.id.llContent), msg, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 初始化天气图标映射关系
     */
    void initIcon(){
        SPUtil.getInstance().put("未知", R.mipmap.type_two_sunny);
        SPUtil.getInstance().put("晴", R.mipmap.type_two_sunny);
        SPUtil.getInstance().put("阴", R.mipmap.type_two_cloudy);
        SPUtil.getInstance().put("多云", R.mipmap.type_two_cloudy);
        SPUtil.getInstance().put("少云", R.mipmap.type_two_cloudy);
        SPUtil.getInstance().put("晴间多云", R.mipmap.type_two_cloudytosunny);
        SPUtil.getInstance().put("小雨", R.mipmap.type_two_light_rain);
        SPUtil.getInstance().put("中雨", R.mipmap.type_two_heavy_rain);
        SPUtil.getInstance().put("大雨", R.mipmap.type_two_heavy_rain);
        SPUtil.getInstance().put("阵雨", R.mipmap.type_two_thunder_rain);
        SPUtil.getInstance().put("雷阵雨", R.mipmap.type_two_thunderstorm);
        SPUtil.getInstance().put("霾", R.mipmap.type_two_haze);
        SPUtil.getInstance().put("雾", R.mipmap.type_two_fog);
        SPUtil.getInstance().put("雨夹雪", R.mipmap.type_two_light_rain);
    }

    /**
     * 城市名称处理
     */
    String replaceCity(String city) {
        city = StringUtil.safeText(city).replaceAll("(?:省|市|自治区|特别行政区|地区|盟)", "");
        return city;
    }



}
