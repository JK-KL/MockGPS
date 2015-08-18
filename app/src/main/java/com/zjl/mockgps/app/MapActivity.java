package com.zjl.mockgps.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;
import com.baidu.mapapi.search.route.*;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjl.mockgps.app.adapter.SuggestInfoAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by C0dEr on 15/8/12.
 */
public class MapActivity extends Activity implements OnGetGeoCoderResultListener, OnGetRoutePlanResultListener {
    private MapView mMapView;
    public EditText startPoint;
    public EditText endPoint;
    private Button pathSearch;
    private BaiduMap mBaiduMap;
    private Context mContext;

    private pointSettingWindow mWindow;

    private GeoCoder mSearch;
    private RoutePlanSearch mRoutePlanSearch;
    private static final LatLng GEO_SHANGHAI = new LatLng(31.227, 121.481);
    private List<Marker> markers = new ArrayList<Marker>();
    int nodeIndex = -1;//节点索引,供浏览节点时使用


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
        mMapView = (MapView) findViewById(R.id.mapview);
        startPoint = (EditText) findViewById(R.id.startPoint);
        endPoint = (EditText) findViewById(R.id.endPoint);
        pathSearch = (Button) findViewById(R.id.search);
        mBaiduMap = mMapView.getMap();
        //设置默认位置为上海;
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(GEO_SHANGHAI));
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
//                PlanNode start = PlanNode.withCityNameAndPlaceName("上海", startPoint.getText().toString());
//                PlanNode end = PlanNode.withCityNameAndPlaceName("上海", endPoint.getText().toString());

                PlanNode start = PlanNode.withCityNameAndPlaceName("上海","上海市闵行区虹梅南路立交桥");
                PlanNode end = PlanNode.withCityNameAndPlaceName("上海", "上海市闵行区Y026(鲁山路)");

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
            switch (markers.get(markers.size() - 1).getTitle()) {
                case "start":
                    startPoint.setText(reverseGeoCodeResult.getAddress());
                    break;
                case "end":
                    endPoint.setText(reverseGeoCodeResult.getAddress());
                    break;
            }
        }
    }


    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息

            SuggestAddrInfo info = walkingRouteResult.getSuggestAddrInfo();
            SuggestInfoAdapter adapter = new SuggestInfoAdapter(mContext, info);
            SuggestWindow popWindow = new SuggestWindow((MapActivity) mContext, adapter);
            popWindow.showAtLocation(mMapView, Gravity.BOTTOM, 0, 0);
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

            Log.i("distance", route.getDistance() + "");
            List<WalkingRouteLine.WalkingStep> steps = route.getAllStep();
            List<LatLng> mPoints = new ArrayList<LatLng>();
            for (WalkingRouteLine.WalkingStep step : steps) {
                mPoints.addAll(step.getWayPoints());
            }
            ObjectMapper om = new ObjectMapper();
            try {
                JsonGenerator mJson = om.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
                mJson.writeObject(mPoints);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

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
}
