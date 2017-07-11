package com.easyandroid.core.base;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : zhousf
 */

public class HostName {

    private static ConcurrentHashMap<Integer,String> hosts = new ConcurrentHashMap<>();

    private static int INDEX = 0;
    //公共Api
    public static int PublicHost = ++INDEX;

    public static void init(){
        hosts.put(PublicHost,"https://api.heweather.com/");
    }

    public static String get(int hostName){
        return hosts.get(hostName);
    }



}
