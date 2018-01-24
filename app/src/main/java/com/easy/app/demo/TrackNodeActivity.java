package com.easy.app.demo;

import android.os.Bundle;
import android.view.View;

import com.easy.R;
import com.easy.app.core.base.BaseActivity;
import com.easy.app.core.plugin.title.TitleBar;

public class TrackNodeActivity extends BaseActivity<TitleBar> {

    @Override
    protected Object initLayout() {
        return R.layout.activity_track_node;
    }

    @Override
    protected void initTitle(TitleBar titleBar) {
        titleBar.title = "级联关闭Activity示例";
        titleBar.rightBtnText = "跳转";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //打开跟踪节点
        trackNodeOpen();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_right_btn:
                Bundle bundle = new Bundle();
                bundle.putString("content","A");
                startActivity(JumpActivity.class,bundle);
                break;
        }
    }
}
