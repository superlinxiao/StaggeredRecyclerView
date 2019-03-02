package com.wuxiaolong.pullloadmorerecyclerviewsample;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author lizheng
 * create at 2019/3/2
 * description:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this, new ImageLoaderConfig().getImagePipelineConfig(this));
    }
}
