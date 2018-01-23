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
    public TextView title_tv;
    public ImageView back_iv;
    public TextView right_btn;
    public String title;//标题
    public String rightBtnText;//右侧按钮文字

    public TitleBar() {
        hasTitleBar = true;
        showTitle = true;
        showBack = true;
    }

    @Override
    public void updateTitle() {
        title_tv.setText(title);
        if(!TextUtils.isEmpty(rightBtnText)){
            right_btn.setText(rightBtnText);
            right_btn.setVisibility(View.VISIBLE);
        }else{
            right_btn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public Class bindPlugin() {
        return TitleBarPlugin.class;
    }

    @Override
    public void initView(CoreActivity context, ViewGroup parentView, View.OnClickListener onClickListener) {
        LayoutInflater.from(context).inflate(R.layout.title_bar_main, parentView, true);
        title_tv = (TextView) parentView.findViewById(R.id.title_tv);
        back_iv = (ImageView) parentView.findViewById(R.id.back_iv);
        right_btn = (TextView) parentView.findViewById(R.id.right_btn);
        //标题
        if(!TextUtils.isEmpty(title)){
            title_tv.setText(title);
        }
        title_tv.setVisibility(showTitle?View.VISIBLE:View.INVISIBLE);
        //返回按钮
        back_iv.setVisibility(showBack?View.VISIBLE:View.INVISIBLE);
        if(showBack){
            back_iv.setOnClickListener(view -> context.onBackPressed());
        }
        //右侧按钮
        if(onClickListener != null){
            right_btn.setOnClickListener(onClickListener);
        }
        if(!TextUtils.isEmpty(rightBtnText)){
            right_btn.setVisibility(View.VISIBLE);
        }else{
            right_btn.setVisibility(View.INVISIBLE);
        }

    }



}
