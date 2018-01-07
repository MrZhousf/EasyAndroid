package com.easyandroid.demo.customer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.easyandroid.R;

public class CarActivity extends AppCompatActivity {

    CarImageView img_iv,img2_iv,img3_iv,img4_iv,img5_iv;
    CarRelativeLayout car_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        car_ll = (CarRelativeLayout) findViewById(R.id.car_ll);
        img_iv = (CarImageView) findViewById(R.id.img_iv);
        img2_iv = (CarImageView) findViewById(R.id.img2_iv);
        img3_iv = (CarImageView) findViewById(R.id.img3_iv);
        img4_iv = (CarImageView) findViewById(R.id.img4_iv);
        img5_iv = (CarImageView) findViewById(R.id.img5_iv);
        img_iv.addCarInfo("头1","¥550.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("头2","¥120.0",getResources().getColor(R.color.color_ffb90f))
                .addCarInfo("头3","¥390.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("头4","¥1020.0",getResources().getColor(R.color.color_9B30FF));
        img2_iv.addCarInfo("脖子1","¥10.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("脖子2","¥20.0",getResources().getColor(R.color.color_ffb90f))
                .addCarInfo("脖子3","¥30.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("脖子4","¥30.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("脖子5","¥40.0",getResources().getColor(R.color.color_9B30FF));
        img3_iv.addCarInfo("胸膛1","¥101.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("胸膛2","¥201.0",getResources().getColor(R.color.color_ffb90f))
                .addCarInfo("胸膛3","¥301.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("胸膛4","¥401.0",getResources().getColor(R.color.color_9B30FF));
        img4_iv.addCarInfo("太阳1","¥100.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("太阳2","¥400.0",getResources().getColor(R.color.color_9B30FF));
        img5_iv.addCarInfo("太阳1","¥3301.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("太阳2","¥3321.0",getResources().getColor(R.color.color_ffb90f))
                .addCarInfo("太阳3","¥3201.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("太阳4","¥4031.0",getResources().getColor(R.color.color_9B30FF));
    }


    public void changeColorBtn(View view){
        switch (view.getId()){
            case R.id.btn_recovery:
                img_iv.recoveryColor();
                break;
            case R.id.btn_green:
                img_iv.setColor(getResources().getColor(R.color.color_31c27c));
                break;
            case R.id.btn_gold:
                img_iv.setColor(getResources().getColor(R.color.color_ffb90f));
                break;
            case R.id.btn_red:
                img_iv.setColor(getResources().getColor(R.color.color_e81d62));
                break;
            case R.id.btn_purple:
                img_iv.setColor(getResources().getColor(R.color.color_9B30FF));
                break;
            default:
                img_iv.setColor(getResources().getColor(R.color.color_ffb90f));
                break;
        }
    }


}
