package com.easy.app.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easy.R;
import com.easy.app.core.base.BaseActivity;
import com.easy.app.core.plugin.title.TitleBar;

public class JumpActivity extends BaseActivity<TitleBar> {

    TextView text;

    String content;

    @Override
    protected Object initLayout() {
        return R.layout.activity_jump;
    }

    @Override
    protected void initTitle(TitleBar titleBar) {
        content = getIntent().getStringExtra("content");
        titleBar.title = content;
        if("A".equals(content)){
            titleBar.rightBtnText = "跳转B";
        }else if("B".equals(content)){
            titleBar.rightBtnText = "跳转C";
        }else if("C".equals(content)){
            titleBar.showCloseBtn = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text = (TextView) findViewById(R.id.text);
        text.setText(content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_right_btn:
                if("A".equals(content)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("content","B");
                    startActivity(JumpActivity.class,bundle);
                }else if("B".equals(content)){
                    Bundle bundle = new Bundle();
                    bundle.putString("content","C");
                    startActivity(JumpActivity.class,bundle);
                }else if("C".equals(content)){
                    trackNodeClose();
                }
                break;
        }
    }
}
