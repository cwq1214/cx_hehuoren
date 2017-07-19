package com.jyt.baseapp.util;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by chenweiqi on 2017/1/6.
 */

public class T {
    public static void showShort(final Context context , final String message){
        if (TextUtils.isEmpty(message)){
            return;
        }
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
