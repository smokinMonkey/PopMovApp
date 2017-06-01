package com.smokinmonkey.popularmoviesapp;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.smokinmonkey.popularmoviesapp.data.MovieDbContract;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER_ID = 178;
    private Uri mDetailUri;

    // depending on the orientation need to bind to different view
    private ImageView ivMoviePoster;
    private TextView tvMovieTitle;
    private TextView tvMovieReleaseDate;
    private TextView tvUserRating;
    private TextView tvMovieOverview;
    private FloatingActionButton fabFavorite;

    // database utilities
    public static final String[] MAIN_MOVIE_LIST = {

            MovieDbContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieDbContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieDbContract.MovieEntry.COLUMN_OVERVIEW,
            MovieDbContract.MovieEntry.COLUMN_VOTE_AVG,

            MovieDbContract.MovieEntry.COLUMN_POSTER_STR,
            MovieDbContract.MovieEntry.COLUMN_BACKDROP_STR,
            MovieDbContract.MovieEntry.COLUMN_TRAILER_STR,
            MovieDbContract.MovieEntry.COLUMN_REVIEW_STR
    };

    // index to keep track of value in the array, if the order
    // of the above array changes, these indexes must adjust
    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_ORIGINAL_TITLE = 1;
    public static final int INDEX_RELEASE_DATE = 2;
    public static final int INDEX_POSTER_PATH = 3;
    public static final int INDEX_BACKDROP_PATH = 4;
    public static final int INDEX_OVERVIEW = 5;
    public static final int INDEX_VOTE_AVG = 6;
    public static final int INDEX_POSTER_STR = 7;
    public static final int INDEX_BACKDROP_STR = 8;
    public static final int INDEX_TRAILER_STR = 9;
    public static final int INDEX_REVIEW_STR = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvMovieTitle = (TextView) findViewById(R.id.tvDetailsMovieTitle);
        tvMovieReleaseDate = (TextView) findViewById(R.id.tvDetailsReleaseDate);
        tvUserRating = (TextView) findViewById(R.id.tvDetailsUserRating);
        tvMovieOverview = (TextView) findViewById(R.id.tvDetailsOverview);
        fabFavorite = (FloatingActionButton) findViewById(R.id.fabFavorite);

        // get URI from intent to get data
        mDetailUri = getIntent().getData();
        if(mDetailUri == null)
            throw new NullPointerException("URI for Movie Detail Activity cannot be null");

        // loader manager to start loader
        getSupportLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case DETAIL_LOADER_ID:
                return new CursorLoader(
                        this,
                        mDetailUri,
                        MAIN_MOVIE_LIST,
                        null,
                        null,
                        null
                );
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if(data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if(!cursorHasValidData) {
            Log.d("ERROR!", "Cursor has no data!!!!!");
            return;
        }

        int movieId = data.getInt(INDEX_MOVIE_ID);

        Log.d("CHECK OUT", "Movie id: " + Integer.toString(movieId));

        String movieTitle = data.getString(INDEX_ORIGINAL_TITLE);
        String movieReleaseDate = data.getString(INDEX_RELEASE_DATE);
        String movieOverview = data.getString(INDEX_OVERVIEW);
        String movieRating = data.getString(INDEX_VOTE_AVG);

        String moviePosterString = data.getString(INDEX_POSTER_STR);
        String movieBackdropString = data.getString(INDEX_BACKDROP_STR);
        String movieTrailer = data.getString(INDEX_TRAILER_STR);
        String movieReview = data.getString(INDEX_REVIEW_STR);

        tvMovieTitle.setText(movieTitle);
        tvMovieReleaseDate.setText(movieReleaseDate);
        tvMovieOverview.setText(movieOverview);
        tvUserRating.setText(movieRating);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);

            Picasso
                    .with(this)
                    .load(movieBackdropString)
                    .placeholder(R.drawable.no_img)
                    .error(R.drawable.no_img)
                    .into(ivMoviePoster);
        } else {
            ivMoviePoster = (ImageView) findViewById(R.id.ivDetailMovieBackdrop);
            // load backdrop poster
            Picasso
                    .with(this)
                    .load(moviePosterString)
                    .placeholder(R.drawable.no_img)
                    .error(R.drawable.no_img)
                    .into(ivMoviePoster);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
