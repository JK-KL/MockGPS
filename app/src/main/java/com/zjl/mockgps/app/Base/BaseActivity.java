package com.zjl.mockgps.app.Base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by C0dEr on 15/9/27.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.activityList.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.activityList.remove(this);
    }


}
