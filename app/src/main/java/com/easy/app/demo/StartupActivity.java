package com.easy.app.demo;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.easy.app.core.base.BaseHandler;
import com.easy.app.core.base.HomeActivity;

public class StartupActivity extends HomeActivity implements BaseHandler.CallBack{

    private final int what_jump = 1;

    @Override
    protected int initLayout() {
        return 0;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.showToolBar = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBaseHandler(this).sendEmptyMessageDelayed(what_jump,1000);
    }

    @Override
    public void handleMessage(Message msg) {
        if(msg.what == what_jump){
            start();
        }
    }

    void start(){
        Intent intent = new Intent(StartupActivity.this, WelcomeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


}
