package com.easy.app.core.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author : zhousf
 */

public class GSonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final TypeAdapter<T> adapter;
    private final Gson gson;

    public GSonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        Log.d("HttpLog","Response:"+response);
        JSONArray service = null;
        try {
            //服务器返回数据格式过滤
            JSONObject jsonObject = new JSONObject(response);
            service = jsonObject.optJSONArray("HeWeather data service 3.0");
        } catch (Exception e){
            throw new ApiException(200, "服务器返回错误");
        }
        if(service!=null && service.length()>0){
            response = service.opt(0).toString();
        }else{
            throw new ApiException(200, "服务器返回错误");
        }
        return adapter.fromJson(response);
    }
}
