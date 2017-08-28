package com.esung.biblotechandroid.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esung.biblotechandroid.Fragments.ProfileFragment;
import com.esung.biblotechandroid.Network.GsonConverters.RentalInfoResult;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.esung.biblotechandroid.Utility.IntentTag.BOOK_ID;
import static com.esung.biblotechandroid.Utility.IntentTag.RENT;
import static com.esung.biblotechandroid.Utility.IntentTag.RENTAL_ACTION;
import static com.esung.biblotechandroid.Utility.IntentTag.RETURN;
import static com.esung.biblotechandroid.Utility.IntentTag.USER_INFO;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.AUTH_TOKEN;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_EMAIL;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_NAME;

public class RentalActivity extends AppCompatActivity {

    private Button mButton;

    private int mBookId;
    private boolean mIsRented;

    private SharedPreferences mSharedPref;
    private String mAuthToken;
    private String mUserName;
    private String mUserEmail;

    private NodeJsService mNodeJsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);


        mButton = (Button) findViewById(R.id.bt_rentalButton);
        mBookId = getIntent().getIntExtra(BOOK_ID, -1);
        mSharedPref = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        mNodeJsService = NodeJsApi.getInstance(getApplicationContext()).getService();

        mAuthToken = mSharedPref.getString(AUTH_TOKEN, "");
        mUserName = mSharedPref.getString(USER_NAME, "");
        mUserEmail = mSharedPref.getString(USER_EMAIL, "");

        if (mAuthToken == "") {
            Toast.makeText(this, "Inappropriate Access", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        fragmentTransaction.replace(R.id.profileFragmentContainer, profileFragment);
        fragmentTransaction.commit();


        Call<RentalInfoResult> fetchRentalInfo = mNodeJsService.fetchRentalInfo(mBookId);
        fetchRentalInfo.enqueue(new Callback<RentalInfoResult>() {
            @Override
            public void onResponse(Call<RentalInfoResult> call, Response<RentalInfoResult> response) {
                String mButtonText = "";
                if (response.body().getIsRented() == 1) {//if book is rented
                    mIsRented = true;
                    mButtonText = "Return";
                } else {
                    mIsRented = false;
                    mButtonText = "Rent";
                }
                mButtonText = mButtonText + "\n" + response.body().getTitle();
                mButton.setText(mButtonText);
            }

            @Override
            public void onFailure(Call<RentalInfoResult> call, Throwable t) {
                t.printStackTrace();
                NodeJsApi.handleServerError(getApplicationContext());
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsRented == true) {
                    returnBook();
                } else {
                    rentBook();
                }
            }
        });
    }

    private void rentBook() {
        Call<ResponseBody> rentBookCall = mNodeJsService.
                sendRentRequest(mAuthToken, mBookId, mUserEmail);
        rentBookCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PageListActivity.class);
                intent.putExtra(RENTAL_ACTION, RENT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                NodeJsApi.handleServerError(getApplicationContext());
            }
        });
    }

    private void returnBook() {
        Call<ResponseBody> returnBookCall = mNodeJsService.sendReturnRequest(mBookId, mUserName);
        returnBookCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                NodeJsApi.handleServerError(getApplicationContext());
            }
        });
        Intent intent = new Intent(getApplicationContext(), PageListActivity.class);
        intent.putExtra(RENTAL_ACTION, RETURN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
