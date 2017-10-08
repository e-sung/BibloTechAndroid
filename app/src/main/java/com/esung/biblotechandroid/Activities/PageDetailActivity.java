package com.esung.biblotechandroid.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.esung.biblotechandroid.Fragments.PostIwroteFragment;
import com.esung.biblotechandroid.Fragments.ReadByMeFragment;
import com.esung.biblotechandroid.Fragments.RentedBooksFragment;
import com.esung.biblotechandroid.R;
import com.esung.biblotechandroid.Utility.IntentTag;
import com.esung.biblotechandroid.Utility.SessionManager;

/**
 * An activity representing a single Page detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PageListActivity}.
 */
public class PageDetailActivity extends AppCompatActivity {

    private static final String PAGE_TAG = "pageTag";
    private static final int RENTED_BOOKS_FRAGMENT_TAG = 1;
    private static final int READ_BY_ME_FRAGMENT_TAG = 2;
    private static final int POST_I_WROTE_FRAGMENT_TAG = 3;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_detail);

        //This activity is for Portrait Mode Only.
        //So, if screen is in LandScape Mode, we start PageListActivity
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, PageListActivity.class);
            intent.putExtra(PAGE_TAG, getIntent().getIntExtra(PAGE_TAG, -1));
            startActivity(intent);
            finish();
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            int buttonTag = getIntent().getIntExtra(PAGE_TAG, -1);
            switch (buttonTag) {
                case RENTED_BOOKS_FRAGMENT_TAG:
                    mFragment = new RentedBooksFragment();
                    getSupportActionBar().setTitle(getString(R.string.books_i_rent));
                    break;
                case READ_BY_ME_FRAGMENT_TAG:
                    mFragment = new ReadByMeFragment();
                    getSupportActionBar().setTitle(getString(R.string.books_i_read));
                    break;
                case POST_I_WROTE_FRAGMENT_TAG:
                    getSupportActionBar().setTitle(getString(R.string.posts_i_wrote));
                    mFragment = new PostIwroteFragment();
                    break;
                default:
                    Toast.makeText(this, "Don't know which button was clicked!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.page_detail_container, mFragment)
                    .commit();
        }
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
        } else if (item.getItemId() == R.id.menu_item_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.putExtra(IntentTag.PREVIOUS_ACTIVITY, true);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
