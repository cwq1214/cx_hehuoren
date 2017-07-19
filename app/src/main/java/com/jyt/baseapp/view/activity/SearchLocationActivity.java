package com.jyt.baseapp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.AddressRcvAdapter;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.view.dialog.InputDialog;
import com.jyt.baseapp.view.viewholder.AddressViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

/**
 * Created by v7 on 2016/12/24.
 */
public class SearchLocationActivity extends BaseActivity {



    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.centerMessage)
//    TextView centerMessage;


    AddressRcvAdapter addressRcvAdapter;
    PoiSearch poiSearch;
    LatLng currentLatlng;
    String keyword;
    String city;
    int pageNum = 0;



    @Override
    public int getLayoutId() {
        return R.layout.activity_search_location;
    }

    @Override
    protected View getContentView() {
        return null;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        city = App.getLocation().getCity();
        if (TextUtils.isEmpty(city)){
            btnFunction.setText("点击输入城市");
        }else {
            btnFunction.setText(city);
        }

        addressRcvAdapter = new AddressRcvAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(addressRcvAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_rcv));
        recyclerView.addItemDecoration(dividerItemDecoration);
        currentLatlng = getIntent().getParcelableExtra(IntentHelper.KEY_LATLNG);
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
//                centerMessage.setVisibility(poiResult==null?View.VISIBLE:View.GONE);
                if (poiResult!=null){
                    if (pageNum!=0) {
                        addressRcvAdapter.getAddress().addAll(poiResult.getAllPoi());
                    }else {
                        addressRcvAdapter.setAddress(poiResult.getAllPoi());

                    }
                    addressRcvAdapter.notifyDataSetChanged();
                }

                swipeRefreshLayout.finishLoadmore();
                swipeRefreshLayout.finishRefreshing();
                pageNum ++;
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
                if (s.length()!=0)
                searchPoi(0);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                searchPoi(0);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
//                refreshLayout.finishLoadmore();
                searchPoi(pageNum);
            }
        });

        addressRcvAdapter.setOnAddressClickListener(new AddressViewHolder.OnAddressClickListener() {
            @Override
            public void onClick(PoiInfo info, int index) {
                Intent intent = new Intent();
                intent.putExtra(IntentHelper.KEY_POIINFO,info);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private void searchPoi(int pageNum){
        if (pageNum ==0 )
            this.pageNum = 0;
//        poiSearch.searchNearby(new PoiNearbySearchOption().location(currentLatlng).keyword(keyword).pageCapacity(50).pageNum(pageNum).radius(20000000).sortType(PoiSortType.comprehensive));

//        LatLngBounds latLngBounds = new LatLngBounds.Builder().include(currentLatlng).build();
//        poiSearch.searchInBound(new PoiBoundSearchOption().bound(latLngBounds).keyword(keyword).pageNum(pageNum));

        poiSearch.searchInCity(new PoiCitySearchOption().city(city).keyword(keyword).isReturnAddr(true).pageNum(pageNum));
    }

    @Override
    public void onFunctionClick() {
        super.onFunctionClick();
        InputDialog inputDialog = new InputDialog(getContext());
        inputDialog.setInputSingleLine(true);
        inputDialog.setTitle("请输入城市");
        inputDialog.setOnDoneClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = ((InputDialog) dialog).getInputContent();
                SearchLocationActivity.this.city = city;
                btnFunction.setText(city);
                if (!TextUtils.isEmpty(keyword)) {
                    searchPoi(0);
                }
                dialog.dismiss();
            }
        });
        inputDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        poiSearch.destroy();
    }
}
