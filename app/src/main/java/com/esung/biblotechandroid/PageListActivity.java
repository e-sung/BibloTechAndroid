package com.esung.biblotechandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.esung.biblotechandroid.Network.GsonConverters.UserInfo;
import com.esung.biblotechandroid.Utility.SessionManager;

import static com.esung.biblotechandroid.Utility.IntentTag.USER_INFO;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.PAGE_TAG;

/**
 * An activity representing a list of Pages. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PageDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PageListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private Button mRentedBooksBt;
    private Button mBooksIreadBt;
    private Button mPostIwroteBt;
    private Button mSearchBooksBt;
    private Button mQRcodeBt;

    private static final int RENTED_BOOKS_FRAGMENT_TAG = 1;
    private static final int READ_BY_ME_FRAGMENT_TAG = 2;
    private static final int POST_I_WROTE_FRAGMENT_TAG = 3;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ProfileFragment mProfilefragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_list);


        mRentedBooksBt = (Button) findViewById(R.id.bt_booksRentedByMe);
        mBooksIreadBt = (Button) findViewById(R.id.bt_booksIread);
        mPostIwroteBt = (Button) findViewById(R.id.bt_postIwrote);
        mSearchBooksBt = (Button) findViewById(R.id.bt_searchBooks);
        mQRcodeBt = (Button) findViewById(R.id.bt_qrCodeActivity);

        if (findViewById(R.id.page_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        UserInfo userInfo = getIntent().getParcelableExtra(USER_INFO);
        if (mProfilefragment != null) {
            removeProfileFragment();
        }
        setupProfileFragment(userInfo);
        mRentedBooksBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane == true) {
                    setPageFragment(new RentedBooksFragment());
                } else {
                    startDetailActivity((Button) v);
                }
            }
        });

        mBooksIreadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane == true) {
                    setPageFragment(new ReadByMeFragment());
                } else {
                    startDetailActivity((Button) v);
                }
            }
        });

        mPostIwroteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane == true) {
                    setPageFragment(new PostIwroteFragment());
                } else {
                    startDetailActivity((Button) v);
                }
            }
        });

        mSearchBooksBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });
    }

    private void setupProfileFragment(UserInfo userInfo) {
        initializeFragmentManagers();
        mProfilefragment = new ProfileFragment();
        mProfilefragment.newInstance(userInfo);
        fragmentTransaction.replace(R.id.profileFragmentContainer, mProfilefragment);
        fragmentTransaction.commit();
    }

    private void removeProfileFragment() {
        initializeFragmentManagers();
        fragmentTransaction.remove(mProfilefragment);
        fragmentTransaction.commit();
    }

    private void initializeFragmentManagers() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    private void setPageFragment(Fragment fragment) {
        fragmentManager.beginTransaction().
                replace(R.id.page_detail_container, fragment).
                commit();
    }

    private void startDetailActivity(Button b) {
        String tag = b.getTag().toString();
        Intent intent = new Intent(getApplicationContext(), PageDetailActivity.class);
        intent.putExtra(PAGE_TAG, Integer.parseInt(tag));
        startActivity(intent);
    }

    @Override
    protected void onResume() {

        if (mTwoPane == true) {
            int buttonTag = getIntent().getIntExtra(PAGE_TAG, -1);
            switch (buttonTag) {
                case RENTED_BOOKS_FRAGMENT_TAG:
                    mRentedBooksBt.performClick();
                    break;
                case READ_BY_ME_FRAGMENT_TAG:
                    mBooksIreadBt.performClick();
                    break;
                case POST_I_WROTE_FRAGMENT_TAG:
                    mPostIwroteBt.performClick();
                    break;
            }
        }
        UserInfo userInfo = getIntent().getParcelableExtra(USER_INFO);
        setupProfileFragment(userInfo);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_logout) {
            new SessionManager(this).logout(getApplicationContext());
        }
        return true;
    }

}
