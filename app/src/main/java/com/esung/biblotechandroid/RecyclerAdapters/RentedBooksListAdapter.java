package com.esung.biblotechandroid.RecyclerAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esung.biblotechandroid.Network.GsonConverters.RentedBooks;
import com.esung.biblotechandroid.R;

import java.util.List;

/**
 * Created by sungdoo on 2017-08-18.
 */

public class RentedBooksListAdapter extends RecyclerView.Adapter<RentedBooksListAdapter.RentedBooksViewHolder> {

    private List<RentedBooks> mRentedBooksList;

    public RentedBooksListAdapter(List<RentedBooks> mRentedBooksList) {
        this.mRentedBooksList = mRentedBooksList;
    }

    @Override
    public RentedBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_rented_books, parent, false);
        return new RentedBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RentedBooksViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRentedBooksList.size();
    }

    public class RentedBooksViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;
        private TextView mDueDateView;

        public RentedBooksViewHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.tv_bookTitle);
            mDueDateView = (TextView) itemView.findViewById(R.id.tv_dueDate);
        }

        public void bind(int position) {
            RentedBooks rentedBooks = mRentedBooksList.get(position);

            //set bookTItle View's content
            String bookTitle = rentedBooks.getTitle();
            mTitleView.setText(bookTitle);

            //set DueDate View's content
            String dueDate = "Due Date : ";
            dueDate = dueDate + rentedBooks.getDueDate().split("T")[0];
            mDueDateView.setText(dueDate);
        }
    }
}
