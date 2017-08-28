package com.esung.biblotechandroid.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esung.biblotechandroid.Activities.PostDetailActivity;
import com.esung.biblotechandroid.Network.GsonConverters.PostInfo;
import com.esung.biblotechandroid.R;

import java.util.List;

/**
 * Created by sungdoo on 2017-08-19.
 */

public class PostIWroteListAdapter extends RecyclerView.Adapter<PostIWroteListAdapter.PostListViewHolder> {

    private List<PostInfo> mPostInfoList;

    public PostIWroteListAdapter(List<PostInfo> mPostInfoList) {
        this.mPostInfoList = mPostInfoList;
    }

    @Override
    public PostListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_post_i_wrote_list, parent, false);
        return new PostListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mPostInfoList.size();
    }

    public class PostListViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView mPostTitleView;
        private TextView mBookTitleView;
        private TextView mWrittenDateView;

        public PostListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mPostTitleView = (TextView) itemView.findViewById(R.id.tv_postTitle);
            mBookTitleView = (TextView) itemView.findViewById(R.id.tv_bookTitle);
            mWrittenDateView = (TextView) itemView.findViewById(R.id.tv_writtenDate);
        }

        public void bind(final int position) {
            PostInfo postInfo = mPostInfoList.get(position);

            // Set postTitle View's contents
            mPostTitleView.setText(postInfo.getPostTitle());

            // Set bookTitle View's contents
            String bookTitle = "Book Title : ";
            bookTitle += mPostInfoList.get(position).getBookTitle();
            mBookTitleView.setText(bookTitle);

            //set WrittenDate View's contents
            String writtenDate = "Written Date : ";
            writtenDate += mPostInfoList.get(position).getWrittenTime().split("T")[0];
            mWrittenDateView.setText(writtenDate);

            //When cardView is clicked, start PostDetailActivity with postId of clicked cardView
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
                    int postId = mPostInfoList.get(position).getId();
                    intent.putExtra("postId", postId);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
