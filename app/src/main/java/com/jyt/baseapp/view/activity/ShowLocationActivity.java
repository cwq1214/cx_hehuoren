package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.util.IntentHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/9.
 */

public class ShowLocationActivity extends BaseActivity {


    String latitude;
    String longitude;
    String selfLat;
    String selfLon;


    @BindView(R.id.mapView)
    MapView mapView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_location;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {

            //个人位置
            MyLocationData locationData = new MyLocationData.Builder().latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mapView.getMap().setMyLocationData(locationData);


        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapView.getMap().setMyLocationEnabled(true);
        mapView.showZoomControls(false);


        latitude  = getIntent().getStringExtra(IntentHelper.KEY_LATITUDE);
        longitude = getIntent().getStringExtra(IntentHelper.KEY_LONGITUDE);

        selfLat = App.getLocation().getLatitude()+"";
        selfLon = App.getLocation().getLongitude()+"";


        LatLng point = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.location2);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mapView.getMap().addOverlay(option);
        MapStatus status = new MapStatus.Builder()
                .target(point)
                .zoom(17).build();
        mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(point, 17));

        App.mLocationClient.registerLocationListener(locationListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.mLocationClient.unRegisterLocationListener(locationListener);
    }
}
