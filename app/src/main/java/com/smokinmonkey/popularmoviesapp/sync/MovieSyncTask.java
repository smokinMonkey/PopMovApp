package com.smokinmonkey.popularmoviesapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.smokinmonkey.popularmoviesapp.data.MovieDbContract;
import com.smokinmonkey.popularmoviesapp.utilities.ConnectUtils;
import com.smokinmonkey.popularmoviesapp.utilities.MovieDBJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by smokinMonkey on 5/19/2017.
 */

public class MovieSyncTask {

    synchronized public static void syncMovie(Context context, String queryType) {

        try {
            URL movieRequestUrl = ConnectUtils.getUrl(context, queryType);
            String jsonMovieResponse = ConnectUtils.getResponseFromHttpURL(movieRequestUrl);
            ContentValues[] movieValues = MovieDBJsonUtils
                    .getMovieContentValuesFromJson(context, jsonMovieResponse);

            if (movieValues != null && movieValues.length != 0) {
                // content resolver for content provider
                ContentResolver movieContentResolver = context.getContentResolver();
//                // delete old data from local database using content provider and content resolver
//                movieContentResolver.delete(
//                        MovieDbContract.MovieEntry.CONTENT_URI,
//                        null,
//                        null
//                );
                // insert the new data into local database
                movieContentResolver.bulkInsert(
                        MovieDbContract.MovieEntry.CONTENT_URI,
                        movieValues
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
