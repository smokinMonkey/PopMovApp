package com.smokinmonkey.popularmoviesapp.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Class to store queries and URI & URL builds and connecting to MovieDB
 * and get data from MovieDB into an array of Movie classes
 *
 * Created by smokinMonkey on 4/21/2017.
 */
public class ConnectUtils {

    private static final String TAG = ConnectUtils.class.getSimpleName();

    // variables to store API key and different types of query requests
    static final String MOVIEDB_NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing";
    static final String API_KEY = "3fc3813b4582c119491777cfb5f1c297";
    static final String MOVIEDB_NOW_PLAYING_API_KEY =
            "https://api.themoviedb.org/3/movie/now_playing?api_key=3fc3813b4582c119491777cfb5f1c297&language=en-US&page=1";

    /**
     * Method to build the URL base on the requested query
     * @param theMovieDBQuery - pass which type of query to perform
     * @return buildURL - URL build for requested query
     */
    public static URL buildURL(String theMovieDBQuery) {

        //Uri buildURI = Uri.parse(theMovieDBQuery).buildUpon().build();
        // to make sure working, hard coding link with api key
        Uri buildURI = Uri.parse(MOVIEDB_NOW_PLAYING_API_KEY).buildUpon().build();

        URL buildURL = null;
        try {
            buildURL = new URL(buildURI.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.toString());
        }

        return buildURL;
    }

    public static String getResponseFromHttpURL(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = conn.getInputStream();

            Scanner scan = new Scanner(in);
            scan.useDelimiter("\\A");

            if(scan.hasNext()) {
                return scan.next();
            } else {
                return null;
            }
        } finally {
            conn.disconnect();
        }
    }

}
