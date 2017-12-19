package com.easyandroid.demo.car;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : zhousf
 * Description :自定义ImageView：触摸回调、数据封装
 * AppCompatImageView added in version com.android.support:appcompat-v7:25.1.0
 * https://developer.android.google.cn/reference/android/support/v7/widget/AppCompatImageView.html
 * Date : 2017/12/15.
 */
public class CarImageView extends AppCompatImageView{

    public interface CarIVTouch{

        /**
         * 显示提示框回调
         * @param centerX 触摸控件的中心点X
         * @param centerY 触摸控件的中心点Y
         * @param carImageView 控件
         */
        void onShowTip(float centerX,float centerY,CarImageView carImageView);
    }

    private Drawable originalDrawable;
    private CarIVTouch carIVTouch;
    private Paint paint;
    private int textSize = sp2px(14);
    private String text_msg = "";
    private int statusBarHeight;

    public CarImageView(Context context) {
        super(context);
        init();
    }

    public CarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        statusBarHeight = getStatusBarHeight();
        originalDrawable = getDrawable();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //图片中绘制文本
//        float textWidth = paint.measureText(text_msg);
//        canvas.drawText(text_msg, getWidth()/2 - textWidth / 2, getHeight()/2 + textSize/2, paint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //计算触摸控件的中心点
                if(carIVTouch != null){
                    float centerX = event.getRawX() - event.getX() + getWidth()/2;
                    float centerY = event.getRawY() - event.getY() + getHeight()/2 - statusBarHeight;
                    carIVTouch.onShowTip(centerX,centerY,this);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    //获取状态栏高度
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //设置控件填充的颜色
    public void setColor(int color){
        Drawable drawable = getDrawable();
        if(drawable != null){
            final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
            setImageDrawable(wrappedDrawable);
        }
    }

    //还原控件填充色
    public void recoveryColor(){
        final Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
        //colorStateList为空时则还原填充色
        DrawableCompat.setTintList(wrappedDrawable, null);
        setImageDrawable(wrappedDrawable);
    }

    //设置提示文本的字体大小
    public void setTextSize(float spValue){
        textSize = sp2px(spValue);
    }

    //设置提示文本
    public void setText(String text){
        text_msg = text==null ? "" : text;
    }

    public String getText(){
        return text_msg==null ? "" : text_msg;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setCarIVTouch(CarIVTouch carIVTouch) {
        this.carIVTouch = carIVTouch;
    }

    private int sp2px(float spValue){
        float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }

    public CarImageView addCarInfo(String itemName, String price, int selectedColor){
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(new CarInfo(itemName,price,selectedColor));
        return this;
    }

    private List<CarInfo> list;

    public List<CarInfo> getList() {
        return list;
    }

    public void setList(List<CarInfo> list) {
        this.list = list;
    }

    static class CarInfo{
        String itemName;
        int selectedColor;
        String price;

        private CarInfo(String itemName, String price, int selectedColor) {
            this.itemName = itemName;
            this.selectedColor = selectedColor;
            this.price = price;
        }
    }



}
