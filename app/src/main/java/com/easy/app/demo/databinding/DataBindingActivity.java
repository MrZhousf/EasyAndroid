package com.easy.app.demo.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.easy.R;
import com.easy.app.core.base.BaseActivity;
import com.easy.app.core.plugin.title.TitleBar;
import com.easy.databinding.ActivityDatabindingBinding;
import com.easy.lib.util.Util;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

public class DataBindingActivity extends BaseActivity<TitleBar> {

    @Bind(R.id.modifyBtn)
    Button modifyBtn;

    private ActivityDatabindingBinding binding;

    @Override
    protected void initTitle(TitleBar title) {
        title.title = "DataBinding";
    }

    @Override
    protected Object initLayout() {
        bindView = LayoutInflater.from(this).inflate(R.layout.activity_databinding,null,false);
        binding = DataBindingUtil.bind(bindView);
        binding.setInfo(ViewModelHelper.getInfo());
        return bindView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelHelper.getInfo().setTitle("Databinding");
        ViewModelHelper.getInfo().setContent("数据双向绑定测试");
        ViewModelHelper.getInfo().setDate(new Date().toString());
    }


    @OnClick(R.id.modifyBtn)
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.modifyBtn:
                Util.Toast.show(this,ViewModelHelper.getInfo().getTitle());
                Util.Log.d("DataBindingActivity",ViewModelHelper.getInfo().toString());
                startActivity(BindingActivity.class);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.Log.d("DataBindingActivity",ViewModelHelper.getInfo().toString());
    }


}
