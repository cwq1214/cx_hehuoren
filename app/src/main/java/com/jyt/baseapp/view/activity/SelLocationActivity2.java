package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.entity.Province;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/7/31.
 */

public class SelLocationActivity2 extends BaseActivity {
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.toCurrentLocation)
    ImageView toCurrentLocation;
    @BindView(R.id.ding)
    ImageView ding;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.btn_changePlace)
    TextView btnChangePlace;

    boolean zoomOnce = false;
    String lat;
    String lon;
    BDLocationListener bdLocationListener;

    String currentProvince;//省
    String currentCity;//市
    String currentDistrict;//区
    LatLng currentLatlng;//当前经纬度
    String currentAddress;//当前地址
    boolean canEditAddress;
    GeoCoder geoCoder;
    PoiSearch poiSearch;
    DistrictSearch mDistrictSearch;

    OptionsPickerView pickerView;


    int searchTimes=0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sel_location2;
    }

    @Override
    protected View getContentView() {
        return null;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.getMap().setMyLocationEnabled(true);
        mapView.showZoomControls(false);

        btnFunction.setText("确定");
        btnFunction.setVisibility(View.VISIBLE);

        geoCoder = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    T.showShort(getContext(), "抱歉，未能找到结果");
                    return;
                }
                List<PoiInfo> poiList = result.getPoiList();
                if (poiList != null && poiList.size() != 0) {
                    address.setText(poiList.get(0).address);
                    address.setTag(poiList.get(0).location);
                }
            }

            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);


        lat = getIntent().getStringExtra(IntentHelper.KEY_LATITUDE);
        lon = getIntent().getStringExtra(IntentHelper.KEY_LONGITUDE);
        currentProvince = getIntent().getStringExtra(IntentHelper.KEY_PROVINCE);
        currentCity = getIntent().getStringExtra(IntentHelper.KEY_CITY);
        currentDistrict = getIntent().getStringExtra(IntentHelper.KEY_DISTRICT);
        canEditAddress = getIntent().getBooleanExtra(IntentHelper.KEY_EDIT_ADDRESS, false);

        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
            if (!canEditAddress) {
                //定义Maker坐标点
                LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.ding);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mapView.getMap().addOverlay(option);
                mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(point, 17));
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double.valueOf(lat), Double.valueOf(lon))));
//            }
            } else {
                LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(point, 17));
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double.valueOf(lat), Double.valueOf(lon))));
            }
        } else {
            if (TextUtils.isEmpty(lat)||TextUtils.isEmpty(lon)) {
                if (mDistrictSearch == null)
                    mDistrictSearch = DistrictSearch.newInstance();

                mDistrictSearch.setOnDistrictSearchListener(new OnGetDistricSearchResultListener() {
                    @Override
                    public void onGetDistrictResult(DistrictResult districtResult) {
                        if (districtResult.centerPt != null) {
                            mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(districtResult.centerPt, 17));
                            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(districtResult.centerPt));

                        }else {
                            BDLocation location = App.getLocation();
                            if (location != null) {
                                MyLocationData locationData = new MyLocationData.Builder().latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                                mapView.getMap().setMyLocationData(locationData);
                                currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                                currentCity = location.getCity();
                                currentProvince = location.getProvince();
                                currentDistrict = location.getDistrict();
                                currentAddress = location.getAddrStr();

                                onToCurrentLocationClick();
                            }
                        }
                    }
                });


                if(!TextUtils.isEmpty(currentProvince)&&!TextUtils.isEmpty(currentCity)&&!TextUtils.isEmpty(currentDistrict)) {
                    if (currentProvince.equals("北京") || currentProvince.equals("上海") || currentProvince.equals("天津") || currentProvince.equals("重庆") || currentProvince.equals("钓鱼岛")) {
                        mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(currentProvince).districtName(currentCity));
                    } else if (currentProvince.equals("港澳") || currentProvince.equals("台湾")) {
                        mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(currentCity).districtName(currentDistrict));
                    } else {
                        mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(currentProvince + currentCity).districtName(currentDistrict));
                    }
                }
            }

        }


        setupLocation();


    }

    private void playAnima() {
        ObjectAnimator.ofFloat(ding, "translationY", 0f, -150, 0f).setDuration(500).start();
    }

    private void setupLocation() {

        bdLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {

                MyLocationData locationData = new MyLocationData.Builder().latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                mapView.getMap().setMyLocationData(locationData);
                currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                currentCity = location.getCity();
                currentProvince = location.getProvince();
                currentDistrict = location.getDistrict();
                currentAddress = location.getAddrStr();


//                if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
//
//                }else {
//                    if (!zoomOnce) {
//                        onToCurrentLocationClick();
//                        zoomOnce = true;
//                    }
//                }

                App.mLocationClient.unRegisterLocationListener(this);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        };

        App.mLocationClient.registerLocationListener(bdLocationListener);


        mapView.getMap().setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (canEditAddress) {
                    playAnima();
                    LatLng center = mapStatus.bound.getCenter();
                    geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(center));
                }


            }
        });
    }

    private void createMark(String lat, String lon) {
        btnFunction.setVisibility(View.GONE);
        //定义Maker坐标点
        LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ding);
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


    @OnClick(R.id.toCurrentLocation)
    public void onToCurrentLocationClick() {
        mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(currentLatlng, 17));
        address.setText(currentAddress);
        address.setTag(currentLatlng);

    }

    @Override
    public void onFunctionClick() {
        super.onFunctionClick();
        if (TextUtils.isEmpty(address.getText().toString())) {
            T.showShort(getContext(), "未选定位置");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(IntentHelper.KEY_LATLNG, (Parcelable) address.getTag());
        intent.putExtra(IntentHelper.KEY_ADDRESS, address.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
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
        App.mLocationClient.unRegisterLocationListener(bdLocationListener);
    }


    @OnClick(R.id.btn_changePlace)
    public void onChangePlaceBtnClick(){



    }



    private void showDialog(List<Province> provinces){
        if (pickerView==null) {

            final List<String> province = new ArrayList();
            final List<List<String>> city = new ArrayList();


            Gson gson = new Gson();
            for (int i = 0, max = provinces.size(); i < max; i++) {
                Province object = provinces.get(i);
                String name = object.name;
                List cities = object.cities;
                province.add(name);
                city.add(cities);
            }


            OptionsPickerView.OnOptionsSelectListener listener = new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    if (mDistrictSearch == null)
                        mDistrictSearch = DistrictSearch.newInstance();

                    mDistrictSearch.setOnDistrictSearchListener(new OnGetDistricSearchResultListener() {
                        @Override
                        public void onGetDistrictResult(DistrictResult districtResult) {
                            if (districtResult.centerPt != null) {
                                mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(districtResult.centerPt, 17));
                            }
                        }
                    });

                    mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(province.get(options1)).districtName(city.get(options1).get(options2)));

                    pickerView.dismiss();
                }
            };

            pickerView = new OptionsPickerView.Builder(getContext(), listener).build();

            pickerView.setPicker(province, city);

        }
        pickerView.show();
    }
}
