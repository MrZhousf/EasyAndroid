package com.easy.app.core.plugin.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.easy.R;
import com.easy.lib.CoreActivity;
import com.easy.lib.plugin.CorePlugin;
import com.easy.lib.plugin.IPlugin;
import com.easy.lib.plugin.title.ITitleBar;
import com.easy.lib.plugin.title.ITitleBarPlugin;
import com.easy.lib.util.Util;


public class TitleBarPlugin extends CorePlugin implements IPlugin,ITitleBarPlugin {

    private TitleBar titleBar;

    @Override
    public boolean initPlugin(Context context) {
        Util.Log.d(TAG,"基础标题栏插件初始化成功");
        return true;
    }

    @Override
    public void bindTitleBar(ITitleBar titleBar) {
        this.titleBar = (TitleBar) titleBar;
    }

    @Override
    public boolean hasTitleBar() {
        return titleBar != null && titleBar.hasTitleBar;
    }

    @Override
    public ViewGroup initTitleView(CoreActivity context, View.OnClickListener onClickListener) {
        if(hasTitleBar()){
            LinearLayout parentLinearLayout = new LinearLayout(context);
            RelativeLayout.LayoutParams parentParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
            parentLinearLayout.setLayoutParams(parentParams);
            int[] attribute = new int[] { R.attr.background_color };
            TypedArray array = context.obtainStyledAttributes(null, attribute);
            parentLinearLayout.setBackgroundColor(array.getColor(0,-1));
            array.recycle();
            titleBar.initView(context,parentLinearLayout,onClickListener);
            return parentLinearLayout;
        }
        return null;
    }





}
