package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.view.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by chenweiqi on 2017/6/6.
 */

public class GuidePageActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    BGABanner banner;
    @BindView(R.id.btn_done)
    ImageView btnDone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide_page;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseActionBarVisible(false);


        final int[] id ={R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3};

        List<View> list = new ArrayList<>();
        for (int i=0;i<id.length;i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setImageDrawable(getResources().getDrawable(id[i]));

            list.add(imageView);
        }

        banner.setAutoPlayAble(false);
        banner.setData(list);


        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==id.length-1){
                    btnDone.setVisibility(View.VISIBLE);
                }else {
                    btnDone.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.btn_done)
    public void onBtnDoneClick(){
        Cache.getInstance().setFirstOpen(false);
        finish();
    }

}
