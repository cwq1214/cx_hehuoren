package com.jyt.baseapp.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.SelAreaAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.Area;
import com.jyt.baseapp.util.ScreenUtils;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/8/3.
 */

public class SelLocationDialog extends AlertDialog {

    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    SelAreaAdapter adapter;

    String province;
    String city;
    String district;

    int type;

    List<Area> provinces;
    List<Area> cities;
    List<Area> districts;


    OnSelFinishCallback onSelFinishCallback;

    public SelLocationDialog(@NonNull Context context) {
        super(context, R.style.customDialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setWindowAnimations(R.style.AnimBottomInOut);
        getWindow().setLayout(ScreenUtils.getScreenWidth(getContext()), (int) (ScreenUtils.getScreenHeight(getContext()) * 0.5));
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_sel_location);
        ButterKnife.bind(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerview.setAdapter(adapter = new SelAreaAdapter());
        adapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder, Object data, int position) {
                if (type == 0){//设置省

                    province = ((Area) data).name;
                    getCityList(((Area) data).id+"");

                    tabLayout.removeAllTabs();
                    tabLayout.addTab(tabLayout.newTab().setText(province));
                    recyclerview.scrollToPosition(0);
                    type++;
                }else if (type == 1){//设置市

                    city = ((Area) data).areaName;

                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    if (tab==null){
                        tabLayout.addTab(tabLayout.newTab().setText(city));
                    }else{
                        tabLayout.removeTabAt(1);
                        if (tabLayout.getTabCount()==2){
                            tabLayout.removeTabAt(1);
                        }
                        tabLayout.addTab(tabLayout.newTab().setText(city));
                    }
                    tabLayout.getTabAt(1).select();
                    getDistrictList(((Area) data).areaId+"");
                    type++;
                    recyclerview.scrollToPosition(0);
                }else if (type == 2){//设置区

                    district = ((Area) data).areaName;

                    if (onSelFinishCallback!=null)
                        onSelFinishCallback.onSelFinish(province,city,district);
                    dismiss();
                    type=0;
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                if (position==type){
//                    return;
//                }
                if (position==0) {
                    type = 0;
                    adapter.setDataList(provinces);
                    selProvince(tab.getText().toString());
                }else if (position ==1 ){
                    type = 1;
                    adapter.setDataList(cities);
                    selCity(tab.getText().toString());
                }else if (position ==2 ){
                    type = 2;
                    adapter.setDataList(districts);
                    selDistrict(tab.getText().toString());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        if (!TextUtils.isEmpty(province)){
//            tabLayout.addTab(tabLayout.newTab().setText(province));
//        }

        if (TextUtils.isEmpty(province)||TextUtils.isEmpty(city)||TextUtils.isEmpty(district)){
            getProvinceList();
        }else {
            Http.getProvinceCityDistrictList(getContext(), province, city, district, new BeanCallback<HashMap>(getContext()) {
                @Override
                public void onResult(boolean success, HashMap response, Exception e) {
                    super.onResult(success, response, e);
                    provinces = (List<Area>) response.get("province");
                    cities = (List<Area>) response.get("city");
                    districts = (List<Area>) response.get("district");


                    tabLayout.addTab(tabLayout.newTab().setText(province));
                    tabLayout.addTab(tabLayout.newTab().setText(city));
                    tabLayout.addTab(tabLayout.newTab().setText(district));

                    tabLayout.getTabAt(2).select();
                    adapter.setDataList(districts);
                    adapter.notifyDataSetChanged();

                    for (int i=0;i<districts.size();i++){
                        districts.get(i).isSel = false;
                        if (districts.get(i).areaName.equals(district)){
                            districts.get(i).isSel = true;
                            recyclerview.scrollToPosition(i);
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
            });

        }
//
//        if (!TextUtils.isEmpty(city)){
//            tabLayout.addTab(tabLayout.newTab().setText(city));
//        }
//
//        if(!TextUtils.isEmpty(district)){
//            tabLayout.addTab(tabLayout.newTab().setText(district));
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void setSelectLocation(String province, String city, String district) {
        this.province = province;
        this.city = city;
        this.district = district;
    }

    @OnClick(R.id.btn_close)
    public void onCloseClick() {
        dismiss();
    }




    private void getProvinceList() {
        Http.getProvinceList(getContext(), new BeanCallback<String>(getContext(),false) {
            @Override
            public void onResult(boolean success, String response, Exception e) {
                super.onResult(success, response, e);
                if (success) {
                    JSONArray json = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        json = jsonObject.optJSONObject("result").optJSONObject("jingdong_area_province_get_responce").optJSONArray("province_areas");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if (json != null) {
                        provinces = new Gson().fromJson(json.toString(), new TypeToken<List<Area>>() {
                        }.getType());


                        adapter.setDataList(provinces);
                        adapter.notifyDataSetChanged();

                        selProvince(province);


                    }
                }
            }
        });
    }

    private void getCityList(String parentId){
        Http.getCityList(getContext(),parentId,new BeanCallback<String>(getContext(),false) {
            @Override
            public void onResult(boolean success, String response, Exception e) {
                super.onResult(success, response, e);
                if (success) {
                    JSONArray json = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        json = jsonObject.optJSONObject("result").optJSONObject("jingdong_areas_city_get_responce").optJSONObject("baseAreaServiceResponse").optJSONArray("data");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if (json != null) {
                        cities = new Gson().fromJson(json.toString(), new TypeToken<List<Area>>() {
                        }.getType());


                        adapter.setDataList(cities);
                        adapter.notifyDataSetChanged();

                        selCity(city);

                    }
                }
            }
        });
    }

    private void getDistrictList(String parentId){
        Http.getCountryList(getContext(),parentId,new BeanCallback<String>(getContext(),false) {
            @Override
            public void onResult(boolean success, String response, Exception e) {
                super.onResult(success, response, e);
                if (success) {
                    JSONArray json = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        json = jsonObject.optJSONObject("result").optJSONObject("jingdong_areas_county_get_responce").optJSONObject("baseAreaServiceResponse").optJSONArray("data");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if (json != null) {
                        districts= new Gson().fromJson(json.toString(), new TypeToken<List<Area>>() {
                        }.getType());


                        adapter.setDataList(districts);
                        adapter.notifyDataSetChanged();

                        selDistrict(district);

                    }
                }
            }
        });
    }

    private void selProvince(String province){
        if (TextUtils.isEmpty(province)){
            return;
        }
        List<Area> areas = adapter.getDataList();
        if (areas==null){
            return;
        }
        int scrollTo = -1;
        for (int i=0,max = areas.size();i<max;i++){
            areas.get(i).isSel = false;
            if (areas.get(i).name.equals(province)){
                areas.get(i).isSel = true;
                scrollTo = i;
                TabLayout.Tab tab=tabLayout.getTabAt(0);
                if (tab!=null)
                    tab.select();
            }
        }
        adapter.notifyDataSetChanged();
        if (scrollTo != -1){
            recyclerview.scrollToPosition(scrollTo);
        }


//        if (cities==null&&!TextUtils.isEmpty(city)){
//            getCityList(areas.get(scrollTo).id+"");
//        }

    }

    private void selCity(String city){
        if (TextUtils.isEmpty(city)){
            return;
        }
        List<Area> areas = adapter.getDataList();
        if (areas==null){
            return;
        }
        int scrollTo = -1;
        for (int i=0,max = areas.size();i<max;i++){
            areas.get(i).isSel = false;
            if (areas.get(i).areaName.equals(city)){
                areas.get(i).isSel = true;
                scrollTo = i;
                TabLayout.Tab tab=tabLayout.getTabAt(1);
                if (tab!=null)
                    tab.select();
            }
        }
        adapter.notifyDataSetChanged();
        if (scrollTo != -1){
            recyclerview.scrollToPosition(scrollTo);
        }

//        if (districts==null&&!TextUtils.isEmpty(district)){
//            getDistrictList(areas.get(scrollTo).areaId+"");
//        }
    }
    private void selDistrict(String district){
        if (TextUtils.isEmpty(district)){
            return;
        }
        List<Area> areas = adapter.getDataList();
        if (areas==null){
            return;
        }
        int scrollTo = -1;
        for (int i=0,max = areas.size();i<max;i++){
            areas.get(i).isSel = false;
            if (areas.get(i).areaName.equals(district)){
                areas.get(i).isSel = true;
                scrollTo = i;
                TabLayout.Tab tab=tabLayout.getTabAt(2);
                if (tab!=null)
                    tab.select();
            }
        }
        adapter.notifyDataSetChanged();
//        if (scrollTo != -1){
//            recyclerview.scrollToPosition(scrollTo);
//        }
    }

    public void setOnSelFinishCallback(OnSelFinishCallback onSelFinishCallback) {
        this.onSelFinishCallback = onSelFinishCallback;
    }

    public interface OnSelFinishCallback {
        void onSelFinish(String province, String city, String district);
    }
}
