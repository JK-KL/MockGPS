package com.zjl.mockgps.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class MainActivity extends Activity {
    private List<Location> locations = new ArrayList<Location>();
    private LocationManager locationManager;

    private TextView total;
    private TextView step;

    private MyHandler handler = new MyHandler();

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("LocationService", "Activity创建");
        total = (TextView) findViewById(R.id.totalSteps);
        step = (TextView) findViewById(R.id.stepsNow);
        mContext = this;
        Mytask task = new Mytask(this);
        task.execute();
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
            Log.i("LocationService", "定位服务开始!");
            //  Toast.makeText(mContext,"定位服务开始",Toast.LENGTH_SHORT);
            int count = 0;
            for (final Location l : locations) {
                //  Toast.makeText(mContext,"定位服务进行中",Toast.LENGTH_SHORT);
                //setTimerTask(l);
                long startTime = Calendar.getInstance().getTimeInMillis();
                long endTime = Calendar.getInstance().getTimeInMillis();
                while (endTime - startTime < 800) {
                    locationManager.setTestProviderLocation("gps", l);
                  //  Log.i("LocationService", "发送位置" + l);
                    endTime = Calendar.getInstance().getTimeInMillis();

                }
                publishProgress(count++);
            }
            Log.i("LocationService", "定位结束");

            handler.sendMessage(handler.obtainMessage(2, "完成啦"));

            return null;
        }

        public void getPoint() {
            ObjectMapper maper = new ObjectMapper();
            Coodinate[] po = null;
            List<Coodinate> points = new ArrayList<Coodinate>();
            String coodinate = "[\n" +
                    "    {\n" +
                    "        \"latitude\": 31.223928,\n" +
                    "        \"longitude\": 121.64769\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.223959,\n" +
                    "        \"longitude\": 121.647758\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.22402,\n" +
                    "        \"longitude\": 121.647896\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.22402,\n" +
                    "        \"longitude\": 121.647896\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.224396,\n" +
                    "        \"longitude\": 121.647675\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.224619,\n" +
                    "        \"longitude\": 121.647583\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.224749,\n" +
                    "        \"longitude\": 121.647598\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.226147,\n" +
                    "        \"longitude\": 121.646751\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.226341,\n" +
                    "        \"longitude\": 121.646629\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.226341,\n" +
                    "        \"longitude\": 121.646629\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.226402,\n" +
                    "        \"longitude\": 121.646996\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.226767,\n" +
                    "        \"longitude\": 121.64901\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.227222,\n" +
                    "        \"longitude\": 121.651649\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.227297,\n" +
                    "        \"longitude\": 121.652069\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.227726,\n" +
                    "        \"longitude\": 121.654594\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.227804,\n" +
                    "        \"longitude\": 121.655045\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.227882,\n" +
                    "        \"longitude\": 121.655487\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.228155,\n" +
                    "        \"longitude\": 121.657082\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.228247,\n" +
                    "        \"longitude\": 121.6576\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.228312,\n" +
                    "        \"longitude\": 121.65799\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.228373,\n" +
                    "        \"longitude\": 121.658356\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.228529,\n" +
                    "        \"longitude\": 121.659271\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.228559,\n" +
                    "        \"longitude\": 121.659424\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.228716,\n" +
                    "        \"longitude\": 121.660339\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.229101,\n" +
                    "        \"longitude\": 121.662567\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.229305,\n" +
                    "        \"longitude\": 121.663742\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.229322,\n" +
                    "        \"longitude\": 121.663834\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.229513,\n" +
                    "        \"longitude\": 121.664963\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.229717,\n" +
                    "        \"longitude\": 121.666115\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.230581,\n" +
                    "        \"longitude\": 121.669983\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.230825,\n" +
                    "        \"longitude\": 121.671227\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.231163,\n" +
                    "        \"longitude\": 121.672897\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.231276,\n" +
                    "        \"longitude\": 121.673454\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.231298,\n" +
                    "        \"longitude\": 121.673553\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.231333,\n" +
                    "        \"longitude\": 121.673714\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.23138,\n" +
                    "        \"longitude\": 121.673927\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.23218,\n" +
                    "        \"longitude\": 121.677185\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.232986,\n" +
                    "        \"longitude\": 121.680328\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.233282,\n" +
                    "        \"longitude\": 121.681442\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.233616,\n" +
                    "        \"longitude\": 121.682724\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.233698,\n" +
                    "        \"longitude\": 121.682693\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.233698,\n" +
                    "        \"longitude\": 121.682693\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.23378,\n" +
                    "        \"longitude\": 121.682777\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.234348,\n" +
                    "        \"longitude\": 121.684914\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.234692,\n" +
                    "        \"longitude\": 121.686203\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.234684,\n" +
                    "        \"longitude\": 121.686371\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.234692,\n" +
                    "        \"longitude\": 121.686424\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.235008,\n" +
                    "        \"longitude\": 121.688072\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.235199,\n" +
                    "        \"longitude\": 121.689064\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.235886,\n" +
                    "        \"longitude\": 121.69326\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.236025,\n" +
                    "        \"longitude\": 121.69413\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.236073,\n" +
                    "        \"longitude\": 121.694427\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.236822,\n" +
                    "        \"longitude\": 121.698814\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.236914,\n" +
                    "        \"longitude\": 121.699341\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.237091,\n" +
                    "        \"longitude\": 121.700188\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.237284,\n" +
                    "        \"longitude\": 121.701057\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.237566,\n" +
                    "        \"longitude\": 121.70224\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.237782,\n" +
                    "        \"longitude\": 121.703087\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.237934,\n" +
                    "        \"longitude\": 121.703621\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.237865,\n" +
                    "        \"longitude\": 121.703682\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.23786,\n" +
                    "        \"longitude\": 121.703682\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.238207,\n" +
                    "        \"longitude\": 121.70488\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.239296,\n" +
                    "        \"longitude\": 121.708328\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.239632,\n" +
                    "        \"longitude\": 121.709373\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.239883,\n" +
                    "        \"longitude\": 121.710175\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.239883,\n" +
                    "        \"longitude\": 121.710175\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.239883,\n" +
                    "        \"longitude\": 121.71035\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.240009,\n" +
                    "        \"longitude\": 121.710747\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.240122,\n" +
                    "        \"longitude\": 121.711128\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.240347,\n" +
                    "        \"longitude\": 121.711823\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.240448,\n" +
                    "        \"longitude\": 121.71196\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.241003,\n" +
                    "        \"longitude\": 121.71373\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.24184,\n" +
                    "        \"longitude\": 121.7164\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.241875,\n" +
                    "        \"longitude\": 121.716507\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.241875,\n" +
                    "        \"longitude\": 121.716507\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.240252,\n" +
                    "        \"longitude\": 121.717255\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"latitude\": 31.239513,\n" +
                    "        \"longitude\": 121.717499\n" +
                    "    }\n" +
                    "]";
            try {
                po = maper.readValue(coodinate, Coodinate[].class);
                points = CaculatePoints(po);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (points.size() != 0) {

                for (Coodinate cood : points) {
                    Location location = new Location("gps");
                    location.setTime(System.currentTimeMillis());
                    Gps GPS = PositionUtils.gcj_To_Gps84(cood.latitude, cood.longitude);
                    location.setLatitude(GPS.getWgLat());
                    location.setLongitude(GPS.getWgLon());
                    location.setAltitude(5.5f);
                    location.setAccuracy(2f);
                    location.setElapsedRealtimeNanos(300);
                    // Log.i("位置信息", location.getAccuracy() + "," + location.getSpeed() + ",");
                    locations.add(location);
                }
            }
        }

        public List<Coodinate> CaculatePoints(Coodinate[] points) {
            List<Coodinate> po = new ArrayList<Coodinate>();
            GPSCaculation GPSC = new GPSCaculation();
            for (int i = 0; i < points.length - 2; i++) {
                if ((points[i + 1].longitude - points[i].longitude == 0) || (points[i + 1].latitude - points[i].latitude == 0)) {
                    po.add(points[i]);
                    continue;
                }
                GPSC.cacu(points[i].longitude, points[i].latitude, points[i + 1].longitude, points[i + 1].latitude);
                if (points[i + 1].longitude - points[i].longitude > 0) {
                    BigDecimal bd = new BigDecimal(points[i + 1].longitude).subtract(new BigDecimal(points[i].longitude)).setScale(6, RoundingMode.HALF_UP).multiply(new BigDecimal(1000000));
                    int n = (bd.intValue() > 100 ? 100 : bd.intValue());
                    for (int j = 0; j < n; j++) {
                        BigDecimal x = new BigDecimal(j).divide(new BigDecimal(1000000));
                        BigDecimal y = GPSC.returnX(x.add(new BigDecimal(points[i].longitude)));
                        po.add(new Coodinate().setCood(y.doubleValue(), x.add(new BigDecimal(points[i].longitude)).doubleValue()));
                    }
                } else if (points[i + 1].latitude - points[i].latitude > 0) {
                    BigDecimal bd = new BigDecimal(points[i + 1].latitude).subtract(new BigDecimal(points[i].latitude)).setScale(6, RoundingMode.HALF_UP).multiply(new BigDecimal(1000000));
                    int n = (bd.intValue() > 100 ? 100 : bd.intValue());
                    for (int j = 0; j < n; j++) {
                        BigDecimal x = new BigDecimal(j).divide(new BigDecimal(1000000));
                        BigDecimal y = GPSC.returnY(x.add(new BigDecimal(points[i].latitude)));
                        po.add(new Coodinate().setCood(x.add(new BigDecimal(points[i].latitude)).doubleValue(), y.doubleValue()));
                    }
                }
            }
            return po;
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
                    total.setText("总共" + msg.obj + "步");
                    break;
                case 2:
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();

                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }
}
