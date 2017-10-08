package com.esung.biblotechandroid.Utility;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.esung.biblotechandroid.Activities.SignInActivity;
import com.esung.biblotechandroid.R;


/**
 * Created by sungdoo on 2017-08-20.
 */

public class SharedPrefUtil {
    public static final String USER_INFO = "userInfo";
    public static final String PAGE_TAG = "pageTag";
    public static final String NETWORK = "network";

    public static final String USER_EMAIL = "userEmail";
    public static final String USER_NAME = "userName";
    public static final String AUTH_TOKEN = "authToken";

    public static final String BASE_URL = "baseURL";
    public static final String NOT_SET = "Not set";

    public static void handleError(Context context) {
        String errorMessage = context.getString(R.string.shared_pref_error);
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
