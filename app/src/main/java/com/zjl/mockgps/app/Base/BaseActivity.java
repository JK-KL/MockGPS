package com.zjl.mockgps.app.Base;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by C0dEr on 15/9/27.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.activityList.add(this);
        ConnectivityManager con=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(con.getActiveNetworkInfo()==null
                ||!con.getActiveNetworkInfo().isConnected()
                ||!con.getActiveNetworkInfo().isAvailable()){
            Toast.makeText(this, "网络未连接!", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * findViewById的简写
     *
     * @param resId Id
     * @param <T>   控件类型
     * @return 控件
     */
    protected <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.activityList.remove(this);
        Log.i("Activity info", this.getLocalClassName() + "销毁");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity info", this.getLocalClassName() + "onResume");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity info", this.getLocalClassName() + "onStop");
    }

    @Override
    protected void onStart() {
        super.onDestroy();
        Log.i("Activity info", this.getLocalClassName() + "onStart");
    }
    @Override
    protected void onRestart() {
        super.onDestroy();
        Log.i("Activity info", this.getLocalClassName() + "onRestart");
    }
    @Override
    protected void onPause() {
        super.onDestroy();
        Log.i("Activity info", this.getLocalClassName() + "onPause");
    }

}
