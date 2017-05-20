package com.smokinmonkey.popularmoviesapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.smokinmonkey.popularmoviesapp.utilities.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Intent intentToStartThisActivity;
    private Movie movieDetails = new Movie();
    private ImageView ivMoviePoster;
    private TextView tvMovieTitle;
    private TextView tvMovieReleaseDate;
    private TextView tvUserRating;
    private TextView tvMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvMovieTitle = (TextView) findViewById(R.id.tvDetailsMovieTitle);
        tvMovieReleaseDate = (TextView) findViewById(R.id.tvDetailsReleaseDate);
        tvUserRating = (TextView) findViewById(R.id.tvDetailsUserRating);
        tvMovieOverview = (TextView) findViewById(R.id.tvDetailsOverview);

        intentToStartThisActivity = this.getIntent();
        Bundle b = getIntent().getExtras();
        movieDetails = (Movie) b.getSerializable("MovieDetails");

        tvMovieTitle.setText(movieDetails.getOriginalTitle());
        tvMovieReleaseDate.setText(movieDetails.getReleaseDate());
        tvUserRating.setText(movieDetails.getVoteAverage());
        tvMovieOverview.setText(movieDetails.getOverview());

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);

            Picasso
                    .with(this)
                    .load(movieDetails.getImagePosterStr())
                    .placeholder(R.drawable.no_img)
                    .error(R.drawable.no_img)
                    .into(ivMoviePoster);
        } else {
            ivMoviePoster = (ImageView) findViewById(R.id.ivDetailMovieBackdrop);
            // load backdrop poster
            Picasso
                    .with(this)
                    .load(movieDetails.getImagePosterStr())
                    .placeholder(R.drawable.no_img)
                    .error(R.drawable.no_img)
                    .into(ivMoviePoster);
        }


    }
}
