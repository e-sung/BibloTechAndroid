package com.esung.biblotechandroid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.esung.biblotechandroid.Network.GsonConverters.SignUpValidity;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.esung.biblotechandroid.Utility.IntentTag.SIGNED_UP_EMAIL;

public class SignUpActivity extends AppCompatActivity {

    private EditText userNameView;
    private EditText phoneNumberView;
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordConfirmView;
    //<--------------------UI Resources ------------------

    //<-------------------Network Resources --------------
    private NodeJsService mNodeJsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userNameView = (EditText) findViewById(R.id.etv_userNameView);
        phoneNumberView = (EditText) findViewById(R.id.etv_phoneNumberView);
        emailView = (EditText) findViewById(R.id.etv_emailView);
        passwordView = (EditText) findViewById(R.id.etv_passwordView);
        passwordConfirmView = (EditText) findViewById(R.id.etv_passwordConfirmView);
        mNodeJsService = NodeJsApi.getInstance(getApplicationContext()).getService();
    }

    public void submit(View view) {
        String userName = userNameView.getText().toString();
        String phoneNumber = phoneNumberView.getText().toString();
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirm = passwordConfirmView.getText().toString();

        Call<SignUpValidity> call = mNodeJsService.signUpSubmit(
                userName, phoneNumber, email, password, passwordConfirm
        );

        call.enqueue(new Callback<SignUpValidity>() {
            @Override
            public void onResponse(Call<SignUpValidity> call, Response<SignUpValidity> response) {

                SignUpValidity validity = response.body();
                if (validity == null) {
                    NodeJsApi.handleServerError(getApplicationContext());
                } else {
                    if (validity.getEmailValidity().length() > 0) {
                        emailView.requestFocus();
                        emailView.setError(validity.getEmailValidity());
                    } else if (validity.getPasswordConfirmValidity().length() > 0) {
                        passwordConfirmView.requestFocus();
                        passwordConfirmView.setError(validity.getPasswordConfirmValidity());
                    } else if (validity.getPasswordValidity().length() > 0) {
                        passwordView.setError(validity.getPasswordValidity());
                        passwordView.requestFocus();
                    } else if (validity.getPhoneNumberValidity().length() > 0) {
                        phoneNumberView.setError(validity.getPhoneNumberValidity());
                        passwordView.requestFocus();
                    } else if (validity.getUserNameValidity().length() > 0) {
                        userNameView.setError(validity.getUserNameValidity());
                        userNameView.requestFocus();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        intent.putExtra(SIGNED_UP_EMAIL, emailView.getText().toString());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpValidity> call, Throwable t) {
                t.printStackTrace();
                NodeJsApi.handleServerError(getApplicationContext());
                finish();
            }
        });

    }

}
