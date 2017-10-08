package com.esung.biblotechandroid.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.esung.biblotechandroid.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.BASE_URL;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.NETWORK;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.NOT_SET;

/**
 * Created by sungdoo on 2017-08-18.
 */

public class NodeJsApi {

    public static NodeJsApi instance = null;
    private NodeJsService mService;

    private NodeJsApi(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(NETWORK, MODE_PRIVATE);
        String baseUrl = sharedPref.getString(BASE_URL, NOT_SET);
        Toast.makeText(context, baseUrl, Toast.LENGTH_SHORT).show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
        this.mService = retrofit.create(NodeJsService.class);
    }

    public static NodeJsApi getInstance(Context context) {
        if (instance == null) {
            instance = new NodeJsApi(context);
        }
        return instance;
    }

    public static void refreshInstance(Context context) {
        instance = new NodeJsApi(context);
    }

    public static void handleServerError(Context context) {
        String errorMessage = context.getString(R.string.server_side_error);
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public NodeJsService getService() {
        return this.mService;
    }


}
