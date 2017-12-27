package com.easyandroid.demo.video;

import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.easyandroid.R;
import com.easyandroid.core.base.BaseActivity;
import com.easyandroid.core.util.LogUtil;
import com.easyandroid.core.util.ToastUtil;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 视频模块
 */
public class VideoActivity extends BaseActivity implements View.OnClickListener,SurfaceHolder.Callback, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnCompletionListener,IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnBufferingUpdateListener,SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "VideoActivity";

    private IjkMediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    /** 播放 */
    private Button playBtn;
    /** 暂停 */
    private Button pauseBtn;
    private SeekBar seekBar;

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
        playBtn = (Button) findViewById(R.id.playBtn);
        pauseBtn = (Button) findViewById(R.id.pauseBtn);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        surfaceView= (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
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
        //seek监听
        mediaPlayer.setOnSeekCompleteListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pauseBtn:
                mediaPlayer.pause();
                break;
            case R.id.playBtn:
                if(mediaPlayer.isPlayable()){
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                    }
                }else{
                    ToastUtil.show(this,"正在加载...");
                }
                break;
        }
    }
    private int currentPosition;
    private int seekPosition;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer.isPlaying()){
                if(isSeeking){
                    seekBar.setProgress(seekPosition);
                    LogUtil.d(TAG,"setProgress1 "+seekPosition);
                }else{
                    long p = mediaPlayer.getCurrentPosition();
                    if(p > 0 && p != currentPosition){
                        currentPosition = (int)p;
                        seekBar.setProgress(currentPosition);
                        LogUtil.d(TAG,"setProgress "+currentPosition);
                    }
                }
            }
            handler.postDelayed(this,50);
        }
    };
    private boolean isSeeking;
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(!fromUser) {
            return ;
        }
        if(progress > 0 ){
            isSeeking = true;
            mediaPlayer.pause();
            mediaPlayer.seekTo(progress);
            LogUtil.d(TAG,"seekTo "+progress);
        }
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        LogUtil.d(TAG,"onSeekComplete");
        mediaPlayer.start();
        seekPosition = (int) iMediaPlayer.getCurrentPosition();
        isSeeking = false;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //连接ijkPlayer 和surfaceHOLDER
        mediaPlayer.setDisplay(holder);
        LogUtil.d(TAG,"surfaceCreated");
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
        seekBar.setMax((int)iMediaPlayer.getDuration());
        handler.post(runnable);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
