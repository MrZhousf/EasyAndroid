package com.easyandroid.demo.camera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 相机聚焦框
 */
public class CameraFocusView extends View {

    private Paint paint;
    //聚焦四边的短线长度
    private static final int shortLineLength = 10;

    public CameraFocusView(Context context) {
        super(context);
        commonInit();
    }

    public CameraFocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public CameraFocusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        commonInit();
    }

    private void commonInit() {
        paint = new Paint();
        //聚焦框颜色
        paint.setColor(Color.TRANSPARENT);
        //聚焦框线宽
        paint.setStrokeWidth(4.0f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int shortX = width / 2;
        int shortY = height / 2;
        //绘制聚焦框
        canvas.drawLine(0.0f, 0.0f, width, 0.0f, paint);
        canvas.drawLine(width, 0.0f, width, height, paint);
        canvas.drawLine(width, height, 0.0f, height, paint); 
        canvas.drawLine(0.0f, height, 0.0f, 0.0f, paint);
        //绘制聚焦框四边的短线
        canvas.drawLine(shortX, 0.0f, shortX, shortLineLength, paint);
        canvas.drawLine(shortX, height, shortX, height - shortLineLength, paint);
        canvas.drawLine(0.0f, shortY, shortLineLength, shortY, paint);
        canvas.drawLine(width, shortY, width - shortLineLength, shortY, paint);
    }

}
