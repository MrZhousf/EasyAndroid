package com.easyandroid.demo.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseActivity;
import com.easyandroid.core.util.LogUtil;
import com.easyandroid.core.util.UtilManager;
import com.easyandroid.databinding.ActivityDatabindingBinding;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

public class DataBindingActivity extends BaseActivity {

    @Bind(R.id.modifyBtn)
    Button modifyBtn;

    private ActivityDatabindingBinding binding;

    @Override
    protected int initLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding);
        binding.setInfo(ViewModelHelper.getInfo());
        return 0;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.title = "DataBinding";
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
                UtilManager.Toast.show(this,ViewModelHelper.getInfo().getTitle());
                LogUtil.d("DataBindingActivity",ViewModelHelper.getInfo().toString());
                startActivity(BindingActivity.class);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("DataBindingActivity",ViewModelHelper.getInfo().toString());
        binding.invalidateAll();
    }


}
