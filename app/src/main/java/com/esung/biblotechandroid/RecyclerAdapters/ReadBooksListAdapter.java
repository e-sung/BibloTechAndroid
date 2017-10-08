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
import com.esung.biblotechandroid.R;

/**
 * Created by sungdoo on 2017-08-19.
 */

public class ReadBooksListAdapter extends RecyclerView.Adapter<ReadBooksListAdapter.ReadBooksViewHolder> {

    private String[] mReadBookList;

    public ReadBooksListAdapter(String[] mReadBookList) {
        this.mReadBookList = mReadBookList;
    }

    @Override
    public ReadBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_read_by_me_result, parent, false);
        return new ReadBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReadBooksViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mReadBookList.length;
    }

    public class ReadBooksViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;
        private Button mGetPostButton;

        public ReadBooksViewHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.tv_title);
            mGetPostButton = (Button) itemView.findViewById(R.id.bt_getPosts);
        }

        public void bind(int position) {
            mTitleView.setText(mReadBookList[position]);
            mGetPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PostListActivity.class);
                    intent.putExtra("bookTitle", mTitleView.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
