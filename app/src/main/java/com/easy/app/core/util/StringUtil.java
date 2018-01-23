package com.easy.app.core.util;

import android.text.TextUtils;

public class StringUtil {


    /**
     * 安全的字符串追加
     * @param prefix 默认字段
     * @param append 追加字符串
     */
    public static String safeText(String prefix, String append) {
        if (TextUtils.isEmpty(append))
            return "";
        return TextUtils.concat(prefix, append).toString();
    }

    public static String safeText(String msg) {
        if (null == msg) {
            return "";
        }
        return safeText("", msg);
    }


}