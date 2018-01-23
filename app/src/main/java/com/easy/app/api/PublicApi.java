package com.easy.app.api;

import com.easy.app.demo.weather.bean.Weather;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 公共Api
 * @author : zhousf
 */

public interface PublicApi {

    @GET("x3/weather?key=282f3846df6b41178e4a2218ae083ea7")
    Observable<Weather> getWeather(@Query("city") String city);


}
