package com.esung.biblotechandroid.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esung.biblotechandroid.Activities.WritePostActivity;
import com.esung.biblotechandroid.Network.GsonConverters.PostContent;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.esung.biblotechandroid.Utility.IntentTag.BOOK_TITLE;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_CONTENT;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_ID;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_LIST_ACTIVITY;
import static com.esung.biblotechandroid.Utility.IntentTag.POST_TITLE;
import static com.esung.biblotechandroid.Utility.IntentTag.PREVIOUS_ACTIVITY;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_INFO;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_NAME;


public class PostDetailFragment extends Fragment {

    private int mPostId;
    private String mPostTitle;


    private String mBookTitle;

    private NodeJsService mNodeJsService;

    private View rootView;
    private TextView mContentView;
    private FloatingActionButton mEditButton;

    public static PostDetailFragment newInstance(int postId, String postTitle, String bookTitle) {
        PostDetailFragment fragment = new PostDetailFragment();
        fragment.setPostId(postId);
        fragment.setPostTitle(postTitle);
        fragment.setBookTitle(bookTitle);
        return fragment;
    }

    public PostDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initiate Views
        rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        mContentView = (TextView) rootView.findViewById(R.id.tv_postContent);
        mEditButton = (FloatingActionButton) rootView.findViewById(R.id.float_edit);

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start write activity
                Intent intent = new Intent(rootView.getContext(), WritePostActivity.class);
                intent.putExtra(POST_ID, mPostId);
                intent.putExtra(BOOK_TITLE, mBookTitle);
                intent.putExtra(POST_TITLE, mPostTitle);
                intent.putExtra(POST_CONTENT, mContentView.getText().toString());
                intent.putExtra(PREVIOUS_ACTIVITY, POST_LIST_ACTIVITY);
                startActivity(intent);
            }
        });
        fillInContent();
        return rootView;
    }

    @Override
    public void onResume() {
        fillInContent();
        super.onResume();
    }

    private void fillInContent() {
        mNodeJsService = NodeJsApi.getInstance(getContext()).getService();
        Call<PostContent> fetchPostContent = mNodeJsService.fetchPostContent(mPostId);
        fetchPostContent.enqueue(new Callback<PostContent>() {
            @Override
            public void onResponse(Call<PostContent> call, Response<PostContent> response) {
                String content = response.body().getPostContent();
                if (content == null) {
                    NodeJsApi.handleServerError(getContext());
                } else {
                    mContentView.setText(content);

                    //Show "edit"Button if user and writer is same person
                    SharedPreferences sp = getContext().
                            getSharedPreferences(USER_INFO, MODE_PRIVATE);
                    String userName = sp.getString(USER_NAME, "Invalid UserName");
                    String writerName = response.body().getWriter();
                    if (userName.equals(writerName)) {
                        mEditButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostContent> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setPostId(int mPostId) {
        this.mPostId = mPostId;
    }

    public void setPostTitle(String mPostTitle) {
        this.mPostTitle = mPostTitle;
    }

    public void setBookTitle(String mBookTitle) {
        this.mBookTitle = mBookTitle;
    }
}

