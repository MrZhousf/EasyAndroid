package com.easyandroid.demo.car;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Author : zhousf
 * Description : 自定义相对布局：子控件颜色控制、显示文本信息、显示列表选项
 * Date : 2017/12/15.
 */
public class CarRelativeLayout extends RelativeLayout implements CarImageView.CarIVTouch{

    private int currentViewId, lastViewId;
    private float currentTouchX, currentTouchY;
    private SparseArray<TextView> viewSparseArray = new SparseArray<>();
    private SparseArray<LinearLayout> itemSparseArray = new SparseArray<>();
    private LinearLayout tip_item_ll;//列表选项
    final int ITEM_WIDTH = 100;//列表选项宽度dp
    final int ITEM_HEIGHT = 40;//列表选项单个高度dp
    final int OFFSET = dip2px(5);//偏移量

    public CarRelativeLayout(Context context) {
        super(context);
    }

    public CarRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init(){
        initCarIVTouch(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(tip_item_ll != null && tip_item_ll.getVisibility() == VISIBLE){
            //动态计算屏幕空间并合理显示列表位置
            float x,y;
            if(getMeasuredWidth() - (tip_item_ll.getMeasuredWidth() + currentTouchX) > OFFSET){
                //右边显示
                x = currentTouchX;
            }else{
                //左边显示
                x = currentTouchX - tip_item_ll.getMeasuredWidth();
                if(x < 0){
                    x = OFFSET;
                }
            }
            if(getMeasuredHeight() - (tip_item_ll.getMeasuredHeight() + currentTouchY) < OFFSET){
                //上边显示
                y = currentTouchY - tip_item_ll.getMeasuredHeight();
                if(y < 0){
                    y = OFFSET;
                }
            }else{
                //下边显示
                y = currentTouchY;
            }
            tip_item_ll.setX(x);
            tip_item_ll.setY(y);
        }
        //设置文本显示位置为触摸控件的中心点
        if(currentTouchX > 0 && currentTouchY > 0 && currentViewId != lastViewId){
            //比较一下防止重复走逻辑
            lastViewId = currentViewId;
            TextView textView = viewSparseArray.get(currentViewId);
            if(textView != null){
                textView.setX(currentTouchX - textView.getMeasuredWidth()/2);
                textView.setY(currentTouchY - textView.getMeasuredHeight()/2);
            }else{
                lastViewId = -1;
            }
        }
    }

    @Override
    public void onShowTip(float centerX, float centerY, CarImageView carImageView) {
        if(tip_item_ll != null){
            tip_item_ll.setVisibility(INVISIBLE);
        }
        currentViewId = carImageView.getId();
        currentTouchX = centerX;
        currentTouchY = centerY;
        showTip(carImageView);
    }

    @SuppressWarnings("ALL")
    private void showTip(final CarImageView carImageView){
        tip_item_ll = itemSparseArray.get(carImageView.getId());
        if(tip_item_ll == null){
            tip_item_ll = new LinearLayout(getContext());
            itemSparseArray.put(carImageView.getId(),tip_item_ll);
            tip_item_ll.setOrientation(LinearLayout.VERTICAL);
            tip_item_ll.setGravity(LinearLayout.VERTICAL);
            //加载列表选项
            for (final CarImageView.CarInfo info : carImageView.getList()){
                TextView tv = new TextView(getContext());
                tv.setText(info.itemName);
                tv.setTextColor(Color.WHITE);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(Color.GRAY);
                tv.setHeight(dip2px(ITEM_HEIGHT));
                //设置列表选项点击的hover效果
                tv.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                tv.setBackgroundColor(Color.DKGRAY);
                                break;
                            case MotionEvent.ACTION_UP:
                                tv.setBackgroundColor(Color.GRAY);
                                break;
                        }
                        return false;
                    }
                });
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView textView = viewSparseArray.get(currentViewId);
                        if(textView == null){
                            textView = new TextView(getContext());
                            textView.setTextColor(Color.BLACK);
                            addView(textView);
                            viewSparseArray.put(currentViewId,textView);
                        }
                        textView.setAlpha(1.0f);
                        textView.setTextSize(px2sp(carImageView.getTextSize()));
                        carImageView.setColor(info.selectedColor);
                        textView.setText(info.price);
                        tip_item_ll.setVisibility(INVISIBLE);
                    }
                });
                tip_item_ll.addView(tv);
                //白色间隔线
                View whiteLine = new View(getContext());
                whiteLine.setBackgroundColor(Color.WHITE);
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.height = 1;
                whiteLine.setLayoutParams(lp);
                tip_item_ll.addView(whiteLine);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.width = dip2px(ITEM_WIDTH);
            params.gravity = Gravity.CENTER;
            addView(tip_item_ll,params);
        }
        tip_item_ll.setVisibility(VISIBLE);
        tip_item_ll.setAlpha(0.8f);
        //设置列表选项在父类的顶层从而防止被其他控件覆盖
        tip_item_ll.bringToFront();

    }

    //递归遍历CarRelativeLayout包含的CarImageView并初始化CarImageView触摸回调
    private void initCarIVTouch(ViewGroup viewGroup){
        for(int i = 0; i < viewGroup.getChildCount(); i++){
            View view = viewGroup.getChildAt(i);
            if(view instanceof CarImageView){
                ((CarImageView) view).setCarIVTouch(this);
            }else if(view instanceof ViewGroup){
                initCarIVTouch((ViewGroup) view);
            }
        }
    }

    private int px2sp(float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
