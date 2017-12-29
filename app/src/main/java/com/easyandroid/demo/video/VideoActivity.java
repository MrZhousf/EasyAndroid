package com.easyandroid.demo.video;

import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseActivity;
import com.easyandroid.core.util.LogUtil;
import com.easyandroid.core.util.UtilManager;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 视频模块
 */
public class VideoActivity extends BaseActivity implements View.OnClickListener,SurfaceHolder.Callback, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener,SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "VideoActivity";

    private IjkMediaPlayer mediaPlayer;
    private VideoSurfaceView surfaceView;
    private SeekBar seekBar;
    private boolean isSeeking;
    private int currentPosition;
    private boolean isPrepared;

    String[] urls = new String[]{
            "http://ips.ifeng.com/video.ifeng.com/video04/2011/03/24/480x360_offline20110324.mp4",
            "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8",
            "http://video.tvbt.sj.360.cn/vod-media-bj/2317731_6412320_201703011105096cf2d16d-8781-4fe1-b48b-d55013198ff4.m3u8",
            "http://clip.uhoop.tom.com/6c6266/20100919/hd16003.flv"
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_video;
    }

    @Override
    protected void initTitle(TitleBar title) {
        title.title = getString(R.string.videoDemo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button playBtn = (Button) findViewById(R.id.playBtn);
        Button pauseBtn = (Button) findViewById(R.id.pauseBtn);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        mediaPlayer = new IjkMediaPlayer();
        try {
            mediaPlayer.setDataSource(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mediaPlayer准备工作
        mediaPlayer.setOnPreparedListener(this);
        //MediaPlayer完成
        mediaPlayer.setOnCompletionListener(this);
        //当前加载进度的监听
        mediaPlayer.setOnBufferingUpdateListener(this);
        surfaceView = (VideoSurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pauseBtn:
                mediaPlayer.pause();
                break;
            case R.id.playBtn:
                if(!isPrepared){
                    UtilManager.Toast.show(this,"正在初始化播放器，请稍后再试。");
                    return ;
                }
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,50);
            if(isSeeking)
                return ;
            long p = mediaPlayer.getCurrentPosition();
            if(p > 0 && p != currentPosition){
                currentPosition = (int)p;
                seekBar.setProgress(currentPosition);
            }

        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isSeeking = true;
        LogUtil.d(TAG,"onStartTrackingTouch "+seekBar.getProgress());
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeeking = false;
        LogUtil.d(TAG,"onStopTrackingTouch "+seekBar.getProgress());
        mediaPlayer.seekTo(seekBar.getProgress());
        mediaPlayer.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.d(TAG,"surfaceCreated");
        mediaPlayer.setDisplay(holder);
        //开启异步准备
        mediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.d(TAG,"surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.d(TAG,"surfaceDestroyed");
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        LogUtil.d(TAG,"onPrepared");
        isPrepared = true;
        seekBar.setMax((int)iMediaPlayer.getDuration());
        handler.post(runnable);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

}
