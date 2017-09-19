package com.example.android.imagedownload;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shanto on 9/17/17.
 */

public class L {

    public static void m(String message){
        Log.w("Test",message);
    }

    public static void s(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
