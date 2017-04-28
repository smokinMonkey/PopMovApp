package com.smokinmonkey.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.smokinmonkey.popularmoviesapp.Utilities.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Movie movieDetails = new Movie();
    private ImageView ivMoviePoster;
    private TextView tvMovieTitle;
    private TextView tvMovieReleaseDate;
    private TextView tvUserRating;
    private TextView tvMovieOverview;

    Intent intentToStartThisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivMoviePoster = (ImageView) findViewById(R.id.ivDetailMoviePoster);
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

        Picasso
                .with(this)
                .load(movieDetails.getImagePosterStr())
                .into(ivMoviePoster);

    }
}
