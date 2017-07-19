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

public class OrderSendFragment extends BaseFragment {
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
        viewPager.setOffscreenPageLimit(2);
        adapter.setFragments(getFragments());
        adapter.setTitles(Arrays.asList(new String[]{"派件中","派件失败","已完成"}));
        adapter.notifyDataSetChanged();

        tabLayout.setupWithViewPager(viewPager);

    }

    public void refresh(){
        for (Fragment f:
             adapter.getFragments()) {
            ((OrderListFragment) f).refresh();
        }
    }

    private List<Fragment> getFragments(){
        List<Fragment> fragments = new ArrayList<>();
        OrderListFragment paijianzhong = new OrderListFragment();
        paijianzhong.setType(OrderListFragment.TYPE_SEND_DOING);
        fragments.add(paijianzhong);

        OrderListFragment paijianshibai = new OrderListFragment();
        paijianshibai.setType(OrderListFragment.TYPE_SEND_FAILED);
        fragments.add(paijianshibai);

        OrderListFragment yiwancheng = new OrderListFragment();
        yiwancheng.setType(OrderListFragment.TYPE_SEND_FINISH);
        fragments.add(yiwancheng);

        return fragments;
    }
}
