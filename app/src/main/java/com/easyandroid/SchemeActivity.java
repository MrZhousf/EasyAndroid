package com.easyandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SchemeActivity extends AppCompatActivity {


    //<a href="scheme://test.com:8080/test?param=HELLO">点击启动APP应用</a>


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);
        Intent intent = getIntent();
        String scheme = intent.getScheme();
        Uri uri = intent.getData();
        if (uri != null) {
            String queryString = uri.getQuery();
            Toast.makeText(this,"收到参数："+queryString,Toast.LENGTH_SHORT).show();
        }

    }
}
