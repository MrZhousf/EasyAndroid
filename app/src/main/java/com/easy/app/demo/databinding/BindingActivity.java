package com.easy.app.demo.databinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.easy.R;

public class BindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        ViewModelHelper.getInfo().setTitle("modify");
        ViewModelHelper.getInfo().setContent("1111111111");
    }
}
