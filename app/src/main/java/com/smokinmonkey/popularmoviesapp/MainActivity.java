package com.smokinmonkey.popularmoviesapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smokinmonkey.popularmoviesapp.data.MovieDbContract;
import com.smokinmonkey.popularmoviesapp.sync.MovieSyncUtils;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // database utilities
    public static final String[] MAIN_MOVIE_LIST = {

            MovieDbContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieDbContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieDbContract.MovieEntry.COLUMN_OVERVIEW,
            MovieDbContract.MovieEntry.COLUMN_VOTE_AVG,
            MovieDbContract.MovieEntry.COLUMN_POP,

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
    public static final int INDEX_POP = 7;
    public static final int INDEX_POSTER_STR = 8;
    public static final int INDEX_BACKDROP_STR = 9;
    public static final int INDEX_TRAILER_STR = 10;
    public static final int INDEX_REVIEW_STR = 11;
    public static final int INDEX_FAVORITE = 12;

    // id used to identify the Loader, to prevent duplicate loaders
    private static final int ID_NOW_PLAYING = 78;
    private static final int ID_MOST_POP = 79;
    private static final int ID_HIGHEST_RATED = 80;
    private static final int ID_FAV = 81;

    private static final String NOW_PLAYING = "now_playing";
    private static final String MOST_POP = "most_popular";
    private static final String HIGHEST_RATED = "highest_rated";
    private static final String FAVORITES = "favorites";


    private String mQueryType = NOW_PLAYING;
    private int mQueryTypeId = ID_NOW_PLAYING;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    // gridview utilities
    private GridView gridView;
    private MovieListAdapter adapter;
    private int mPos = GridView.NO_ID;

    // debug utilities
    private TextView mtvErrorMsg;
    private ProgressBar mpbLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mQueryType = savedInstanceState.getString("queryType");
            mQueryTypeId = savedInstanceState.getInt("queryTypeId");
        }

        gridView = (GridView) findViewById(R.id.gvGridView);
        mtvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
        mpbLoad = (ProgressBar) findViewById(R.id.pbProgressBar);

        adapter = new MovieListAdapter(this);
        gridView.setAdapter(adapter);

        // hide grid view until data is loaded, until then, show loading bar
        showLoading();
        // initialize loader
        getSupportLoaderManager().initLoader(mQueryTypeId, null, this);
        MovieSyncUtils.startSync(this, mQueryType);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("queryType", mQueryType);
        outState.putInt("queryTypeId", mQueryTypeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mQueryType = savedInstanceState.getString("queryType");
        mQueryTypeId = savedInstanceState.getInt("queryTypeId");
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void showLoading() {
        gridView.setVisibility(View.INVISIBLE);
        mpbLoad.setVisibility(View.VISIBLE);
    }

    // creating menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    // when options item selected run different queries
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.now_playing:
                mQueryType = NOW_PLAYING;
                mQueryTypeId = ID_NOW_PLAYING;
                getSupportLoaderManager().restartLoader(ID_NOW_PLAYING, null, this);
                MovieSyncUtils.startSync(this, NOW_PLAYING);
                return true;
            case R.id.most_popular:
                mQueryType = MOST_POP;
                mQueryTypeId = ID_MOST_POP;
                getSupportLoaderManager().restartLoader(ID_MOST_POP, null, this);
                MovieSyncUtils.startSync(this, MOST_POP);
                return true;
            case R.id.highest_rated:
                mQueryType = HIGHEST_RATED;
                mQueryTypeId = ID_HIGHEST_RATED;
                getSupportLoaderManager().restartLoader(ID_HIGHEST_RATED, null, this);
                MovieSyncUtils.startSync(this, HIGHEST_RATED);
                return true;
            case R.id.favorite:
                mQueryType = FAVORITES;
                mQueryTypeId = ID_FAV;
                getSupportLoaderManager().restartLoader(ID_FAV, null, this);
                MovieSyncUtils.startSync(this, FAVORITES);
                return true;
            default:
                return true;
        }
    }

    // method to show movie data after data been loaded with no errors
    private void showMovieDataView() {
        gridView.setVisibility(View.VISIBLE);
        mtvErrorMsg.setVisibility(View.INVISIBLE);
        mpbLoad.setVisibility(View.INVISIBLE);
    }

    // method to show error messages if problem exists
    private void showErrorMsg() {
        gridView.setVisibility(View.INVISIBLE);
        mtvErrorMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri movieQueryUri = MovieDbContract.MovieEntry.CONTENT_URI;
        switch (id) {
            case ID_NOW_PLAYING:
                return new CursorLoader(this,
                        movieQueryUri,
                        MAIN_MOVIE_LIST,
                        null,
                        null,
                        "release_date DESC"
                );
            case ID_MOST_POP:
                return new CursorLoader(this,
                        movieQueryUri,
                        MAIN_MOVIE_LIST,
                        null,
                        null,
                        "popularity DESC"
                );
            case ID_HIGHEST_RATED:
                return new CursorLoader(this,
                        movieQueryUri,
                        MAIN_MOVIE_LIST,
                        null,
                        null,
                        "vote_avg DESC"
                );
            case ID_FAV:
                String[] selectArgs = {"1"};
                return new CursorLoader(this,
                        movieQueryUri,
                        MAIN_MOVIE_LIST,
                        MovieDbContract.MovieEntry.COLUMN_FAVORITE + "=?",
                        selectArgs,
                        null
                );
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        if (mPos == GridView.NO_ID) mPos = 0;
        gridView.smoothScrollToPosition(mPos);
        if (data.getCount() != 0) showMovieDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { adapter.swapCursor(null); }

}
