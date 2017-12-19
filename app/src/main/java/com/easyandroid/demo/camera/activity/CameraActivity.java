package com.easyandroid.demo.camera.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.easyandroid.R;
import com.easyandroid.demo.camera.view.CameraHelper;
import com.easyandroid.demo.camera.view.MaskSurfaceView;

public class CameraActivity extends Activity implements CameraHelper.CaptureCallback, View.OnClickListener {

	private MaskSurfaceView surfaceView;

	private ImageView btnToggleCamera;
	private ImageView btnTakePicture;
	private View focusView;
	//是否显示红色裁剪框
	private boolean showRedBorderLine = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		btnToggleCamera = (ImageView) findViewById(R.id.bnToggleCamera);
		btnTakePicture = (ImageView) findViewById(R.id.bnCapture);
		focusView = findViewById(R.id.viewFocuse);
		surfaceView = (MaskSurfaceView) findViewById(R.id.surface_view);
		initView();
		CameraHelper.getInstance().setFlashlight(CameraHelper.Flashlight.OFF);
	}

	private void initView() {
		Display display = getWindowManager().getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();
		btnToggleCamera.measure(w, h);
		btnTakePicture.measure(w, h);
		int btnTakePictureWidth = btnTakePicture.getMeasuredWidth();
		// 设置矩形区域大小
		if(showRedBorderLine){
			surfaceView.setMaskSize(2 * (h - 5 * btnTakePictureWidth / 2) / 3, h - 5 * btnTakePictureWidth / 2);
		}else{
			//全屏拍摄
			surfaceView.setMaskSize(w,h);
		}
		btnToggleCamera.setOnClickListener(this);
		btnTakePicture.setOnClickListener(this);
		focusView.setOnClickListener(this);
	}

	@Override
	public void onCapture(boolean success, String filePath) {
		if (success) {
            Intent it = new Intent();
            it.putExtra("filePath", filePath);
			setResult(RESULT_OK, it);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bnToggleCamera:
			switchCamera();
			break;
		case R.id.bnCapture:
			takePicture();
			break;
		case R.id.viewFocuse:
			focus();
			break;
		}
	}

	private void takePicture() {
		btnTakePicture.setEnabled(false);
		CameraHelper.getInstance().tackPicture(this);
	}

	private void switchCamera() {
		CameraHelper.getInstance().switchCamera();
	}

	private void focus() {
		CameraHelper.getInstance().focus(focusView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		btnTakePicture.setEnabled(true);
	}
}
