package com.smokinmonkey.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by smokinMonkey on 5/13/2017.
 */

public class MovieDbContract {

    public static final String CONTENT_AUTHORITY = "com.smokinmonkey.popularmoviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movies";

    // defines the table contents of the movies table
    public static final class MovieEntry implements BaseColumns {
        // base CONTENT URI used to query the movie table from the content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        // local table name for movie database
        public static final String TABLE_NAME = "movies";

        // table columns
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE ="release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVG = "vote_avg";

        public static final String COLUMN_POSTER_STR = "poster_str";
        public static final String COLUMN_BACKDROP_STR = "backdrop_str";
        public static final String COLUMN_TRAILER_STR = "preview_str";
        public static final String COLUMN_REVIEW_STR = "review_str";

        /**  builds URI that adds the movie id to the end of movie content URI
         *   path this is used to query movie details about a single movie
         *   entry by its id
         *
         *   @param movieId specific movie Id
         *   @return URI to query details about a single movie entry
         */
        public static Uri buildMovieUriWithId(int movieId) {
            // log URI make sure it is correct
            Uri uriWithId = CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(movieId))
                    .build();

            Log.d("MovieListAdater", "DEBUG URI with MOVIE ID: " + uriWithId);

            return uriWithId;

//            return CONTENT_URI.buildUpon()
//                    .appendPath(Integer.toString(movieId))
//                    .build();
        }

    }


}
