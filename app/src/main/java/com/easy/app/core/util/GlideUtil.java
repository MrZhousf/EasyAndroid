package com.easy.app.core.util;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.easy.R;
import com.easy.app.core.bean.GlideCircleTransform;

import static com.bumptech.glide.Glide.with;

public class GlideUtil {

    private static final String TAG = GlideUtil.class.getSimpleName();

    public static void loadImg(View view,int resId){
        Glide.with(view.getContext())
                .load(resId)
                .crossFade()
                .into((ImageView) view);
    }

    public static void loadImg(final View view, String url){
        if(view instanceof ImageView){
            Glide.with(view.getContext())
                    .load(url)
                    .centerCrop()
                    .listener(logListener)
                    .placeholder(R.mipmap.defalut_img)
                    .into((ImageView) view);
        }else{
            DrawableRequestBuilder builder = Glide.with(view.getContext())
                    .load(url)
                    .centerCrop()
                    .listener(logListener)
                    .dontAnimate()//无动画
                    .placeholder(R.mipmap.defalut_img) //默认占位图
                    .error(R.mipmap.error_img)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    ;
            builder.into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        view.setBackground(resource);
                    }
                }
            });
        }
    }

    public static void loadRoundImg(ImageView iv, String url) {
        with(iv.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(iv.getContext()))
                .error(R.mipmap.error_img)
                .into(iv);
    }

    private static RequestListener<String, GlideDrawable> logListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            //打印请求URL
            Log.d(TAG, "onException: " + model);
            //显示错误信息
            Log.w(TAG, "onException: ", e);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            //返回true表示拦截不再传递，false表示事件会传递下去
            Log.d(TAG, "url=: " + model);
            return false;
        }
    };






}
