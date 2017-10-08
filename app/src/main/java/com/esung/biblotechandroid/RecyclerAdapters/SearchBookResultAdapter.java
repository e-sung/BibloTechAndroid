package com.esung.biblotechandroid.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.esung.biblotechandroid.Activities.PostListActivity;
import com.esung.biblotechandroid.Network.GsonConverters.BookSearchResults;
import com.esung.biblotechandroid.R;

import java.util.List;

/**
 * Created by sungdoo on 2017-08-13.
 */

public class SearchBookResultAdapter extends RecyclerView.Adapter<SearchBookResultAdapter.ViewHolder> {

    private List<BookSearchResults> mBookSearchResult;

    public SearchBookResultAdapter(List<BookSearchResults> mBookSearchResult) {
        this.mBookSearchResult = mBookSearchResult;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_search_book_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mBookSearchResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleview;
        private TextView mAuthorView;
        private TextView mRentableStatusView;
        private Button mGetPostButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleview = (TextView) itemView.findViewById(R.id.tv_title);
            mAuthorView = (TextView) itemView.findViewById(R.id.tv_author);
            mRentableStatusView = (TextView) itemView.findViewById(R.id.tv_rentalStatus);
            mGetPostButton = (Button) itemView.findViewById(R.id.bt_getPosts);
        }

        public void bind(int position) {
            BookSearchResults item = mBookSearchResult.get(position);

            mTitleview.setText(item.getTitle());
            mAuthorView.setText(item.getAuthor());
            if (item.getIsRented() == 0) {
                mRentableStatusView.setText("In stock");
            } else {
                String str = "Rented By ";
                str += item.getRenterEmail();
                mRentableStatusView.setText(str);
            }

            mGetPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PostListActivity.class);
                    intent.putExtra("bookTitle", mTitleview.getText());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
