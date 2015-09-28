package com.zjl.mockgps.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.zjl.mockgps.app.Algorithm.CEAlgorithm;
import com.zjl.mockgps.app.Base.BaseActivity;
import com.zjl.mockgps.app.Model.Coodinate;
import com.zjl.mockgps.app.Common.CollectionExtension;
import com.zjl.mockgps.app.Algorithm.PositionUtils;
import com.zjl.mockgps.app.R;

import java.util.*;


public class MainActivity extends BaseActivity {
    private TextView total;
    private TextView step;
    private TextView tips;
    private Button reset;

    private MyHandler handler = new MyHandler();
    private final Mytask task = new Mytask(this);
    private List<Coodinate> originalPoints = new ArrayList<Coodinate>();
    private Context mContext;
    private List<Location> locations = new ArrayList<Location>();
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("LocationService", "Activity创建");
        Bundle bundle = getIntent().getExtras();
        originalPoints = bundle.getParcelableArrayList("points");
        Init();
        task.execute();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.cancel(true);
            }
        });
    }

    public void Init() {
        total = (TextView) findViewById(R.id.totalSteps);
        step = (TextView) findViewById(R.id.stepsNow);
        tips = (TextView) findViewById(R.id.tips);
        reset = (Button) findViewById(R.id.reset);
        mContext = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class Mytask extends AsyncTask<String, Integer, String> {
        public Context mContext;

        Mytask(Context context) {
            this.mContext = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i("LocationService", "定位服务创建!");
            //  Toast.makeText(mContext, "定位服务创建", Toast.LENGTH_SHORT);
            locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
            locationManager.addTestProvider("gps", false, false, false, false, true,
                    true, true, 0, 5);
            locationManager.setTestProviderEnabled("gps", true);
            locationManager.setTestProviderStatus("gps", LocationProvider.AVAILABLE, null, System.currentTimeMillis());
            getPoint();
            Message message = handler.obtainMessage(1, locations.size());
            handler.sendMessage(message);
            handler.sendMessage(handler.obtainMessage(2, "尽量不要把App放在后台执行哦"));
            Log.i("LocationService", "定位服务开始!");
            //  Toast.makeText(mContext,"定位服务开始",Toast.LENGTH_SHORT);
            int count = 0;
            for (final Location l : locations) {
                long startTime = Calendar.getInstance().getTimeInMillis();
                long endTime = Calendar.getInstance().getTimeInMillis();
                while (endTime - startTime < 1000) {
                    locationManager.setTestProviderLocation("gps", l);
                    //Log.i("LocationService", "发送位置" + l);
                    endTime = Calendar.getInstance().getTimeInMillis();

                }
                publishProgress(count++);
            }
            Log.i("LocationService", "定位结束");
            handler.sendMessage(handler.obtainMessage(2, "完成啦"));
            return null;
        }

        public void getPoint() {
            CEAlgorithm ce = new CEAlgorithm();
            ce.setOriginalCood(originalPoints);
            List<Coodinate> points = ce.expand();
            if (!CollectionExtension.IsNullOrEmpty(points)) {
                for (Coodinate cood : points) {
                    Location location = new Location("gps");
                    location.setTime(System.currentTimeMillis());
                    Coodinate GPS = PositionUtils.gcj_To_Gps84(cood.latitude, cood.longitude);
                    location.setLatitude(GPS.latitude);
                    location.setLongitude(GPS.longitude);
                    location.setAltitude(5.5f);
                    location.setAccuracy(2f);
                    if (Build.VERSION.SDK_INT > 17)
                        location.setElapsedRealtimeNanos(300);
                    locations.add(location);
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            step.setText("现在" + values[0] + "步");
            super.onProgressUpdate(values);
        }
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    total.setText("总共" + msg.obj + "米");
                    break;
                case 2:
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    tips.setText(msg.obj.toString());
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LocationService", "Activity销毁");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LocationService", "Activity暂停");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LocationService", "Activity复活");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LocationService", "Activity重新开始");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LocationService", "Activity开始");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LocationService", "Activity停止");
    }


}
