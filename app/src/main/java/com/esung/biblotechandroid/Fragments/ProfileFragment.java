package com.esung.biblotechandroid.Fragments;

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

import com.esung.biblotechandroid.Network.GsonConverters.UserInfo;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;
import com.esung.biblotechandroid.Utility.MD5Util;
import com.esung.biblotechandroid.Utility.SharedPrefUtil;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_EMAIL;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.USER_INFO;


public class ProfileFragment extends Fragment {

    private Context mContext;
    private ImageView mGravatarView;
    private TextView mRentScoreView;
    private TextView mRentableBooksView;



    private UserInfo mUserInfo;
    private NodeJsService nodeJsService;
    private SharedPreferences sharedPref;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(UserInfo userInfo) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setUserInfo(userInfo);
//        Bundle args = new Bundle();
//        args.putParcelable(USER_INFO, userInfo);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO use arguments...
//        mUserInfo = getArguments().getParcelable("userInfo");
        nodeJsService = NodeJsApi.getInstance(getContext()).getService();
        sharedPref = getContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mGravatarView = (ImageView) view.findViewById(R.id.iv_gravatar_view);
        mRentScoreView = (TextView) view.findViewById(R.id.tv_rentScore);
        mRentableBooksView = (TextView) view.findViewById(R.id.tv_rentableBooks);

        setUpUi();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    private void setUpUi() {
        if (mUserInfo != null) {
            fillInUserInfo(mUserInfo);
        } else {
            String userEmail = sharedPref.getString(USER_EMAIL, null);
            if (userEmail == null) {
                SharedPrefUtil.handleError(getContext());
            }

            Call<UserInfo> fetchCall = nodeJsService.fetchUserInfo(userEmail);
            fetchCall.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    mUserInfo = response.body();
                    if (mUserInfo == null) {
                        NodeJsApi.handleServerError(getContext());
                    } else {
                        fillInUserInfo(mUserInfo);
                    }
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    t.printStackTrace();
                    NodeJsApi.handleServerError(getContext());
                }
            });
        }
    }


    private void fillInUserInfo(UserInfo userInfo) {
        mRentScoreView.setText(String.valueOf(userInfo.getRentscore()));
        mRentableBooksView.setText(String.valueOf(userInfo.getRentableBooks()));
        String gravatarHash = MD5Util.md5Hex(sharedPref.getString(USER_EMAIL, "fallback@profile.pic"));
        String gravatarUrl = "http://www.gravatar.com/avatar/" + gravatarHash + "?size=200";
        Picasso.with(mContext)
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

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }
}
