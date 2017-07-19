package com.jyt.baseapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by chenweiqi on 2017/6/7.
 */

public class OrderTrackResult implements Parcelable {
    public String partnerName ;//true string 名称
    public String mobile ;//true string 联系方式
    public List<Track> track;// true array[object] 追踪列表

    public String orderNo;//订单号


    protected OrderTrackResult(Parcel in) {
        partnerName = in.readString();
        mobile = in.readString();
        track = in.createTypedArrayList(Track.CREATOR);
        orderNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partnerName);
        dest.writeString(mobile);
        dest.writeTypedList(track);
        dest.writeString(orderNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderTrackResult> CREATOR = new Creator<OrderTrackResult>() {
        @Override
        public OrderTrackResult createFromParcel(Parcel in) {
            return new OrderTrackResult(in);
        }

        @Override
        public OrderTrackResult[] newArray(int size) {
            return new OrderTrackResult[size];
        }
    };
}
