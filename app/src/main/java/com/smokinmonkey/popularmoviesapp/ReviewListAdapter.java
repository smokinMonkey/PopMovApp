package com.smokinmonkey.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smokinmonkey.popularmoviesapp.utilities.Review;

/**
 * Created by smokinMonkey on 6/13/2017.
 */

public class ReviewListAdapter extends BaseAdapter {

    private TextView mtvAuthor;
    private TextView mtvContent;
    private TextView mtvUrl;

    private Context mContext;
    private Review[] mReviews;

    public ReviewListAdapter(@NonNull Context context, Review[] reviews) {
        this.mContext = context;
        this.mReviews = reviews;
    }

    public void setReviewData(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mReviews == null) return 0;
        return mReviews.length;
    }

    @Override
    public Object getItem(int position) {
        return mReviews[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.review_list_item, parent, false);
        }

        Review review = mReviews[position];

        mtvAuthor = (TextView) convertView.findViewById(R.id.tvReviewAuthor);
        mtvContent = (TextView) convertView.findViewById(R.id.tvReviewContent);
        mtvUrl = (TextView) convertView.findViewById(R.id.tvReviewUrl);

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.review_list_item, parent, false);
        }

        mtvAuthor.setText(review.getReviewAuthor());
        mtvContent.setText(review.getReviewContent());
        mtvUrl.setText(review.getReviewUrl());

        return convertView;
    }
}
