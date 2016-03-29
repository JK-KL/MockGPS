package com.zjl.mockgps.app.Base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import com.baidu.mapapi.SDKInitializer;
import com.zjl.mockgps.app.common.CollectionExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C0dEr on 15/8/7.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }

    public static List<Activity> activityList = new ArrayList<Activity>();

    /**
     * 退出应用程序
     */
    public static void exitAppWithDialog(Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("您确定要退出程序吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!CollectionExtension.IsNullOrEmpty(activityList)) {
                    for (Activity activity : activityList) {
                        activity.finish();
                    }

                }
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

}
