package com.jyt.baseapp.util;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

/**
 * Created by chenweiqi on 2017/6/6.
 */

public class Cache {

    private static String KEY_TOKEN = "key_token";
    public static final String KEY_PHONE = "key_phone";
    public static final String KEY_USERNAME = "key_username";
    public static final String KEY_AVATAR = "key_avatar";
    public static final String KEY_ID_CARD = "key_id_card";
    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_FIRST_OPEN = "key_first_open";
    public static final String KEY_IDENTIFIED = "KEY_IDENTIFIED";


    private static Cache cache = new Cache();

    public static Cache getInstance(){
        return cache;
    }

    public void init(Context context){
        Hawk.init(context).build();
    }


    public void setToken(String token){
        Hawk.put(KEY_TOKEN,token);
    }

    public String getToken(){
        return Hawk.get(KEY_TOKEN,"");
    }


    public String getPhone(){
        return Hawk.get(KEY_PHONE);
    }

    public void setPhone(String phone){
        Hawk.put(KEY_PHONE,phone);
    }

    public String getUserName(){
        return Hawk.get(KEY_USERNAME);
    }

    public void setUserName(String userName){
        Hawk.put(KEY_USERNAME,userName);
    }

    public String getAvatar(){
        return Hawk.get(KEY_AVATAR);
    }

    public void setAvatar(String avatar){
        Hawk.put(KEY_AVATAR,avatar);
    }

    public String getIDCard(){
        return Hawk.get(KEY_ID_CARD);
    }

    public void setIDCard(String idCard){
        Hawk.put(KEY_ID_CARD,idCard);
    }

    public void setUserId(String userId){
        Hawk.put(KEY_USER_ID,userId);
    }

    public String getUserId(){
        return Hawk.get(KEY_USER_ID);
    }

    public boolean isFirstOpen(){
        return Hawk.get(KEY_FIRST_OPEN,true);
    }
    public void setFirstOpen(boolean firstOpen){
        Hawk.put(KEY_FIRST_OPEN,firstOpen);
    }


    public  String getIdentified() {
        return Hawk.get(KEY_IDENTIFIED,"0");
    }
    public  void setIdentified(String identified){
        Hawk.put(KEY_IDENTIFIED,identified);
    }
}
