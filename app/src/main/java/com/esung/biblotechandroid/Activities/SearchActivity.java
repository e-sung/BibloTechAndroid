package com.esung.biblotechandroid.Activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.esung.biblotechandroid.Network.GsonConverters.BookSearchResults;
import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.Network.NodeJsService;
import com.esung.biblotechandroid.R;
import com.esung.biblotechandroid.RecyclerAdapters.SearchBookResultAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String SEARCH_BY_TITLE = "by-title";
    private static final String SEARCH_BY_AUTHOR = "by-author";
    private static final String SEARCH_BY_PUBLISHER = "by-publisher";
    private NodeJsService mNodeJsService;
    private RecyclerView mRecyclerView;
    private SearchView searchView;
    private String SearchCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_searchBookResult);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNodeJsService = NodeJsApi.getInstance(getApplicationContext()).getService();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_search_menu, menu);

        //Setup SearchCriteria Spinner on ActionBar
        MenuItem searchCriteriaItem = menu.findItem(R.id.app_bar_search_criteria);
        Spinner searchCriteriaSpinner = (Spinner) MenuItemCompat.getActionView(searchCriteriaItem);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.search_criterias, android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchCriteriaSpinner.setAdapter(spinnerAdapter);
        searchCriteriaSpinner.setOnItemSelectedListener(this);

        //Setup SearchBar on ActionBar
        searchView = (SearchView) MenuItemCompat.getActionView(
                menu.findItem(R.id.app_bar_search)
        );
        MenuItemCompat.expandActionView(menu.findItem(R.id.app_bar_search));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fillInRecyclerView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void fillInRecyclerView(String query) {
        Call<List<BookSearchResults>> call = mNodeJsService.fetchSearchResult(SearchCriteria, query);
        call.enqueue(new Callback<List<BookSearchResults>>() {
            @Override
            public void onResponse(Call<List<BookSearchResults>> call, Response<List<BookSearchResults>> response) {
                if (response.body() != null) {
                    mRecyclerView.setAdapter(new SearchBookResultAdapter(response.body()));
                } else {
                    NodeJsApi.handleServerError(getApplicationContext());
                }
            }

            @Override
            public void onFailure(Call<List<BookSearchResults>> call, Throwable t) {
                t.printStackTrace();
                NodeJsApi.handleServerError(getApplicationContext());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                SearchCriteria = SEARCH_BY_TITLE;
                break;
            case 1:
                SearchCriteria = SEARCH_BY_AUTHOR;
                break;
            case 2:
                SearchCriteria = SEARCH_BY_PUBLISHER;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Required Empty Method for Search Category Spinner
    }
}

