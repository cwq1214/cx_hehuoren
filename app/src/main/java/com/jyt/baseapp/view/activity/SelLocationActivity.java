package com.jyt.baseapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.L;
import com.jyt.baseapp.util.T;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/12/23.
 */
public class SelLocationActivity extends BaseActivity {
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.search_content)
    LinearLayout searchContent;
    @BindView(R.id.search_mask)
    LinearLayout searchMask;
    @BindView(R.id.toCurrentLocation)
    ImageView toCurrentLocation;
    @BindView(R.id.address)
    TextView address;

    boolean zoomOnce = false;
    LatLng currentLatlng;
    String currentCity;
    String currentAddress;
    List<Overlay> marksList=new ArrayList();
    String lat;
    String lon;
    boolean isSel= true;


    @Override
    public int getLayoutId() {
        return R.layout.activity_sel_location;
    }

    @Override
    protected View getContentView() {
        return null;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mapView.getMap().setMyLocationEnabled(true);
        mapView.showZoomControls(false);


        BDLocation location = App.getLocation();
        if (location!=null) {
            createSelfLocationMark(location);
        }

        CloudManager.getInstance().init(new CloudListener() {
            @Override
            public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i){
                if (cloudSearchResult==null)
                    return;
                List<CloudPoiInfo> poiList = cloudSearchResult.poiList;
                L.e("i",i);
                for (int j=0;j<poiList.size();j++){
                    L.e("poi",poiList.get(j).address);
                }
//                L.e();
            }


            @Override
            public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
                L.e("i",i);
            }

            @Override
            public void onGetCloudRgcResult(CloudRgcResult cloudRgcResult, int i) {
                List customPois = cloudRgcResult.customPois;
                L.e("i",i);
                for (int j=0;j<customPois.size();j++){
                    L.e("poi",customPois.get(j).toString());
                }
            }
        });

        setupSearchView();
        setupLocation();

         lat = getIntent().getStringExtra(IntentHelper.KEY_LATITUDE);
         lon = getIntent().getStringExtra(IntentHelper.KEY_LONGITUDE);

        btnFunction.setText("确定");
        btnFunction.setVisibility(View.VISIBLE);

//        if (!TextUtils.isEmpty(lat)&&!TextUtils.isEmpty(lon)){
//            functionBtn.setVisibility(View.GONE);
//            isSel = false;
//            //定义Maker坐标点
//            LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
//            //构建Marker图标
//            BitmapDescriptor bitmap = BitmapDescriptorFactory
//                    .fromResource(R.mipmap.location2);
//            //构建MarkerOption，用于在地图上添加Marker
//            OverlayOptions option = new MarkerOptions()
//                    .position(point)
//                    .icon(bitmap);
//            //在地图上添加Marker，并显示
//            mapView.getMap().addOverlay(option);
//            MapStatus status = new MapStatus.Builder()
//                    .target(point)
//                    .zoom(17).build();
//            mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(status));
//        }
    }


    private void setupSearchView(){

        searchMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openSearchLocationActivityForResult(getContext(),currentLatlng);
            }
        });
    }

    private void createSelfLocationMark(BDLocation location){
        MyLocationData locationData = new MyLocationData.Builder().latitude(location.getLatitude()).longitude(location.getLongitude()).build();
        mapView.getMap().setMyLocationData(locationData);
        currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        currentCity = location.getCity();
        currentAddress = location.getAddrStr();
        if (!zoomOnce) {
            onToCurrentLocationClick();
            zoomOnce = true;
        }
        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
//            btnFunction.setVisibility(View.GONE);
            isSel = false;
            //定义Maker坐标点
            LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
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

        }
    }

    private void setupLocation(){
        App.mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {

                MyLocationData locationData = new MyLocationData.Builder().latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                mapView.getMap().setMyLocationData(locationData);
                currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                currentCity = location.getCity();
                currentAddress = location.getAddrStr();
                if (!zoomOnce) {
                    onToCurrentLocationClick();
                    zoomOnce = true;
                }
                if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
                    btnFunction.setVisibility(View.GONE);
                    isSel = false;
                    //定义Maker坐标点
                    LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
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

                }

                App.mLocationClient.unRegisterLocationListener(this);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });

    }


    @OnClick(R.id.toCurrentLocation)
    public void onToCurrentLocationClick(){
        mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(currentLatlng, 17));
        address.setText(currentAddress);
        address.setTag(currentLatlng);

    }

    @Override
    public void onFunctionClick() {
        super.onFunctionClick();
        if (TextUtils.isEmpty(address.getText().toString())){
            T.showShort(getContext(),"未选定位置");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(IntentHelper.KEY_LATLNG, (Parcelable) address.getTag());
        intent.putExtra(IntentHelper.KEY_ADDRESS,address.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IntentHelper.REQUIRE_CODE_SEARCH_LOCATION&&resultCode== Activity.RESULT_OK){
            locationResult(data);
        }
    }

    private void locationResult(Intent data){
        PoiInfo poiInfo = data.getParcelableExtra(IntentHelper.KEY_POIINFO);
        //定义Maker坐标点
                LatLng point =poiInfo.location;
        //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.location_pop);
        //构建MarkerOption，用于在地图上添加Marker
        Bundle bundle = new Bundle();
        bundle.putString("address",poiInfo.address);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .title(poiInfo.name)
                .extraInfo(bundle);
        //删除其他所有Marker
        if (marksList.size()!=0){
            for (int i=marksList.size()-1;i>=0;i--){
                marksList.get(i).remove();
                marksList.remove(marksList.get(i));
            }

        }

        //在地图上添加Marker，并显示
        marksList.add(mapView.getMap().addOverlay(option));

        mapView.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                address.setText(marker.getExtraInfo().getString("address"));
                address.setTag(marker.getPosition());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(marker.getPosition());
                mapView.getMap().animateMapStatus(update);
                return true;
            }
        });

        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(poiInfo.location);
        mapView.getMap().animateMapStatus(update);
        address.setText(poiInfo.address+poiInfo.name);
        address.setTag(poiInfo.location);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
