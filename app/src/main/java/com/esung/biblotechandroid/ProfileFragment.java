package com.esung.biblotechandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esung.biblotechandroid.Network.GsonConverters.UserInfo;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.Utility.MD5Util;
import com.esung.biblotechandroid.Utility.SharedPrefUtil;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    private UserInfo mUserInfo;

    private ImageView mGravatarView;
    private TextView mRentScoreView;
    private TextView mRentableBooksView;

    private NodeJsService nodeJsService;
    private SharedPreferences sharedPref;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(UserInfo userInfo) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("userInfo", userInfo);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        mUserInfo = getArguments().getParcelable("userInfo");
        nodeJsService = NodeJsApi.getInstance().getService();
        sharedPref = getContext().getSharedPreferences(SharedPrefUtil.USER_INFO, Context.MODE_PRIVATE);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mGravatarView = (ImageView) view.findViewById(R.id.iv_gravatar_view);
        mRentScoreView = (TextView) view.findViewById(R.id.tv_rentScore);
        mRentableBooksView = (TextView) view.findViewById(R.id.tv_rentableBooks);

        setUpUi();


        return view;
    }

    private void setUpUi() {
        if (mUserInfo != null) {
            fillInUserInfo(mUserInfo);
        } else {
            Call<UserInfo> fetchCall = nodeJsService.fetchUserInfo(sharedPref.getString(SharedPrefUtil.USER_EMAIL, null));
            fetchCall.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    mUserInfo = response.body();
                    fillInUserInfo(mUserInfo);
                }
                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void fillInUserInfo(UserInfo userInfo) {
        mRentScoreView.setText(String.valueOf(userInfo.getRentscore()));
        mRentableBooksView.setText(String.valueOf(userInfo.getRentableBooks()));
        String gravatarHash = MD5Util.md5Hex(sharedPref.getString(SharedPrefUtil.USER_EMAIL, null));
        String gravatarUrl = "http://www.gravatar.com/avatar/" + gravatarHash + "?size=200";
        Picasso.with(getActivity())
                .load(gravatarUrl)
                .into(mGravatarView);
    }

    @Override
    public void onResume() {
        setUpUi();
        super.onResume();
    }

    public void updateFigures(final int newRentScore, int newRentableBooks) {
        Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(600);
                while (Integer.parseInt(mRentScoreView.getText().toString()) < newRentScore - 1) {
                    SystemClock.sleep(50);
                    int oldRentscore = Integer.parseInt(mRentScoreView.getText().toString());
                    oldRentscore += 1;
                    final int finalOldRentscore = oldRentscore;
                    mRentScoreView.post(new Runnable() {
                        @Override
                        public void run() {
                            mRentScoreView.setText(String.valueOf(finalOldRentscore));
                        }
                    });
                }
            }
        };
        new Thread(updateUI).start();
        mRentableBooksView.setText(String.valueOf(newRentableBooks));
    }
}
