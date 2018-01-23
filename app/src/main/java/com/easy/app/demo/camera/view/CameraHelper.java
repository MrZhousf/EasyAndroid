package com.easy.app.demo.camera.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.easy.app.core.base.BaseApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraHelper {

	private final String TAG = getClass().getSimpleName();
	private ToneGenerator tone;
	private String filePath = "camera_capture";//保存拍照照片文件夹
	private boolean isPreviewing;

	private static CameraHelper helper;
	private Camera camera;
	private MaskSurfaceView surfaceView;

	// 分辨率
	private Size resolution;

	// 照片质量
	private int picQuality = 100;

	// 照片尺寸
	private Size pictureSize;

	// 闪光灯模式(default：自动)
	private String flashlightStatus = Parameters.FLASH_MODE_ON;

	private int currentCameraId = 0;

	private int frontCameraId;

	private SurfaceHolder holder;

	private int format, width, height, screenWidth, screenHeight;

	public enum Flashlight {
		AUTO, ON, OFF
	}

	private CameraHelper() {
		frontCameraId = findFrontFacingCamera();
	}

	public static synchronized CameraHelper getInstance() {
		if (null == helper) {
			synchronized (CameraHelper.class){
				if(null == helper){
					helper = new CameraHelper();
				}
			}
		}
		return helper;
	}

	/**
	 * 设置照片质量
	 */
	public CameraHelper setPicQuality(int picQuality) {
		this.picQuality = picQuality;
		return helper;
	}

	/**
	 * 设置闪光灯模式
	 */
	public CameraHelper setFlashlight(Flashlight status) {
		switch (status) {
		case AUTO:
			this.flashlightStatus = Parameters.FLASH_MODE_AUTO;
			break;
		case ON:
			this.flashlightStatus = Parameters.FLASH_MODE_ON;
			break;
		case OFF:
			this.flashlightStatus = Parameters.FLASH_MODE_OFF;
			break;
		default:
			this.flashlightStatus = Parameters.FLASH_MODE_AUTO;
		}
		return helper;
	}

	/**
	 * 设置文件保存路径
	 */
	public CameraHelper setPictureSaveDictionaryPath(String path) {
		this.filePath = path;
		return helper;
	}

	public CameraHelper setMaskSurfaceView(MaskSurfaceView surfaceView) {
		this.surfaceView = surfaceView;
		return helper;
	}

	/**
	 * 打开相机并开启预览
	 *
	 * @param holder
	 *            SurfaceHolder
	 * @param format
	 *            图片格式
	 * @param width
	 *            SurfaceView宽度
	 * @param height
	 *            SurfaceView高度
	 * @param screenWidth
	 *            屏幕宽度
	 * @param screenHeight
	 *            屏幕高度
	 */
	public void openCamera(SurfaceHolder holder, int format, int width, int height, int screenWidth, int screenHeight) {
		this.holder = holder;
		this.format = format;
		this.width = width;
		this.height = height;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		openCamera();
	}

	private void openCamera() {
		if (this.camera != null) {
			this.camera.release();
		}
		try {
			this.camera = Camera.open(currentCameraId);
			this.initParameters(holder, format, width, height, screenWidth, screenHeight);
			this.startPreview();
		} catch (Exception e){
			Toast.makeText(BaseApplication.get(), "打开摄像头失败", Toast.LENGTH_SHORT).show();
		}
	}

	public interface CaptureCallback {
		public void onCapture(boolean success, String filePath);
	}

	/**
	 * 照相
	 */
	public void tackPicture(final CaptureCallback callback) {
		//先进行聚焦
		this.camera.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean flag, Camera camera) {
				//拍照：shutter为空时则不发出系统咔嚓的声音
//				camera.takePicture(new ShutterCallback() {
//					@Override
//					public void onShutter() {
//						// 发出拍照的咔嚓声响
//						if (tone == null) {
//							tone = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
//						}
//						tone.startTone(ToneGenerator.TONE_PROP_BEEP);
//					}
//				}, null, new PictureCallback() {
//					@Override
//					public void onPictureTaken(byte[] data, Camera camera) {
//						//获取照片并保存
//						String filepath = savePicture(data);
//						boolean success = false;
//						if (filepath != null) {
//							success = true;
//						}
//						stopPreview();
//						callback.onCapture(success, filepath);
//					}
//				});
				camera.takePicture(null, null, new PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						//获取照片并保存
						String filepath = savePicture(data);
						boolean success = false;
						if (filepath != null) {
							success = true;
						}
						stopPreview();
						callback.onCapture(success, filepath);
					}
				});
			}
		});
	}

	/**
	 * 裁剪并保存照片
	 */
	private String savePicture(byte[] data) {
		File imgFileDir = getImageDir();
		if (!imgFileDir.exists() ) {
			imgFileDir.mkdirs();
		}
		String imgFilePath = imgFileDir.getPath() + File.separator + this.generateFileName();
		Bitmap b = this.cutImage(data);
		File imgFile = new File(imgFilePath);
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(imgFile);
			bos = new BufferedOutputStream(fos);
			b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (Exception error) {
			error.printStackTrace();
			return null;
		} finally {
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
				}
				if (bos != null) {
					bos.flush();
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.d(TAG,"保存图片路径："+imgFilePath);
		return imgFilePath;
	}

	/**
	 * 生成图片名称
	 */
	private String generateFileName() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());
		String strDate = dateFormat.format(new Date());
		return "img_" + strDate + ".jpg";
	}

	private File getImageDir() {
		String path = Environment.getExternalStorageDirectory().getPath() + "/"+filePath+"/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		return file;
	}

	/**
	 * 初始化相机参数
	 * @param holder SurfaceHolder
	 * @param format 图片格式
	 * @param width  SurfaceView宽度
	 * @param height SurfaceView高度
	 * @param screenWidth  屏幕宽度
	 * @param screenHeight 屏幕高度
	 */
	private void initParameters(SurfaceHolder holder, int format, int width, int height, int screenWidth, int screenHeight) {
		try {
			Parameters p = this.camera.getParameters();
			this.camera.setPreviewDisplay(holder);
			// 照片质量
			p.set("jpeg-quality", picQuality);
			// 设置照片格式
			p.setPictureFormat(PixelFormat.JPEG);
			// 设置闪光灯
			p.setFlashMode(this.flashlightStatus);
			// 设置最佳预览尺寸
			List<Size> previewSizes = p.getSupportedPreviewSizes();
			if (width > height) {
				// 横屏
				this.camera.setDisplayOrientation(0);
				Log.d(TAG, "SurfaceView: " + width + " × " + height + " display 0 ");
				// 设置预览分辨率
				this.resolution = this.getOptimalPreviewSize(previewSizes, width, height);
			} else {
				// 竖屏
				this.camera.setDisplayOrientation(90);
				Log.d(TAG, "SurfaceView: " + width + " × " + height + " display 90 ");
				this.resolution = this.getOptimalPreviewSize(previewSizes, height, width);
			}
			try {
				p.setPreviewSize(resolution.width, resolution.height);
				Log.d(TAG, "使用的相机预览分辨率: " + this.resolution.width + " × " + this.resolution.height);
			} catch (Exception e) {
				Log.d(TAG, "不支持的相机预览分辨率: " + this.resolution.width + " × " + this.resolution.height);
			}
			// 设置照片尺寸
			List<Size> pictureSizes = p.getSupportedPictureSizes();
			this.setPictureSize(pictureSizes, this.resolution.width, this.resolution.height);
			try {
				p.setPictureSize(pictureSize.width, pictureSize.height);
				Log.d(TAG, "使用的照片尺寸: " + this.pictureSize.width + " × " + this.pictureSize.height);
			} catch (Exception e) {
				Log.d(TAG, "不支持的照片尺寸: " + this.pictureSize.width + " × " + this.pictureSize.height);
			}
			this.camera.setParameters(p);
		} catch (Exception e) {
			Log.d(TAG, "相机参数设置错误 e " + e.toString());
		}
	}



	/**
	 * 释放Camera
	 */
	public void releaseCamera() {
		if (this.camera != null) {
			if (this.isPreviewing) {
				this.stopPreview();
			}
			this.camera.setPreviewCallback(null);
			isPreviewing = false;
			this.camera.release();
			this.camera = null;
		}
	}

	/**
	 * 停止预览
	 */
	private void stopPreview() {
		if (this.camera != null && this.isPreviewing) {
			this.camera.stopPreview();
			this.isPreviewing = false;
		}
	}

	/**
	 * 开始预览
	 */
	public void startPreview() {
		if (this.camera != null) {
			this.camera.startPreview();
			this.isPreviewing = true;
		}
	}

	/**
	 * 裁剪照片
	 */
	private Bitmap cutImage(byte[] data) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		if (this.surfaceView.getWidth() < this.surfaceView.getHeight()) {
			// 竖屏旋转照片
			Matrix matrix = new Matrix();
			matrix.reset();
			if (currentCameraId == 0){
				matrix.setRotate(90);
			} else {
				matrix.setRotate(-90);
				matrix.postScale(-1, 1);
			}
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}
		if (this.surfaceView == null) {
			return bitmap;
		} else {
			int[] sizes = this.surfaceView.getMaskSize();
			if (sizes[0] == 0 || sizes[1] == 0) {
				return bitmap;
			}
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			//按比例去裁剪图片
			int a = Math.round((float) sizes[0] / (float) screenWidth * w);
			int b = Math.round((float) (sizes[1] / (float) screenHeight) * h);

			int x = (w - a) / 2;
			int y = (h - b) / 2;

			return Bitmap.createBitmap(bitmap, x, y, a, b);
		}
	}

	/**
	 * 获取最佳预览尺寸
	 */
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null) return null;
		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		int targetHeight = h;
		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}
		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	/**
	 * 设置照片尺寸为最接近屏幕尺寸
	 */
	private void setPictureSize(List<Size> list, int screenWidth, int screenHeight) {
		int approach = Integer.MAX_VALUE;
		for (Size size : list) {
			Log.d(TAG, "照片支持的尺寸: " + size.width + " × " + size.height);
			int temp = Math.abs(size.width - screenWidth + size.height - screenHeight);
			if (approach > temp) {
				approach = temp;
				this.pictureSize = size;
			}
		}
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.CameraInfo info = new Camera.CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}

	public void switchCamera() {
		if (currentCameraId == 0) {
			currentCameraId = frontCameraId;
		} else {
			currentCameraId = 0;
		}
		releaseCamera();
		openCamera();
	}

	private Parameters parameters;

	private AutoFocusCallback focusCallback = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success){
				camera.cancelAutoFocus();
				parameters = camera.getParameters();
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				camera.setParameters(parameters);
				camera.autoFocus(autoFocusCallback);
			}
		}
	};

	public void focus(View focusView) {
		if (null == camera) {
			return;
		}
		camera.cancelAutoFocus();
		try {
			camera.autoFocus(focusCallback);
		} catch (Exception e) {
			return;
		}
		if (View.INVISIBLE == focusView.getVisibility()) {
			focusView.setVisibility(View.VISIBLE);
		}
	}

	private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				camera.cancelAutoFocus();
				if (!Build.MODEL.equals("KORIDY H30")) {
					parameters = camera.getParameters();
					parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
					camera.setParameters(parameters);
				}else{
					parameters = camera.getParameters();
					parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
					camera.setParameters(parameters);
				}
			}
		}
	};

}