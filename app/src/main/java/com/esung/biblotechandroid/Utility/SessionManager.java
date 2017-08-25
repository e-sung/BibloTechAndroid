package com.esung.biblotechandroid.Utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.esung.biblotechandroid.SignInActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sungdoo on 2017-08-16.
 */

public class SessionManager {
    private Context mContext;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor prefEditor;


    public SessionManager(Context context) {
        sharedPref = context.getSharedPreferences(SharedPrefUtil.USER_INFO, Context.MODE_PRIVATE);
        prefEditor = sharedPref.edit();
        mContext = context;
    }

    public void logout(Context context) {
        sharedPref = context.getSharedPreferences(SharedPrefUtil.USER_INFO, Context.MODE_PRIVATE);
        prefEditor = sharedPref.edit();
        prefEditor.clear();
        prefEditor.commit();
        mContext.startActivity(new Intent(mContext, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
    //TODO : Add function / If phone is not connected to wifi, make sure it connect to wifi;
//        Context context = getApplicationContext();
//        ConnectivityManager cm =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
//        if(isWifi == false){
//            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//            wifiManager.setWifiEnabled(true);
//        }


}
