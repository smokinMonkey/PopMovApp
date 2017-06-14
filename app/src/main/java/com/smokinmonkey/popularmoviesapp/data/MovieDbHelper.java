package com.smokinmonkey.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by smokinMonkey on 5/14/2017.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieDbContract.MovieEntry.TABLE_NAME + " ( " +
                        MovieDbContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieDbContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                        MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieDbContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_VOTE_AVG + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_POP + " REAL, " +

                        MovieDbContract.MovieEntry.COLUMN_POSTER_STR + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_BACKDROP_STR + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_TRAILER_STR + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_REVIEW_STR + " TEXT, " +
                        MovieDbContract.MovieEntry.COLUMN_FAVORITE + " INTEGER, " +

                "UNIQUE (" + MovieDbContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT IGNORE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieDbContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
