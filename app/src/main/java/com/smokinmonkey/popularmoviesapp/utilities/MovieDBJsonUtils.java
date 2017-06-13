package com.smokinmonkey.popularmoviesapp.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.smokinmonkey.popularmoviesapp.data.MovieDbContract;

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

    // variables to store keys
    private static final String PAGE = "page";
    private static final String RESULTS = "results";

    // required values for the project
    private static final String MOVIE_ID = "id";
    private static final String POSTER_PATH = "poster_path";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String POP = "popularity";

    // checks if there is an error code
    private static final String STATUS_CODE = "status_code";

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

    public static ContentValues[] getMovieContentValuesFromJson(
            Context context, String movieJsonStr) throws JSONException {

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray jsonMoviesArray = movieJson.getJSONArray(RESULTS);

        ContentValues[] movieContentValues = new ContentValues[jsonMoviesArray.length()];

        for(int i = 0; i < jsonMoviesArray.length(); i++) {
            int movieId;
            double voteAvg;
            String movieTitle;
            String posterPath;
            String backdropPath;
            String releaseDate;
            String overview;
            double pop;

            JSONObject movie = jsonMoviesArray.getJSONObject(i);

            movieId = movie.getInt(MOVIE_ID);
            voteAvg = movie.getDouble(VOTE_AVERAGE);
            movieTitle = movie.getString(ORIGINAL_TITLE);
            posterPath = movie.getString(POSTER_PATH);
            backdropPath = movie.getString(BACKDROP_PATH);
            releaseDate = movie.getString(RELEASE_DATE);
            overview = movie.getString(OVERVIEW);
            pop = movie.getDouble(POP);

            ContentValues movieValues = new ContentValues();
            // column values available from json
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_VOTE_AVG, voteAvg);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieTitle);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH, backdropPath);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_OVERVIEW, overview);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_POP, pop);
            // URL values from builder
            String posterUrlString = buildMoviePosterUrl(posterPath);
            String backdropUrlString = buildMoviePosterUrl(backdropPath);

            String movieTrailersUrlString = buildMovieTrailerUrl(movieId);
            String movieReviewsUrlString = buildMovieReviewUrl(movieId);

            // put them into local database
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_POSTER_STR, posterUrlString);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_BACKDROP_STR, backdropUrlString);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_TRAILER_STR, movieTrailersUrlString);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_REVIEW_STR, movieReviewsUrlString);

            movieContentValues[i] = movieValues;
        }

        return movieContentValues;
    }

    public static String buildMoviePosterUrl(String posterPath) {
        String posterString;
        posterString = ConnectUtils.IMAGE_BASE_URL + posterPath;
        return posterString;
    }

    public static String buildMovieTrailerUrl(int movieId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ConnectUtils.HTTP)
                .authority(ConnectUtils.BASE_MOVIEDB_URL)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(Integer.toString(movieId))
                .appendPath("videos")
                .appendQueryParameter("api_key", ConnectUtils.API_KEY)
                .build();
        return builder.toString();
    }

    public static String buildMovieReviewUrl(int movieId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ConnectUtils.HTTP)
                .authority(ConnectUtils.BASE_MOVIEDB_URL)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(Integer.toString(movieId))
                .appendPath("reviews")
                .appendQueryParameter("api_key", ConnectUtils.API_KEY)
                .build();
        return builder.toString();
    }


}