package com.jyt.baseapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenweiqi on 2017/6/13.
 */

public class BankCard implements Parcelable{
    public String bankId ;//true string id
    public String bankNumber;// true string 卡号
    public String openBank;// true string 开户行
    public String name;// true string 开户人姓名

    public boolean checked = false;
    public boolean canChecked = false;

    protected BankCard(Parcel in) {
        bankId = in.readString();
        bankNumber = in.readString();
        openBank = in.readString();
        name = in.readString();
        checked = in.readByte() != 0;
        canChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankId);
        dest.writeString(bankNumber);
        dest.writeString(openBank);
        dest.writeString(name);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeByte((byte) (canChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BankCard> CREATOR = new Creator<BankCard>() {
        @Override
        public BankCard createFromParcel(Parcel in) {
            return new BankCard(in);
        }

        @Override
        public BankCard[] newArray(int size) {
            return new BankCard[size];
        }
    };
}
