package com.easyandroid.demo.video;

import android.content.Context;
import android.media.AudioManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.easyandroid.core.util.LogUtil;

/**
 * Author : zhousf
 * Description :
 * Date : 2017/12/28.
 */
public class VideoSurfaceView extends SurfaceView implements View.OnTouchListener{

    private static final String TAG = "VideoSurfaceView";

    public VideoSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    GestureDetectorCompat gestureDetector;
    AudioManager mAudioManager;

    private void init(Context context){
        setOnTouchListener(this);
        gestureDetector = new GestureDetectorCompat(context,new GestureListener());
        //音量控制,初始化定义
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private GestureListener() {
            super();
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtil.d(TAG,"onScroll");
            float difX = e1.getX() - e2.getX();
            float difY = e1.getY() - e2.getY();
            if(Math.abs(difX) > Math.abs(difY)){
                //x轴滑动
                if (e1.getX() - e2.getX() > 0) {
                    LogUtil.d(TAG,"向左");
                } else if (e2.getX() - e1.getX() > 0) {
                    LogUtil.d(TAG,"向右");
                }
            }else{
                //y轴滑动
                if(e1.getY() - e2.getY() > 0){
                    LogUtil.d(TAG,"向上");
                    raiseVolume();
                } else if(e2.getY() - e1.getY() > 0){
                    LogUtil.d(TAG,"向下");
                    lowerVolume();
                }

            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LogUtil.d(TAG,"onFling");
//            float difX = e1.getX() - e2.getX();
//            float difY = e1.getY() - e2.getY();
//            if(Math.abs(difX) > Math.abs(difY)){
//                //x轴滑动
//                if (e1.getX() - e2.getX() > 0) {
//                    LogUtil.d(TAG,"向左"+velocityX);
//                } else if (e2.getX() - e1.getX() > 0) {
//                    LogUtil.d(TAG,"向右"+velocityX);
//                }
//            }else{
//                //y轴滑动
//                if(e1.getY() - e2.getY() > 0){
//                    LogUtil.d(TAG,"向上"+velocityY);
//                    raiseVolume();
//                } else if(e2.getY() - e1.getY() > 0){
//                    LogUtil.d(TAG,"向下"+velocityY);
//                    lowerVolume();
//                }
//
//            }
            return false;
        }

        private void lowerVolume(){
            //降低音量，调出系统音量控制
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,
                    AudioManager.FX_FOCUS_NAVIGATION_UP);

        }

        private void raiseVolume(){
            //增加音量，调出系统音量控制
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
        }


    }



}
