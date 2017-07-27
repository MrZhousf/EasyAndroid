package com.easyandroid.demo.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseRefreshFragment;
import com.easyandroid.core.util.LogUtil;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 视频
 */
public class VideoFragment extends BaseRefreshFragment
        implements View.OnClickListener,SurfaceHolder.Callback, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener {

    private static final String TAG = "VideoFragment";

    private String title = "";

    IjkMediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private LinearLayout linearLayout;
    private boolean f=true;
    private View view;
    /** 暂停 */
    private Button mButton1;
    /** 播放 */
    private Button mButton2;

    String[] urls = new String[]{
            "http://ips.ifeng.com/video.ifeng.com/video04/2011/03/24/480x360_offline20110324.mp4",
            "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8",
            "http://video.tvbt.sj.360.cn/vod-media-bj/2317731_6412320_201703011105096cf2d16d-8781-4fe1-b48b-d55013198ff4.m3u8",
            "http://clip.uhoop.tom.com/6c6266/20100919/hd16003.flv"
    };

    @Override
    protected int initLayout() {
        return R.layout.fragment_video;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        title  = getArguments().getString("title","");
        LogUtil.d(TAG,"title = "+title);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d(TAG,"onViewCreated "+title);
        mButton1 = (Button) view.findViewById(R.id.button1);
        mButton2 = (Button) view.findViewById(R.id.button2);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        surfaceView= (SurfaceView) view.findViewById(R.id.surfaceView);
        linearLayout= (LinearLayout) view.findViewById(R.id.line1);
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        mediaPlayer=new IjkMediaPlayer();
        try {
            if("1".equals(title)){
                mediaPlayer.setDataSource(urls[0]);
            }else if("2".equals(title)){
                mediaPlayer.setDataSource(urls[1]);
            }else{
                mediaPlayer.setDataSource(urls[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mediaPlayer准备工作
        mediaPlayer.setOnPreparedListener(this);
        //MediaPlayer完成
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);//当前加载进度的监听
//        //开启异步准备
//        mediaPlayer.prepareAsync();
//        //当触摸surfaceview时显示或消失底部按钮
//        surfaceView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction()==(MotionEvent.ACTION_DOWN))
//                {
//                    if (f==true)
//                    {
//                        f=false;
//                        linearLayout.setVisibility(View.VISIBLE);
//                    }else {
//                        f=true;
//                        linearLayout.setVisibility(View.GONE);
//                    }
//                }
//
//                return true;
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                mediaPlayer.pause();
                break;
            case R.id.button2:
                mediaPlayer.start();
                break;
        }
    }

    @Override
    public void onResume() {
        LogUtil.d(TAG,"onResume "+title);
        super.onResume();
    }

    @Override
    protected void onRefreshTop() {

    }

    @Override
    protected void onRefreshBottom() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //连接ijkPlayer 和surfaceHOLDER
        mediaPlayer.setDisplay(holder);
        //开启异步准备
        mediaPlayer.prepareAsync();
        LogUtil.d(TAG,"surfaceCreated "+title);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.d(TAG,"surfaceChanged "+title);
//        mediaPlayer.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.d(TAG,"surfaceDestroyed "+title);
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        LogUtil.d(TAG,"onBufferingUpdate "+title +"  i="+i);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
//        iMediaPlayer.seekTo(0);
//        iMediaPlayer.start();
        LogUtil.d(TAG,"onCompletion "+title);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
//        iMediaPlayer.start();
        LogUtil.d(TAG,"onPrepared "+title);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(TAG,"onPause "+title);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(TAG,"onStop "+title);
    }

    long current =0;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(mediaPlayer!=null){
                mediaPlayer.seekTo(current);
                mediaPlayer.start();
                long totalCache = mediaPlayer.getVideoCachedBytes();
                LogUtil.d(TAG,"可见"+title +" current="+current+",cache="+totalCache);
            }
        }else{
            if(mediaPlayer!=null){
                mediaPlayer.pause();
                current = mediaPlayer.getCurrentPosition();
            }
            LogUtil.d(TAG,"不可见"+title +"  current="+current);
        }
    }
}
