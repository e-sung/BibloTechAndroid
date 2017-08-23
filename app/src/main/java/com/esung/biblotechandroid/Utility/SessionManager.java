package com.esung.biblotechandroid.Utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
        sharedPref = context.getSharedPreferences(SharedPrefUtil.USER_INFO,Context.MODE_PRIVATE);
        prefEditor = sharedPref.edit();
        mContext = context;
    }

    public void logout(Context context) {
        sharedPref = context.getSharedPreferences(SharedPrefUtil.USER_INFO,Context.MODE_PRIVATE);
        prefEditor = sharedPref.edit();
        prefEditor.clear();
        prefEditor.commit();
        mContext.startActivity(new Intent(mContext, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


}
