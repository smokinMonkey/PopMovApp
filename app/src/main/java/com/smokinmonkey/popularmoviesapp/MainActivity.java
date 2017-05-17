package com.smokinmonkey.popularmoviesapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smokinmonkey.popularmoviesapp.utilities.ConnectUtils;
import com.smokinmonkey.popularmoviesapp.utilities.Movie;
import com.smokinmonkey.popularmoviesapp.utilities.MovieDBJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public static Movie[] mmaMainMovieList;

    private GridView gridView;
    private MovieListAdapter adapter;

    private TextView mtvErrorMsg;
    private ProgressBar mpbLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
        mpbLoad = (ProgressBar) findViewById(R.id.pbProgressBar);

        gridView = (GridView) findViewById(R.id.gvGridView);
        adapter = new MovieListAdapter(this, mmaMainMovieList);
        gridView.setAdapter(adapter);

        String testQuery = "now_playing";
        loadMovieData(testQuery);
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
                loadMovieData("now_playing");
                return true;
            case R.id.most_popular:
                loadMovieData("most_popular");
                return true;
            case R.id.highest_rated:
                loadMovieData("highest_rated");
                return true;
            default:
                return true;
        }
    }

    // method to load and bind the data to views
    private void loadMovieData(String searchType) {
        showMovieDataView();
        URL movieDbURL = ConnectUtils.buildURL(searchType);
        new MovieDBQueryTask().execute(movieDbURL);
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

    // async task for connect internet and download data
    public class MovieDBQueryTask extends AsyncTask<URL, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mpbLoad.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(URL... params) {
            if(params.length == 0) {
                return null;
            }

            URL movieDBQuery = params[0];
            String movieDBResults = null;

            try {
                movieDBResults = ConnectUtils.getResponseFromHttpURL(movieDBQuery);
                Movie[] mmaMovieList;
                mmaMovieList = MovieDBJsonUtils
                        .getIndividualMovieValuesFromJson(MainActivity.this, movieDBResults);
                return mmaMovieList;
            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            mpbLoad.setVisibility(View.INVISIBLE);
            showMovieDataView();
            adapter.setMovieData(movieData);
        }
    }

}
