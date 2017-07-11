package com.easyandroid.core.base;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author : zhousf
 */

public class ResultsDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // 转换Json的数据, 获取内部有用的信息
        Log.d("HttpLog","Response:"+json.toString());
        JsonElement results = json.getAsJsonObject().get("HeWeather data service 3.0")
                .getAsJsonArray().get(0);
        return new Gson().fromJson(results, typeOfT);
    }



}
