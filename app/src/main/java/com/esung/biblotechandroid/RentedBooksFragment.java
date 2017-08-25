package com.esung.biblotechandroid;


import android.content.Context;
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

import com.esung.biblotechandroid.Network.GsonConverters.RentedBooks;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.RecyclerAdapters.RentedBooksListAdapter;
import com.esung.biblotechandroid.Utility.SharedPrefUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_EMAIL;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_INFO;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RentedBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RentedBooksFragment extends Fragment {

    private View fragmentView;
    private TextView mPageTitleView;
    private NodeJsService mNodeJsService;
    private RecyclerView mRecyclerView;

    public RentedBooksFragment() {
        // Required empty public constructor
    }

    public static RentedBooksFragment newInstance(String param1, String param2) {
        RentedBooksFragment fragment = new RentedBooksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_rented_books, container, false);
        mPageTitleView = (TextView) fragmentView.findViewById(R.id.tv_pageTitle);
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPageTitleView.setVisibility(View.GONE);
        }
        mNodeJsService = NodeJsApi.getInstance().getService();

        SharedPreferences sharedPref = getContext().getSharedPreferences(USER_INFO,MODE_PRIVATE);
        String userEmail = sharedPref.getString(USER_EMAIL,null);
        if(userEmail == null){
            SharedPrefUtil.handleError(getContext());
        }
        Call<List<RentedBooks>> fetchRentedBooks = mNodeJsService.fetchRentedBy(userEmail);
        fetchRentedBooks.enqueue(new Callback<List<RentedBooks>>() {
            @Override
            public void onResponse(Call<List<RentedBooks>> call, Response<List<RentedBooks>> response) {
                List<RentedBooks> rentedBooks = response.body();
                if (rentedBooks == null) {
                    NodeJsApi.handleServerError(getContext());
                } else {
                    mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_rentedBooksList);
                    mRecyclerView.setAdapter(new RentedBooksListAdapter(rentedBooks));
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onFailure(Call<List<RentedBooks>> call, Throwable t) {
                NodeJsApi.handleServerError(getContext());
            }
        });

        return fragmentView;

    }

}
