package com.zjl.mockgps.app;

import android.app.Application;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by C0dEr on 15/8/7.
 */
public class BaseApplication extends Application {
     @Override
    public void onCreate(){
         super.onCreate();
         SDKInitializer.initialize(this);
     }
}
