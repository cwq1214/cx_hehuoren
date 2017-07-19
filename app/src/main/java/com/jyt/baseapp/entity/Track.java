package com.jyt.baseapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenweiqi on 2017/6/9.
 */
public class Track implements Parcelable {
    public String msg;//true string 信息
    public String msgTime;
    ;//true 时间

    protected Track(Parcel in) {
        msg = in.readString();
        msgTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeString(msgTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}
