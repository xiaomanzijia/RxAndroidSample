package com.licheng.app.rxandroidsample;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by licheng on 24/5/16.
 */
public class RxAndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
