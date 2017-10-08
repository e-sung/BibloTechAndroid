package com.esung.biblotechandroid.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;
import com.esung.biblotechandroid.Utility.SharedPrefUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.esung.biblotechandroid.Utility.IntentTag.BOOK_TITLE;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_CONTENT;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_ID;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_TITLE;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.AUTH_TOKEN;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_INFO;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_NAME;

public class WritePostActivity extends AppCompatActivity {

    private static final int UPDATE_PHASE = 0;
    private static final int INSERT_PHASE = 1;
    private SharedPreferences sharedPref;
    private EditText mTitleView;
    private EditText mContentView;
    private String mUserName;
    private String mPostTitle;
    private String mPostContent;
    private String mBookTitle;
    private int mPostId;
    private int Phase;
    private NodeJsService mNodeJsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        mTitleView = (EditText) findViewById(R.id.etv_postTitle);
        mContentView = (EditText) findViewById(R.id.etv_postContent);

        //Let's see if we are making a new entry of post OR editing existing post
        if (getIntent().getStringExtra(POST_CONTENT) != null) {
            Phase = UPDATE_PHASE;
            mContentView.setText(getIntent().getStringExtra(POST_CONTENT));
            mTitleView.setText(getIntent().getStringExtra(POST_TITLE));
            mPostId = getIntent().getIntExtra(POST_ID, -1);
        } else {
            Phase = INSERT_PHASE;
        }

        sharedPref = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        mNodeJsService = NodeJsApi.getInstance(getApplicationContext()).getService();

        FloatingActionButton submitButton = (FloatingActionButton) findViewById(R.id.fab);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTitleView.getText().toString().isEmpty() == false) {

                    mUserName = sharedPref.getString(USER_NAME, null);
                    if (mUserName == null){SharedPrefUtil.handleError(getApplicationContext());}
                    mPostTitle = mTitleView.getText().toString();
                    mPostContent = mContentView.getText().toString();
                    mBookTitle = getIntent().getStringExtra(BOOK_TITLE);

                    String authToken = sharedPref.getString(AUTH_TOKEN, null);
                    if (authToken == null) SharedPrefUtil.handleError(getApplicationContext());

                    Call<ResponseBody> call;
                    if (Phase == UPDATE_PHASE) {
                        call = mNodeJsService.editEntry(authToken,mPostTitle,mPostContent,mPostId);
                    } else {
                        call = mNodeJsService.postNewEntry(authToken, mBookTitle, mPostTitle, mPostContent, mUserName);
                    }

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Class targetClass = null;
                            switch (Phase){
                                case INSERT_PHASE:
                                    targetClass = PostListActivity.class;
                                    break;
                                case UPDATE_PHASE:
                                    targetClass = PostDetailActivity.class;
                                    break;
                            }
                            Intent intent = new Intent(getApplicationContext(),targetClass);
                            intent.putExtra(POST_ID,mPostId);
                            intent.putExtra(BOOK_TITLE,mBookTitle);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                            NodeJsApi.handleServerError(getApplicationContext());
                        }
                    });
                }
                else{
                    Toast.makeText(WritePostActivity.this, getString(R.string.warning_on_title), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}