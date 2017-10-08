package com.esung.biblotechandroid.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.esung.biblotechandroid.Network.GsonConverters.PostInfo;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;
import com.esung.biblotechandroid.RecyclerAdapters.PostListAdapter;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.esung.biblotechandroid.Utility.IntentTag.BOOK_TITLE;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_LIST_ACTIVITY;
import static com.esung.biblotechandroid.Utility.IntentTag.PREVIOUS_ACTIVITY;

/**
 * An activity representing a list of Posts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PostDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PostListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private RecyclerView mRecyclerView;
    private NodeJsService mNodeJsService;
    private String mBookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        //Set ActionBar's title with "Post on BookTitle" format
        mBookTitle = getIntent().getStringExtra(BOOK_TITLE);
        getSupportActionBar().setTitle("Post on " + getIntent().getStringExtra(BOOK_TITLE));

        FloatingActionButton writeNewPost = (FloatingActionButton) findViewById(R.id.fab);
        writeNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WritePostActivity.class);
                intent.putExtra(BOOK_TITLE, mBookTitle);
                //WritePostActivity is curious about
                //whether we are coming from PostListActivity or PostDetailActivity
                intent.putExtra(PREVIOUS_ACTIVITY, POST_LIST_ACTIVITY);
                startActivity(intent);
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_post_list);
        if (findViewById(R.id.post_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mNodeJsService = NodeJsApi.getInstance(getApplicationContext()).getService();
        fillInRecyclerView();
    }

    @Override
    protected void onRestart() {
        fillInRecyclerView();
        super.onRestart();
    }

    private void fillInRecyclerView() {
        Call<List<PostInfo>> fetchPostList = mNodeJsService.getPostListAboutBook(mBookTitle);
        fetchPostList.enqueue(new Callback<List<PostInfo>>() {
            @Override
            public void onResponse(Call<List<PostInfo>> call, Response<List<PostInfo>> response) {
                List<PostInfo> postInfoList = response.body();
                if (postInfoList == null) {
                    NodeJsApi.handleServerError(getApplicationContext());
                } else {
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    Collections.reverse(postInfoList);
                    mRecyclerView.setAdapter(new PostListAdapter(postInfoList, mTwoPane));
                }
            }

            @Override
            public void onFailure(Call<List<PostInfo>> call, Throwable t) {
                t.printStackTrace();
                NodeJsApi.handleServerError(getApplicationContext());
            }
        });
    }
}
