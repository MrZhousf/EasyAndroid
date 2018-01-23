package com.easy.app.demo.camera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.easy.R;
import com.easy.app.core.base.BaseActivity;
import com.easy.app.core.plugin.title.TitleBar;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class TestCameraActivity extends BaseActivity<TitleBar> {

    @Bind(R.id.image)
    ImageView image;

    private Bitmap bitmap;

    @Override
    protected Object initLayout() {
        return R.layout.activity_test_camera;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.title = "相机";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivityForResult(CameraActivity.class, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            //不对图进行压缩
            options.inSampleSize = 1;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(data.getStringExtra("filePath"), options);
            image.setImageBitmap(bitmap);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//如果是4.4及以上版本
                    Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(new File(data.getStringExtra("filePath"))); //out is your output file
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);
                } else {
                    sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

}
