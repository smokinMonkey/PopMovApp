package com.smokinmonkey.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smokinmonkey.popularmoviesapp.data.MovieDbContract;
import com.smokinmonkey.popularmoviesapp.utilities.ConnectUtils;
import com.smokinmonkey.popularmoviesapp.utilities.Preview;
import com.smokinmonkey.popularmoviesapp.utilities.PreviewDBJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

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
    private Button btnPreview;
    private Button btnReview;

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
            MovieDbContract.MovieEntry.COLUMN_REVIEW_STR,

            MovieDbContract.MovieEntry.COLUMN_FAVORITE
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
    public static final int INDEX_FAV = 11;

    private int mMovieId;
    private int mFav;
    private ContentValues mThisMovie = new ContentValues();

    private Preview[] mPreviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvMovieTitle = (TextView) findViewById(R.id.tvDetailsMovieTitle);
        tvMovieReleaseDate = (TextView) findViewById(R.id.tvDetailsReleaseDate);
        tvUserRating = (TextView) findViewById(R.id.tvDetailsUserRating);
        tvMovieOverview = (TextView) findViewById(R.id.tvDetailsOverview);

        fabFavorite = (FloatingActionButton) findViewById(R.id.fabFavorite);
        btnPreview = (Button) findViewById(R.id.btnPreview);
        btnReview = (Button) findViewById(R.id.btnReview);

        // favorite button clicked
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if it is already in favorites
                if(fabFavorite.getDrawable().getConstantState() == getResources()
                        .getDrawable(R.drawable.ic_star_border_black_48dp).getConstantState()) {

                    Uri updateUri = MovieDbContract.MovieEntry.buildMovieUriWithId(mMovieId);
                    mThisMovie.put(MovieDbContract.MovieEntry.COLUMN_FAVORITE, "1");

                    getContentResolver().update(updateUri, mThisMovie, null, null);

                    fabFavorite.setImageResource(R.drawable.ic_star_black_48dp);

                } else if (fabFavorite.getDrawable().getConstantState() == getResources()
                        .getDrawable(R.drawable.ic_star_black_48dp).getConstantState()) {

                    Uri updateUri = MovieDbContract.MovieEntry.buildMovieUriWithId(mMovieId);
                    mThisMovie.put(MovieDbContract.MovieEntry.COLUMN_FAVORITE, "0");

                    getContentResolver().update(updateUri, mThisMovie, null, null);

                    fabFavorite.setImageResource(R.drawable.ic_star_border_black_48dp);
                }

            }
        });

        // preview button clicked
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // review button clicked
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoReview();
            }
        });

        // get URI from intent to get data
        mDetailUri = getIntent().getData();
        if (mDetailUri == null) {
            throw new NullPointerException("URI for Movie Detail Activity cannot be null");
        }

        // loader manager to start loader
        getSupportLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
    }

    public void gotoReview() {
        Intent reviewIntent = new Intent (this, ReviewActivity.class);
        Bundle reviewStr = new Bundle();

        reviewStr.putString("review_str", String.valueOf(mThisMovie.get(
                MovieDbContract.MovieEntry.COLUMN_REVIEW_STR)));
        reviewStr.putString("movie_backdrop", String.valueOf(mThisMovie.get(
                MovieDbContract.MovieEntry.COLUMN_BACKDROP_STR)));
        reviewStr.putString("movie_poster", String.valueOf(mThisMovie.get(
                MovieDbContract.MovieEntry.COLUMN_POSTER_STR)));
        reviewStr.putString("movie_title", String.valueOf(mThisMovie.get(
                MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
        reviewIntent.putExtras(reviewStr);

        startActivity(reviewIntent);
    }

    public void loadPreview() throws IOException, JSONException {
        String previewStr = (String) mThisMovie.get(MovieDbContract.MovieEntry.COLUMN_TRAILER_STR);
        URL previewURL = new URL (previewStr);
        new PreviewQueryTask().execute(previewURL);
    }

    public void playPreview(Preview[] previews) {
        mPreviews = previews;

        Intent previewIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + mPreviews[0].getPreviewKey()));
        startActivity(previewIntent);
    }

    public class PreviewQueryTask extends AsyncTask<URL, Void, Preview[]> {
        @Override
        protected Preview[] doInBackground(URL... params) {
            if(params.length == 0) { return null; }
            URL previewQuery = params[0];
            String previewResults = null;

            try {
                previewResults = ConnectUtils.getResponseFromHttpURL(previewQuery);
                Preview[] previews;
                previews = PreviewDBJsonUtils.getPreviewFromJson(DetailActivity.this, previewResults);

                return previews;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Preview[] previews) {
            playPreview(previews);
        }
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

        String movieTitle = data.getString(INDEX_ORIGINAL_TITLE);
        String movieReleaseDate = data.getString(INDEX_RELEASE_DATE);
        String movieOverview = data.getString(INDEX_OVERVIEW);
        String movieRating = data.getString(INDEX_VOTE_AVG);

        String moviePosterString = data.getString(INDEX_POSTER_STR);
        String movieBackdropString = data.getString(INDEX_BACKDROP_STR);
        String movieTrailer = data.getString(INDEX_TRAILER_STR);
        String movieReview = data.getString(INDEX_REVIEW_STR);

        mMovieId = data.getInt(INDEX_MOVIE_ID);
        mFav = data.getInt(INDEX_FAV);

        mThisMovie.put(MovieDbContract.MovieEntry.COLUMN_MOVIE_ID, mMovieId);
        mThisMovie.put(MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieTitle);
        mThisMovie.put(MovieDbContract.MovieEntry.COLUMN_BACKDROP_STR, movieBackdropString);
        mThisMovie.put(MovieDbContract.MovieEntry.COLUMN_POSTER_STR, moviePosterString);
        mThisMovie.put(MovieDbContract.MovieEntry.COLUMN_FAVORITE, mFav);
        mThisMovie.put(MovieDbContract.MovieEntry.COLUMN_TRAILER_STR, movieTrailer);
        mThisMovie.put(MovieDbContract.MovieEntry.COLUMN_REVIEW_STR, movieReview);

        tvMovieTitle.setText(movieTitle);
        tvMovieReleaseDate.setText(movieReleaseDate);
        tvMovieOverview.setText(movieOverview);
        tvUserRating.setText(movieRating);

        if(mFav == 1) {
            fabFavorite.setImageResource(R.drawable.ic_star_black_48dp);
        } else {
            fabFavorite.setImageResource(R.drawable.ic_star_border_black_48dp);
        }

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);

            Picasso
                    .with(this)
                    .load(moviePosterString)
                    .placeholder(R.drawable.no_img)
                    .error(R.drawable.no_img)
                    .into(ivMoviePoster);
        } else {
            ivMoviePoster = (ImageView) findViewById(R.id.ivDetailMovieBackdrop);
            // load backdrop poster
            Picasso
                    .with(this)
                    .load(movieBackdropString)
                    .placeholder(R.drawable.no_img)
                    .error(R.drawable.no_img)
                    .into(ivMoviePoster);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
