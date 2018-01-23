package com.easy.app.core.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easy.R;

/**
 * Author : zhousf
 * Description :
 * Date : 2017/7/3.
 */
public class LoadingDialog extends Dialog {

    private ImageView iv_load_result;// 加载的结果图标显示
    private TextView tv_load;// 加载的文字展示
    private ProgressBar pb_loading;// 加载中的图片
    private final int LOAD_SUCCEED = 0x001;
    private final int LOAD_FAILED = 0x002;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LOAD_SUCCEED:
                    dismiss();
                    break;
                case LOAD_FAILED:
                    dismiss();
                    break;
                default:
                    break;
            }
        };
    };


    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commom_loading_layout);
        iv_load_result = (ImageView) findViewById(R.id.iv_load_result);
        tv_load = (TextView) findViewById(R.id.tv_load);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);

    }

    public LoadingDialog text(String text){
        if(tv_load != null && !TextUtils.isEmpty(text)){
            tv_load.setText(text);
        }
        return this;
    }

    public void succeed() {// 加载成功
        pb_loading.setVisibility(View.GONE);
        iv_load_result.setVisibility(View.VISIBLE);
        tv_load.setText("加载成功");
        mHandler.sendEmptyMessageDelayed(LOAD_SUCCEED, 1000);
    }

    public void failed() {// 加载失败
        pb_loading.setVisibility(View.GONE);
        iv_load_result.setVisibility(View.VISIBLE);
        tv_load.setText("加载失败");
        mHandler.sendEmptyMessageDelayed(LOAD_FAILED, 1000);
    }



}
