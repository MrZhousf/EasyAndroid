package com.easy.lib.plugin.common;

import android.content.Context;
import android.os.Environment;

import com.easy.lib.plugin.CorePlugin;
import com.easy.lib.util.Util;
import com.easydblib.helper.DBHelper;

public class DBPlugin extends CorePlugin {

    @Override
    public boolean initPlugin(Context context) {
        DBHelper.builder()
                .setDbPath(Environment.getExternalStorageDirectory() + "/easy_db")//数据库保存路径
                .setDbName("easy")//数据库名称
                .setDbVersion(1)//数据库版本号
                .showDBLog(true)//显示数据库操作日志
                .setLogTAG("EASY_DB")//日志显示标识
                .build(context);
        Util.Log.d(TAG,"数据库初始化成功");
        return true;
    }

}
