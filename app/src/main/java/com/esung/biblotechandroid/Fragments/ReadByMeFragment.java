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

import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;
import com.esung.biblotechandroid.RecyclerAdapters.ReadBooksListAdapter;
import com.esung.biblotechandroid.Utility.SharedPrefUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_EMAIL;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_INFO;


public class ReadByMeFragment extends Fragment {

    private NodeJsService mNodeJsService;
    private RecyclerView mRecyclerView;

    private View fragmentView;
    private TextView mPageTitleView;

    public ReadByMeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_read_by_me, container, false);
        mPageTitleView = (TextView) fragmentView.findViewById(R.id.tv_pageTitle);

        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPageTitleView.setVisibility(View.GONE);
        }


        mNodeJsService = NodeJsApi.getInstance(getContext()).getService();
        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String userEmail = sharedPref.getString(USER_EMAIL, null);
        if (userEmail == null) {
            SharedPrefUtil.handleError(getContext());
        }
        Call<ResponseBody> fetchContent = mNodeJsService.getBooksReadBy(userEmail);
        fetchContent.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseContent = null;
                try {
                    responseContent = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    NodeJsApi.handleServerError(getContext());
                }
                responseContent = responseContent.split(":")[1];
                if (responseContent.equals("\";\"}") == false) {
                    responseContent = responseContent.substring(1, responseContent.length() - 4);
                    String[] resultArray = responseContent.split(";");

                    mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_readBooksList);
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mRecyclerView.setAdapter(new ReadBooksListAdapter(resultArray));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        return fragmentView;
    }

}
