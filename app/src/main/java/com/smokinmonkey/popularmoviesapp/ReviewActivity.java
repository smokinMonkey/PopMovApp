package com.smokinmonkey.popularmoviesapp;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smokinmonkey.popularmoviesapp.utilities.ConnectUtils;
import com.smokinmonkey.popularmoviesapp.utilities.Review;
import com.smokinmonkey.popularmoviesapp.utilities.ReviewDBJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ReviewActivity extends AppCompatActivity {

    String mReviewStr;
    String mJsonReviewStr;
    Review[] mReviews;

    String mMovieTitle;
    String mMovieBackdrop;
    String mMoviePoster;

    ImageView mivMoviePoster;
    TextView mtvMovieTitle;

    ListView lvReviews;
    ReviewListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        lvReviews = (ListView) findViewById(R.id.lvReviews);
        adapter = new ReviewListAdapter(this, mReviews);
        lvReviews.setAdapter(adapter);

        mtvMovieTitle = (TextView) findViewById(R.id.tvReviewMoviteTitle);

        mReviewStr = getIntent().getExtras().getString("review_str");
        mMovieTitle = getIntent().getExtras().getString("movie_title");
        mMovieBackdrop = getIntent().getExtras().getString("movie_backdrop");
        mMoviePoster = getIntent().getExtras().getString("movie_poster");

        mtvMovieTitle.setText(mMovieTitle);

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            mivMoviePoster = (ImageView) findViewById(R.id.ivReviewMovieBackdrop);

            Picasso
                    .with(this)
                    .load(mMovieBackdrop)
                    .placeholder(R.drawable.no_img)
                    .error(R.drawable.no_img)
                    .into(mivMoviePoster);
        } else {
            mivMoviePoster = (ImageView) findViewById(R.id.ivReviewMoviePoster);

            Picasso
                    .with(this)
                    .load(mMoviePoster)
                    .placeholder(R.drawable.no_img)
                    .error(R.drawable.no_img)
                    .into(mivMoviePoster);
        }

        try {
            loadReview(mReviewStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadReview(String reviewStr) throws MalformedURLException {
        URL reviewURL = new URL(reviewStr);
        new ReviewQueryTask().execute(reviewURL);
    }

    public class ReviewQueryTask extends AsyncTask<URL, Void, Review[]> {
        @Override
        protected Review[] doInBackground(URL... params) {
            if(params.length == 0) { return null; }

            URL reviewQuery = params[0];
            String reviewResults = null;

            try {
                reviewResults = ConnectUtils.getResponseFromHttpURL(reviewQuery);
                Review[] mThisReviews;
                mThisReviews = ReviewDBJsonUtils.getReviewFromJson(ReviewActivity.this, reviewResults);

                return mThisReviews;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            adapter.setReviewData(reviews);
        }
    }


}
