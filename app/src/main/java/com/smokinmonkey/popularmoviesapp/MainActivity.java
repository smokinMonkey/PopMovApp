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
    // id used to identify the Loader, to prevent duplicate loaders
    private static final int ID_MOVIE_LOADER = 78;
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

        mtvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
        mpbLoad = (ProgressBar) findViewById(R.id.pbProgressBar);

        gridView = (GridView) findViewById(R.id.gvGridView);
        // adapter = new MovieListAdapter(this, mmaMainMovieList);
        gridView.setAdapter(adapter);

        // hide grid view until data is loaded, until then, show loading bar
        showLoading();
        // initialize loader
        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

        MovieSyncUtils.startSync(this);
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
                //loadMovieData("now_playing");
                return true;
            case R.id.most_popular:
                //loadMovieData("most_popular");
                return true;
            case R.id.highest_rated:
                //loadMovieData("highest_rated");
                return true;
            default:
                return true;
        }
    }

    // method to show movie data after data been loaded with no errors
    private void showMovieDataView() {
        gridView.setVisibility(View.VISIBLE);
        mtvErrorMsg.setVisibility(View.INVISIBLE);
    }

    // method to show error messages if problem exists
    private void showErrorMsg() {
        gridView.setVisibility(View.INVISIBLE);
        mtvErrorMsg.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_MOVIE_LOADER:
                Uri movieQueryUri = MovieDbContract.MovieEntry.CONTENT_URI;
                return new CursorLoader(this, movieQueryUri, MAIN_MOVIE_LIST, null, null, null);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // error Cursor data null object
        adapter.swapCursor(data);
        if (mPos == GridView.NO_ID) mPos = 0;
        gridView.smoothScrollToPosition(mPos);
        if (data.getCount() != 0) showMovieDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
