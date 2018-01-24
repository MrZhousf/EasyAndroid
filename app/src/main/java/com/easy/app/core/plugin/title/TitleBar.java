package com.easy.app.core.plugin.title;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easy.R;
import com.easy.lib.CoreActivity;
import com.easy.lib.plugin.title.ITitleBar;

public class TitleBar implements ITitleBar {

    public boolean hasTitleBar;//是否有标题栏
    public boolean showBack;//显示返回按钮
    public boolean showTitle;//显示标题
    public boolean showCloseBtn;//显示左侧关闭按钮
    private TextView title_tv;
    private ImageView title_back_btn;
    public TextView title_right_btn;
    private TextView title_left_close_btn;
    public String title;//标题
    public String rightBtnText;//右侧按钮文字

    public TitleBar() {
        hasTitleBar = true;
        showTitle = true;
        showBack = true;
        showCloseBtn = false;
    }

    @Override
    public void updateTitle() {
        title_tv.setText(title);
        if(!TextUtils.isEmpty(rightBtnText)){
            title_right_btn.setText(rightBtnText);
            title_right_btn.setVisibility(View.VISIBLE);
        }else{
            title_right_btn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public Class bindPlugin() {
        return TitleBarPlugin.class;
    }

    @Override
    public void initView(CoreActivity context, ViewGroup parentView, View.OnClickListener onClickListener) {
        LayoutInflater.from(context).inflate(R.layout.title_bar_main, parentView, true);
        //标题
        title_tv = (TextView) parentView.findViewById(R.id.title_tv);
        if(!TextUtils.isEmpty(title)){
            title_tv.setText(title);
        }
        title_tv.setVisibility(showTitle?View.VISIBLE:View.INVISIBLE);
        //返回按钮
        title_back_btn = (ImageView) parentView.findViewById(R.id.title_back_btn);
        title_back_btn.setVisibility(showBack?View.VISIBLE:View.INVISIBLE);
        if(showBack){
            title_back_btn.setOnClickListener(view -> context.onBackPressed());
        }
        //左侧关闭按钮
        title_left_close_btn = (TextView) parentView.findViewById(R.id.title_left_close_btn);
        title_left_close_btn.setVisibility(showCloseBtn?View.VISIBLE:View.INVISIBLE);
        title_left_close_btn.setOnClickListener(view -> context.trackNodeClose());
        //右侧按钮
        title_right_btn = (TextView) parentView.findViewById(R.id.title_right_btn);
        if(onClickListener != null){
            title_right_btn.setOnClickListener(onClickListener);
        }
        if(!TextUtils.isEmpty(rightBtnText)){
            title_right_btn.setVisibility(View.VISIBLE);
            title_right_btn.setText(rightBtnText);
        }else{
            title_right_btn.setVisibility(View.INVISIBLE);
        }

    }



}
