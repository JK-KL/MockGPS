package com.zjl.mockgps.app.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;
import com.baidu.mapapi.search.route.*;
import com.zjl.mockgps.app.Base.BaseActivity;
import com.zjl.mockgps.app.Base.BaseApplication;
import com.zjl.mockgps.app.Model.Coodinate;
import com.zjl.mockgps.app.R;
import com.zjl.mockgps.app.PopWindow.SuggestWindow;
import com.zjl.mockgps.app.PopWindow.pointSettingWindow;
import com.zjl.mockgps.app.common.CollectionExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by C0dEr on 15/8/12.
 */
public class MapActivity extends BaseActivity implements OnGetGeoCoderResultListener, OnGetRoutePlanResultListener, BDLocationListener {
    private MapView mMapView;
    public EditText startPoint;
    public EditText endPoint;
    private Button pathSearch;
    private BaiduMap mBaiduMap;
    private Context mContext;

    private pointSettingWindow mWindow;
    public SuggestWindow popWindow;
    private GeoCoder mSearch;
    private RoutePlanSearch mRoutePlanSearch;
    private List<Marker> markers = new ArrayList<Marker>();
    int nodeIndex = -1;//节点索引,供浏览节点时使用

    private LocationClient mLocationClient;

    private String City = "上海";
    private LatLng currentLocation = new LatLng(31.227, 121.481);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        mContext = this;
        initComponent();
        initListener();
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        mRoutePlanSearch.setOnGetRoutePlanResultListener(this);


    }


    private void initComponent() {
        mMapView = $(R.id.mapview);
        startPoint = $(R.id.startPoint);
        endPoint = $(R.id.endPoint);
        pathSearch = $(R.id.search);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient = new LocationClient(this.getApplicationContext());
        mLocationClient.registerLocationListener(this);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(currentLocation));
        startPoint.setText("上海市浦东新区杨高南路立交桥");
        endPoint.setText("上海市环东二大道立交桥");
    }

    private void initListener() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                View.OnClickListener mClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.makeStart:
                                BitmapDescriptor st = BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
                                OverlayOptions sto = new MarkerOptions().position(latLng).icon(st)
                                        .zIndex(9).draggable(true);
                                Marker startMarker = (Marker) mBaiduMap.addOverlay(sto);
                                startMarker.setTitle("start");
                                for (Marker m : markers) {
                                    if (m.getTitle().toString().equals("start")) {
                                        m.remove();
                                        markers.remove(m);
                                        break;
                                    }
                                }
                                markers.add(startMarker);
                                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
                                break;
                            case R.id.makeEnd:
                                BitmapDescriptor en = BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
                                OverlayOptions eno = new MarkerOptions().position(latLng).icon(en)
                                        .zIndex(9).draggable(true);
                                Marker endMarker = (Marker) mBaiduMap.addOverlay(eno);
                                endMarker.setTitle("end");
                                for (Marker m : markers) {
                                    if (m.getTitle().toString().equals("end")) {
                                        m.remove();
                                        markers.remove(m);
                                        break;
                                    }
                                }
                                markers.add(endMarker);

                                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
                                break;
                        }
                    }
                };
                mWindow = new pointSettingWindow((Activity) mContext, mClickListener);
                mWindow.showAtLocation(MapActivity.this.findViewById(R.id.mapview), Gravity.TOP, 0, 80);
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {


                return false;
            }
        });

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //  mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(marker.getPosition()));
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });
        pathSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (startPoint.getText().equals("")) {
                    Toast.makeText(mContext, "请设置起始点", Toast.LENGTH_SHORT).show();
                }
                if (endPoint.getText().equals("")) {
                    Toast.makeText(mContext, "请设置结束点", Toast.LENGTH_SHORT).show();
                }
                PlanNode start = PlanNode.withCityNameAndPlaceName(City, startPoint.getText().toString());
                PlanNode end = PlanNode.withCityNameAndPlaceName(City, endPoint.getText().toString());
                mRoutePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(start).to(end));
            }
        });
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        //TODO 坐标位置转地址,这里没有用处
        Log.i("Location", geoCodeResult.getAddress());

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
            Log.i("Location", reverseGeoCodeResult.getAddress());
            String wordReg = "[a-zA-Z][0-9]{1,4}";
            Pattern pattern = Pattern.compile(wordReg);
            Matcher matcher=pattern.matcher(reverseGeoCodeResult.getAddress());
            if(!CollectionExtension.IsNullOrEmpty(markers)){
                switch (markers.get(markers.size() - 1).getTitle()) {
                    case "start":
                        startPoint.setText(matcher.replaceAll(""));
                        break;
                    case "end":
                        endPoint.setText(matcher.replaceAll(""));
                        break;
                }
            }else{
                City=reverseGeoCodeResult.getAddressDetail().city;
            }

        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            //return;
        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            SuggestAddrInfo info = walkingRouteResult.getSuggestAddrInfo();
            popWindow.showAtLocation(mMapView, Gravity.TOP, 0, 0);
            //return;
        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBaiduMap.clear();
            RouteLine route = walkingRouteResult.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(walkingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            Toast.makeText(mContext, "该段路程一共" + route.getDistance() + "米", Toast.LENGTH_SHORT).show();
            List<WalkingRouteLine.WalkingStep> steps = route.getAllStep();
            final List<Coodinate> mPoints = new ArrayList<Coodinate>();
            for (WalkingRouteLine.WalkingStep step : steps) {
                mPoints.addAll(Coodinate.La2Cood(step.getWayPoints()));
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("是否开始模拟移动?")
                    .setTitle("操作")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent mIntent = new Intent(MapActivity.this, MainActivity.class);
                            mIntent.putParcelableArrayListExtra("points", (ArrayList<Coodinate>) mPoints);
                            startActivity(mIntent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog dia = builder.create();
            dia.show();

        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation.getLocType() != BDLocation.TypeGpsLocation
                && bdLocation.getLocType() != BDLocation.TypeNetWorkLocation
                && bdLocation.getLocType() != BDLocation.TypeOffLineLocation) {
            return;
        }
        currentLocation = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(currentLocation));
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BaseApplication.exitAppWithDialog(this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        mLocationClient.stop();

        super.onStop();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
