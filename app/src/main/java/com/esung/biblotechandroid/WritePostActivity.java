package com.esung.biblotechandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.Utility.SharedPrefUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.esung.biblotechandroid.Utility.SharedPrefUtil.AUTHO_TOKEN;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_INFO;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_NAME;

public class WritePostActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;

    private EditText mTitleView;
    private EditText mContentView;

    private String mUserName;
    private String mPostTitle;
    private String mPostContent;
    private String mBookTitle;
    private int mPostId;

    private int Phase;
    private static final int UPDATE_PHASE = 0;
    private static final int INSERT_PHASE = 1;

    private NodeJsService mNodeJsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        mTitleView = (EditText) findViewById(R.id.etv_postTitle);
        mContentView = (EditText) findViewById(R.id.etv_postContent);
        if (getIntent().getStringExtra("postContent") != null) {
            Phase = UPDATE_PHASE;
            mContentView.setText(getIntent().getStringExtra("postContent"));
            mTitleView.setText(getIntent().getStringExtra("postTitle"));
            mPostId = getIntent().getIntExtra("postId", -1);
        } else {
            Phase = INSERT_PHASE;
        }

        sharedPref = getSharedPreferences(USER_INFO, MODE_PRIVATE);

        mNodeJsService = NodeJsApi.getInstance().getService();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTitleView.getText().toString().isEmpty() == false) {

                    mUserName = sharedPref.getString(USER_NAME, null);
                    mPostTitle = mTitleView.getText().toString();
                    mPostContent = mContentView.getText().toString();
                    mBookTitle = getIntent().getStringExtra("bookTitle");

                    Call<ResponseBody> call;
                    if (Phase == UPDATE_PHASE) {
                        String authToken = sharedPref.getString(AUTHO_TOKEN, null);
                        if (authToken == null) {
                            SharedPrefUtil.handleError(getApplicationContext());
                        }
                        call = mNodeJsService.editPost(
                                authToken, mPostTitle, mPostContent, mPostId
                        );
                    } else {
                        call = mNodeJsService.
                                submitPost(mBookTitle, mPostTitle, mPostContent, mUserName);
                    }
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(WritePostActivity.this, getString(R.string.server_side_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
