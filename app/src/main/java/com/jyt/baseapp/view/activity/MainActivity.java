package com.jyt.baseapp.view.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.FragmentViewPagerAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.OrderMessage;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.L;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.dialog.RobOrderDialog;
import com.jyt.baseapp.view.dialog.SelLocationDialog;
import com.jyt.baseapp.view.fragment.HomeFragment;
import com.jyt.baseapp.view.fragment.MessageBoxFragment;
import com.jyt.baseapp.view.fragment.OrderFragment;
import com.jyt.baseapp.view.fragment.OrderPickUpFragment;
import com.jyt.baseapp.view.fragment.OrderSendFragment;
import com.jyt.baseapp.view.fragment.PersonalCenterFragment;
import com.jyt.baseapp.view.widget.NoScrollViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by chenweiqi on 2017/5/31.
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.img_shoueye)
    ImageView imgShoueye;
    @BindView(R.id.text_shouye)
    TextView textShouye;
    @BindView(R.id.layout_shouye)
    LinearLayout layoutShouye;
    @BindView(R.id.img_dingdan)
    ImageView imgDingdan;
    @BindView(R.id.text_dingdan)
    TextView textDingdan;
    @BindView(R.id.layout_dingdan)
    LinearLayout layoutDingdan;
    @BindView(R.id.img_xiaoxi)
    ImageView imgXiaoxi;
    @BindView(R.id.text_xiaoxi)
    TextView textXiaoxi;
    @BindView(R.id.layout_xiaoxi)
    LinearLayout layoutXiaoxi;
    @BindView(R.id.img_geren)
    ImageView imgGeren;
    @BindView(R.id.text_geren)
    TextView textGeren;
    @BindView(R.id.layout_geren)
    LinearLayout layoutGeren;
    @BindView(R.id.text_msgCount)
    public TextView textMsgCount;

    RobOrderDialog robOrderDialog;
    ChangePageReceiver changePageReceiver;
    Integer[] selImgId = {R.mipmap.common_tab_home_s,R.mipmap.common_tab_order_s,R.mipmap.common_tab_msg_s,R.mipmap.common_tab_mine_s};
    Integer[] norImgId = {R.mipmap.common_tab_home_n,R.mipmap.common_tab_order_n,R.mipmap.common_tab_msg_n,R.mipmap.common_tab_mine_n};
    ImageView[] img ;
    TextView[] text ;

    public static int startPage = -1;

    FragmentViewPagerAdapter adapter;
    CustomMsgReceiver talkReceiver;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setBaseActionBarVisible(false);

        img = new ImageView[]{imgShoueye,imgDingdan,imgXiaoxi,imgGeren};
        text = new TextView[]{textShouye,textDingdan,textXiaoxi,textGeren};

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<img.length;i++){
                    img[i].setImageDrawable(getResources().getDrawable(norImgId[i]));
                    text[i].setTextColor(getResources().getColor(R.color.disableColor));
                }

                img[position].setImageDrawable(getResources().getDrawable(selImgId[position]));
                text[position].setTextColor(getResources().getColor(R.color.enableColor));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter = new FragmentViewPagerAdapter(getSupportFragmentManager()));
        adapter.setFragments(getFragments());
        adapter.notifyDataSetChanged();

        talkReceiver = new CustomMsgReceiver();
        IntentFilter intentFilter = new IntentFilter(JPushInterface.ACTION_NOTIFICATION_RECEIVED);
        intentFilter.addCategory(getPackageName());
        registerReceiver(talkReceiver,intentFilter);

        changePageReceiver = new ChangePageReceiver();
        registerReceiver(changePageReceiver,new IntentFilter("ACTION_CHANGE_PAGE"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (startPage!=-1){
            viewPager.setCurrentItem(startPage,false);

//            if (startPage==1){
//                OrderFragment orderFragment = (OrderFragment) adapter.getFragments().get(1);
//                if (orderFragment!=null) {
//                    OrderPickUpFragment pickUpFragment = ((OrderFragment) orderFragment).getPickUpFragment();
//                    OrderSendFragment sendFragment = ((OrderFragment) orderFragment).getSendFragment();
//
//                    try {
//                        if (pickUpFragment != null) {
//                            pickUpFragment.refresh();
//                        }
//                    } catch (Exception ex) {
//
//                    }
//                    try {
//                        if (sendFragment != null) {
//                            sendFragment.refresh();
//                        }
//                    } catch (Exception ex) {
//
//                    }
//                }
//            }

            startPage = -1;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(talkReceiver);
        unregisterReceiver(changePageReceiver);
    }

    @OnClick({R.id.layout_shouye,R.id.layout_dingdan,R.id.layout_xiaoxi,R.id.layout_geren})
    public void onBottomBarClick(View view){
        switch (view.getId()){
            case R.id.layout_shouye:
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.layout_dingdan:
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.layout_xiaoxi:
                viewPager.setCurrentItem(2,false);
                break;
            case R.id.layout_geren:
                viewPager.setCurrentItem(3,false);
                break;
        }
    }


    private List<Fragment> getFragments(){
        List<Fragment> fragments = new ArrayList<>();

        HomeFragment homeFragment = new HomeFragment();
        fragments.add(homeFragment);

        OrderFragment orderFragment = new OrderFragment();
        fragments.add(orderFragment);

        MessageBoxFragment messageBoxFragment = new MessageBoxFragment();
        fragments.add(messageBoxFragment);

        PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        fragments.add(personalCenterFragment);

        return fragments;
    }

    public void changePage(int page){
        viewPager.setCurrentItem(page,false);
    }

    public void toPickUpPage(){
        viewPager.setCurrentItem(1,false);
//        OrderFragment orderFragment = (OrderFragment) adapter.getFragments().get(1);


    }



    public class CustomMsgReceiver extends BroadcastReceiver {
        private static final String TAG = "CustomMsgReceiver";

        private NotificationManager nm;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == nm) {
                nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            Bundle bundle = intent.getExtras();
            Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + Arrays.toString(bundle.keySet().toArray()));
            if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "接受到推送下来的自定义消息");
                // Push Talk messages are push down by custom message format
                processCustomMessage(context, bundle);
            }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
                receivingNotification(context,bundle);
            }
        }

        private void processCustomMessage(Context context, Bundle bundle) {
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        }

        private void receivingNotification(Context context, Bundle bundle){
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            L.e(TAG, " title : " + title);
            String message = bundle.getString(JPushInterface.EXTRA_ALERT);
            L.e(TAG, "message : " + message);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            L.e(TAG, "extras : " + extras);

            try {
                JSONObject jsonObject = new JSONObject(extras);

                String type = jsonObject.getString("type");
                if (TextUtils.isEmpty(type)){
                    return;
                }
                if (type.equals("1")
                        ||type.equals("2")){//抢单提示

                    final OrderMessage data = new OrderMessage();
                    data.htmlStr = jsonObject.optString("htmlStr");
                    data.messageId = jsonObject.optString("messageId");
                    data.endTime = jsonObject.optString("endTime");
                    data.msgType = type;

                    try {
                        if (robOrderDialog!=null){
                            robOrderDialog.dismiss();
                        }
                        robOrderDialog = new RobOrderDialog(getContext());
                        robOrderDialog.setMessage((OrderMessage) data);
                        robOrderDialog.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class ChangePageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            final int page = intent.getIntExtra("page",0);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changePage(page);
                }
            });
        }
    }
}
