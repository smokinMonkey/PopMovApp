package com.smokinmonkey.popularmoviesapp.Utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class with all the methods to parse json data from MovieDB and store it into each
 * individual Movie class.
 *
 * Created by smokinMonkey on 4/21/2017.
 */

public class MovieDBJsonUtils {

    private static final String LOG_TAG = MovieDBJsonUtils.class.getSimpleName();

    /**
     * This method parsed the json data response and returns an array of Movie
     * class. Movie class stores all the values for each individual movie values.
     * @param context
     * @param movieJsonStr - the movie json data string
     * @return Movie[] - array of Movie class
     * @throws JSONException
     */
    public static Movie[] getIndividualMovieValuesFromJson(Context context, String movieJsonStr)
            throws JSONException {

        // variables to store keys
        final String PAGE = "page";
        final String RESULTS = "results";

        // required values for the project
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String ORIGINAL_TITLE = "original_title";
        final String VOTE_AVERAGE = "vote_average";

        // checks if there is an error code
        final String STATUS_CODE = "status_code";

        // convert movie JSON string into JSON object
        JSONObject movieDbJson = new JSONObject(movieJsonStr);

        // checks if there is an error code
        if (movieDbJson.has(STATUS_CODE)) {
            int statusCode = movieDbJson.getInt(STATUS_CODE);
            switch (statusCode) {
                case 1:
                    break;
                case 2:
                    Log.e(LOG_TAG, "Invalid service: this service does not exist.");
                    return null;
                case 3:
                    Log.e(LOG_TAG, "Authentication failed: you do not have permission to access the service.");
                    return null;
                case 4:
                    Log.e(LOG_TAG, "Invalid format: this service doesn't exist in that format.");
                    return null;
                case 5:
                    Log.e(LOG_TAG, "Invalid parameters: your request parameters are incorrect.");
                    return null;
                case 6:
                    Log.e(LOG_TAG, "Invalid id: the pre-requistie id is invalid or not found.");
                    return null;
                case 7:
                    Log.e(LOG_TAG, "Invalid API key: you must be granted a valid key.");
                    return null;
                case 9:
                    Log.e(LOG_TAG, "Service offline: this service is temporarily offline, try again later.");
                    return null;
                case 10:
                    Log.e(LOG_TAG, "Suspended API key: access to your account has been suspended.");
                    return null;
                case 11:
                    Log.e(LOG_TAG, "Internal error: something went wrong, from TMDb");
                    return null;
                default:
                    Log.e(LOG_TAG, "Something went wrong not in the status code: " + statusCode);
                    return null;
            }
        }

        // separating the JSON objects into individual movies into a JSON array
        JSONArray movieArray = movieDbJson.getJSONArray(RESULTS);
        Movie[] parsedMovieList = new Movie[movieArray.length()];

        // for loop to parse each movie from the json array
        for (int i = 0; i < movieArray.length(); i++) {
            String originalTitle;
            String posterPath;
            String overview;
            String voteAverage;
            String releaseDate;

            // json object for each individual movie
            JSONObject individualMovie = movieArray.getJSONObject(i);

            // get the values for each individual movie json object
            originalTitle = individualMovie.getString(ORIGINAL_TITLE);
            posterPath = individualMovie.getString(POSTER_PATH);
            overview = individualMovie.getString(OVERVIEW);
            voteAverage = individualMovie.getString(VOTE_AVERAGE);
            releaseDate = individualMovie.getString(RELEASE_DATE);

            // storing values into a movie class
            parsedMovieList[i] = new Movie(originalTitle, posterPath, overview,
                    voteAverage, releaseDate);
        }

        return parsedMovieList;
    }

}