package com.esung.biblotechandroid.Fragments;


import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esung.biblotechandroid.Network.GsonConverters.PostInfo;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;
import com.esung.biblotechandroid.RecyclerAdapters.PostIWroteListAdapter;
import com.esung.biblotechandroid.Utility.SharedPrefUtil;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_INFO;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_NAME;


public class PostIwroteFragment extends Fragment {

    private NodeJsService mNodeJsService;
    private TextView mPageTitleView;
    private RecyclerView mRecyclerView;
    private View fragmentView;

    public PostIwroteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_post_iwrote, container, false);
        mPageTitleView = (TextView) fragmentView.findViewById(R.id.tv_pageTitle);

        //If screen orientation is portrait, mPageTitleView should be invisible
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPageTitleView.setVisibility(View.GONE);
        }

        mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_postIwroteList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mNodeJsService = NodeJsApi.getInstance(getContext()).getService();
        fillInContent();
        return fragmentView;
    }

    @Override
    public void onResume() {
        fillInContent();
        super.onResume();
    }

    private void fillInContent() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String userName = sharedPref.getString(USER_NAME, "Invalid UserName");
        if (userName == null) {
            SharedPrefUtil.handleError(getContext());
        }
        Call<List<PostInfo>> fetchPostInfoCall = mNodeJsService.fetchPostInfoByUserName(userName);
        fetchPostInfoCall.enqueue(new Callback<List<PostInfo>>() {
            @Override
            public void onResponse(Call<List<PostInfo>> call, Response<List<PostInfo>> response) {
                List<PostInfo> postInfoList = response.body();
                if (postInfoList == null) {
                    NodeJsApi.handleServerError(getContext());
                } else {
                    Collections.reverse(postInfoList);
                    mRecyclerView.setAdapter(new PostIWroteListAdapter(postInfoList));
                }
            }

            @Override
            public void onFailure(Call<List<PostInfo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
