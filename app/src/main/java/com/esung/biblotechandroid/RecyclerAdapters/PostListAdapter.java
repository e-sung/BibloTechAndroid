package com.esung.biblotechandroid.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esung.biblotechandroid.Activities.PostDetailActivity;
import com.esung.biblotechandroid.Fragments.PostDetailFragment;
import com.esung.biblotechandroid.Network.GsonConverters.PostInfo;
import com.esung.biblotechandroid.R;

import java.util.List;

/**
 * Created by sungdoo on 2017-08-20.
 */

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostlistViewHolder> {

    private List<PostInfo> mPostList;
    private ViewGroup mParentView;
    private boolean mTwoPane;
    private FragmentManager mFragmentManager;

    public PostListAdapter(List<PostInfo> mPostList, boolean mTwoPane) {
        this.mPostList = mPostList;
        this.mTwoPane = mTwoPane;
    }

    @Override
    public PostlistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        mParentView = (ViewGroup) parent.getParent();
        mFragmentManager = ((AppCompatActivity) mParentView.getContext()).getSupportFragmentManager();
        View view = inflater.inflate(R.layout.item_post_list, parent, false);
        return new PostlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostlistViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public class PostlistViewHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        private TextView mPostTitleView;
        private TextView mWrittenDateView;
        private TextView mWriterView;
        private int mPostId;

        public PostlistViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mPostTitleView = (TextView) itemView.findViewById(R.id.tv_postTitle);
            mWrittenDateView = (TextView) itemView.findViewById(R.id.tv_writtenDate);
            mWriterView = (TextView) itemView.findViewById(R.id.tv_writer);
        }

        public void bind(int position) {
            final PostInfo postInfo = mPostList.get(position);

            //set postTitle View's content
            final String postTitle = postInfo.getPostTitle();
            mPostTitleView.setText(postTitle);

            //set writtenDate View's content
            String writtenDate = postInfo.getWrittenTime().split("T")[0];
            mWrittenDateView.setText(writtenDate);

            //set Writer View's content
            String writer = postInfo.getWriter();
            mWriterView.setText("Written By : " + writer);

            mPostId = postInfo.getId();
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        PostDetailFragment fragment = PostDetailFragment.newInstance(
                                mPostId, postTitle, postInfo.getBookTitle()
                        );
                        mFragmentManager.beginTransaction().
                                replace(R.id.post_detail_container, fragment)
                                .commit();
                    } else {
                        Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
                        intent.putExtra("postId", mPostId);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
