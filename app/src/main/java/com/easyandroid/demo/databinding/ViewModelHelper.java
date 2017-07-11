package com.easyandroid.demo.databinding;

/**
 * @author : zhousf
 * @description :
 * @date : 2017/4/11.
 */

public class ViewModelHelper {


    private static Info info;


    public static Info getInfo(){
        if (info == null) {
            info = new Info();
        }
        return info;
    }


}
