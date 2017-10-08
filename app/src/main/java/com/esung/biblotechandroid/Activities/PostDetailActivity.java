package com.esung.biblotechandroid.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.esung.biblotechandroid.Network.GsonConverters.PostContent;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;
import com.esung.biblotechandroid.Utility.SharedPrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.esung.biblotechandroid.Utility.IntentTag.POST_CONTENT;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_DETAIL_ACTIVITY;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_ID;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_TITLE;
import static com.esung.biblotechandroid.Utility.IntentTag.PREVIOUS_ACTIVITY;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_INFO;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_NAME;


public class PostDetailActivity extends AppCompatActivity {

    //    private PostSearchResults mItem;
    private NodeJsService mNodeJsService;

    private Toolbar mToolbar;
    private TextView mPostContentView;
    private FloatingActionButton mEditButton;
    private ActionBar mActionBar;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        mPostContentView = (TextView) findViewById(R.id.tv_postContent);

        postId = getIntent().getIntExtra(POST_ID, -1);
        mNodeJsService = NodeJsApi.getInstance(getApplicationContext()).getService();
        fillInContent();

        mEditButton = (FloatingActionButton) findViewById(R.id.float_edit);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start write activity
                Intent intent = new Intent(getApplicationContext(), WritePostActivity.class);
                intent.putExtra(POST_ID, postId);
                intent.putExtra(POST_TITLE, mActionBar.getTitle().toString());
                intent.putExtra(POST_CONTENT, mPostContentView.getText().toString());
                intent.putExtra(PREVIOUS_ACTIVITY, POST_DETAIL_ACTIVITY);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        fillInContent();
        super.onRestart();
    }

    private void fillInContent() {
        Call<PostContent> fetchPostContent = mNodeJsService.getEntryOfBookReviewBy(postId);
        fetchPostContent.enqueue(new Callback<PostContent>() {
            @Override
            public void onResponse(Call<PostContent> call, Response<PostContent> response) {
                PostContent postContent = response.body();
                if (postContent == null) {
                    Context context = PostDetailActivity.this;
                    NodeJsApi.handleServerError(context);
                } else {

                    //Setup PostTitle on ActionBar
                    setSupportActionBar(mToolbar);
                    if (mActionBar == null) {
                        mActionBar = getSupportActionBar();
                    }
                    String postTitle = postContent.getPostTitle();
                    mActionBar.setTitle(postTitle);

                    //Setup PostContent on mPostContentView
                    mPostContentView.setText(postContent.getPostContent());

                    //Show "edit"Button if user and writer is same person
                    SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                    String userName = sharedPreferences.getString(USER_NAME, null);
                    if (userName == null) {
                        Context context = PostDetailActivity.this;
                        SharedPrefUtil.handleError(context);
                    }
                    String writerName = postContent.getWriter();
                    if (userName.equals(writerName)) {
                        mEditButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostContent> call, Throwable t) {
                t.printStackTrace();
                Context context = PostDetailActivity.this;
                NodeJsApi.handleServerError(context);

            }
        });
    }
}
