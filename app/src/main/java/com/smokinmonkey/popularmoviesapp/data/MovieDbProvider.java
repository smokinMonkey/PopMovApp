package com.smokinmonkey.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by smokinMonkey on 5/13/2017.
 */

public class MovieDbProvider extends ContentProvider {

    private static final String LOG_TAG = MovieDbProvider.class.getSimpleName();

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenMovieHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieDbContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieDbContract.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, MovieDbContract.PATH_MOVIE + "/#", CODE_MOVIE_ID);

        Log.d(LOG_TAG, "URI matcher: " + matcher.toString());

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenMovieHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor c;

        switch(sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                c = mOpenMovieHelper.getReadableDatabase().query(
                        MovieDbContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CODE_MOVIE_ID:
                c = mOpenMovieHelper.getReadableDatabase().query(
                        MovieDbContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieDbContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented, no getType yet");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new RuntimeException("Not implemented, no insert, use bulkinsert");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenMovieHelper.getWritableDatabase();

        int rowsInserted = 0;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                db.beginTransaction();
                try {
                    // foreach loop to insert each value in content values into db
                    for(ContentValues value : values) {
                        long _id = db.insert(MovieDbContract.MovieEntry.TABLE_NAME, null, value);
                        if(_id != -1) rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        if(rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(LOG_TAG, "BULK INSERT, number of rows INSERTED: " + rowsInserted);

        return rowsInserted;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;

        if(selection == null) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                rowsDeleted = mOpenMovieHelper.getWritableDatabase().delete(
                        MovieDbContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(LOG_TAG, "DELETE, number of rows DELETED: " + rowsDeleted);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
