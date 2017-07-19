package com.jyt.baseapp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FragmentViewPagerAdapter;
import com.jyt.baseapp.view.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class OrderFragment extends BaseFragment {
    @BindView(R.id.rbtn_pickup)
    RadioButton rbtnPickup;
    @BindView(R.id.rbtn_send)
    RadioButton rbtnSend;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;


    private static OrderPickUpFragment pickUpFragment;
    private static OrderSendFragment sendFragment;
    FragmentViewPagerAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragmen_order;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pickUpFragment = new OrderPickUpFragment();
        sendFragment = new OrderSendFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(pickUpFragment);
        fragments.add(sendFragment);

        viewPager.setAdapter(adapter = new FragmentViewPagerAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        adapter.setFragments(fragments);
        adapter.notifyDataSetChanged();


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtn_pickup){
                    viewPager.setCurrentItem(0,false);
                }else if(checkedId==R.id.rbtn_send){
                    viewPager.setCurrentItem(1,false);
                }
            }
        });
    }


    public static OrderPickUpFragment getPickUpFragment() {
        return pickUpFragment;
    }

    public static OrderSendFragment getSendFragment() {
        return sendFragment;
    }
}
