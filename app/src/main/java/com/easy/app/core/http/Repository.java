package com.easy.app.core.http;

import android.os.Build;
import android.util.Log;
import android.util.SparseArray;

import com.easy.BuildConfig;
import com.easy.app.api.PublicApi;
import com.easy.app.demo.weather.bean.Weather;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * @author : zhousf
 */
public class Repository {

    private static File cacheDir;

    private Retrofit retrofit;

    private OkHttpClient httpClient;

    private static SparseArray<Repository> repositories = new SparseArray<>();

    public static void init(File dir){
        cacheDir = dir;
        //初始化域名仓库
        HostName.init();
    }

    private static Repository get(int hostName){
        Repository repository = repositories.get(hostName);
        if(null == repository){
            repository = new Repository(hostName);
            repositories.put(hostName,repository);
        }
        return repository;
    }

    /**
     * 公共Api
     */
    public static PublicApi getPublicApi(){
        return get(HostName.PublicHost).create(PublicApi.class);
    }

    public <T> T create(final Class<T> service){
        return retrofit.create(service);
    }


    private Repository(int hostName) {
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        retrofit = new Retrofit.Builder()
                .baseUrl(HostName.get(hostName))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
//                .addConverterFactory(getGSonConverterFactory(hostName))
                .addConverterFactory(PublicConverterFactory.create(gsonBuilder.create()))
                .client(getHttpClient())
                .build();
    }

    private GsonConverterFactory getGSonConverterFactory(int hostName){
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        if(hostName == HostName.PublicHost){
            gsonBuilder.registerTypeAdapter(Weather.class,new ResultsDeserializer<Weather>());
        }
        return GsonConverterFactory.create(gsonBuilder.create());
    }


    private OkHttpClient getHttpClient(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Arrays.asList(Protocol.SPDY_3, Protocol.HTTP_1_1));
        if(BuildConfig.DEBUG){
            httpClientBuilder.addInterceptor(LOG_INTERCEPTOR);
        }
        httpClient = httpClientBuilder.build();
        return httpClient;
    }

    /**
     * Http日志拦截器
     */
    private Interceptor LOG_INTERCEPTOR = new Interceptor() {

        long startTime;

        @Override
        public Response intercept(Chain chain) throws IOException {
            startTime = System.nanoTime();
            showLog(String.format("%s-URL: %s %n",chain.request().method(),
                    chain.request().url()));
            Response res = chain.proceed(chain.request());
            long endTime = System.nanoTime();
            showLog(String.format(Locale.getDefault(),"CostTime: %.3fs",(endTime-startTime)/1e9d));
            return res;
        }

        void showLog(String msg){
            if(BuildConfig.DEBUG)
                Log.d("HttpLog["+startTime+"]", msg);
        }
    };


    private Interceptor HEAD_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (Build.VERSION.SDK_INT > 13) {
                request.newBuilder().addHeader("Connection", "close");
            }
            return chain.proceed(request);
        }
    };


}
