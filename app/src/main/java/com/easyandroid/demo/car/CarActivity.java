package com.easyandroid.demo.car;

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
        img_iv.addCarInfo("喷漆","¥550.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("钣金","¥120.0",getResources().getColor(R.color.color_ffb90f))
                .addCarInfo("拆钣喷","¥390.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("更换","¥1020.0",getResources().getColor(R.color.color_9B30FF));
        img2_iv.addCarInfo("喷漆","¥10.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("钣金","¥20.0",getResources().getColor(R.color.color_ffb90f))
                .addCarInfo("拆钣喷","¥30.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("拆钣喷","¥30.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("更换","¥40.0",getResources().getColor(R.color.color_9B30FF));
        img4_iv.addCarInfo("喷漆","¥100.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("更换","¥400.0",getResources().getColor(R.color.color_9B30FF));
        img3_iv.addCarInfo("喷漆","¥101.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("钣金","¥201.0",getResources().getColor(R.color.color_ffb90f))
                .addCarInfo("拆钣喷","¥301.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("更换","¥401.0",getResources().getColor(R.color.color_9B30FF));
        img5_iv.addCarInfo("喷漆","¥3301.0",getResources().getColor(R.color.color_31c27c))
                .addCarInfo("钣金","¥3321.0",getResources().getColor(R.color.color_ffb90f))
                .addCarInfo("拆钣喷","¥3201.0",getResources().getColor(R.color.color_e81d62))
                .addCarInfo("更换","¥4031.0",getResources().getColor(R.color.color_9B30FF));
    }


    public void changeColorBtn(View view){
        switch (view.getId()){
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
