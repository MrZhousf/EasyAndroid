package com.easyandroid.demo.camera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 相机矩形框遮罩层
 */
public class MaskSurfaceView extends FrameLayout {

	private MSurfaceView surfaceView;
	private MaskView maskView;
	private int width;
	private int height;
	private int maskWidth = 150;//默认矩形框的外边距
	private int maskHeight = 50;//默认矩形框的外边距
	private int screenWidth;
	private int screenHeight;

	public MaskSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		surfaceView = new MSurfaceView(context);
		maskView = new MaskView(context);
		this.addView(surfaceView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addView(maskView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		CameraHelper.getInstance().setMaskSurfaceView(this);
		maskWidth = (screenHeight - maskWidth*2);
		maskHeight = (screenWidth - maskHeight*2);
	}

	public void setMaskSize(Integer width, Integer height) {
		maskHeight = height;
		maskWidth = width;
	}

	public int[] getMaskSize() {
		return new MaskSize().size;
	}

	private class MSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
		private SurfaceHolder holder;

		public MSurfaceView(Context context) {
			super(context);
			this.holder = this.getHolder();
			// translucent半透明 transparent透明
			this.holder.setFormat(PixelFormat.TRANSPARENT);
			this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			this.holder.addCallback(this);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			width = w;
			height = h;
			CameraHelper.getInstance().openCamera(holder, format, width, height, screenWidth, screenHeight);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			CameraHelper.getInstance().releaseCamera();
		}
	}

	private class MaskSize {
		private int[] size;

		private MaskSize() {
			this.size = new int[] { maskWidth, maskHeight, width, height };
		}
	}

	private class MaskView extends View {

		private Paint linePaint;
		private Paint rectPaint;

		public MaskView(Context context) {
			super(context);

			// 绘制中间透明区域矩形边界的Paint
			linePaint = new Paint();
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeWidth(3f);
			linePaint.setColor(Color.RED);

			// 绘制四周矩形阴影区域
			rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			rectPaint.setColor(Color.GRAY);
			rectPaint.setStyle(Paint.Style.FILL);
			rectPaint.setAlpha(50);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			if (maskHeight == 0 && maskWidth == 0) {
				return;
			}
			if (maskHeight == height || maskWidth == width) {
				return;
			}

			if ((height > width && maskHeight < maskWidth) || (height < width && maskHeight > maskWidth)) {
				int temp = maskHeight;
				maskHeight = maskWidth;
				maskWidth = temp;
			}

			int h = Math.abs((height - maskHeight) / 2);
			int w = Math.abs((width - maskWidth) / 2);

			// 上
			canvas.drawRect(0, 0, width, h, this.rectPaint);
			// 右
			canvas.drawRect(width - w, h, width, height - h, this.rectPaint);
			// 下
			canvas.drawRect(0, height - h, width, height, this.rectPaint);
			// 左
			canvas.drawRect(0, h, w, h + maskHeight, this.rectPaint);

			//中间的矩形白线框
			canvas.drawRect(w, h, w + maskWidth, h + maskHeight, this.linePaint);
			super.onDraw(canvas);
		}
	}
}