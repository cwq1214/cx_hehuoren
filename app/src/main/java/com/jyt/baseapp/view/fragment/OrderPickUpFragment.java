package com.jyt.baseapp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FragmentViewPagerAdapter;
import com.jyt.baseapp.view.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class OrderPickUpFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;


    FragmentViewPagerAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_pickup_send;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager.setAdapter(adapter = new FragmentViewPagerAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        adapter.setFragments(getFragments());
        adapter.setTitles(Arrays.asList(new String[]{"待收件","进行中","已完成","已取消"}));
        adapter.notifyDataSetChanged();

        tabLayout.setupWithViewPager(viewPager);
    }

    public void refresh(){
        for (Fragment f:adapter.getFragments()) {
            ((OrderListFragment) f).refresh();
        }
    }

    private List<Fragment> getFragments(){
        List<Fragment> fragments = new ArrayList<>();
        OrderListFragment daishou = new OrderListFragment();
        daishou.setType(OrderListFragment.TYPE_PICKUP_READY);
        fragments.add(daishou);

        OrderListFragment jinxing = new OrderListFragment();
        jinxing.setType(OrderListFragment.TYPE_PICKUP_DOING);
        fragments.add(jinxing);

        OrderListFragment wancheng = new OrderListFragment();
        wancheng.setType(OrderListFragment.TYPE_PICKUP_FINISH);
        fragments.add(wancheng);

        OrderListFragment quxiao = new OrderListFragment();
        quxiao.setType(OrderListFragment.TYPE_PICKUP_CANCEL);
        fragments.add(quxiao);
        return fragments;
    }
}
