package com.smokinmonkey.popularmoviesapp.utilities;

import java.io.Serializable;

/**
 * Class to store all the values needed for each individual movie parsed
 * from json data.
 *
 * Created by smokinMonkey on 4/21/2017.
 */
public class Movie implements Serializable {

    private final String LOG_TAG = Movie.class.getSimpleName();

    private String mOriginalTitle;
    private String mPosterPath;
    private String mBackdropPath;
    private String mOverview;
    private String mVoteAverage;
    private String mReleaseDate;
    private String mBackdropStr;

    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private String mMoviePosterStr = "";

    public Movie(String originalTitle, String posterPath, String backdropPath, String overview,
                 String voteAvgerage, String releaseDate) {
        this.mOriginalTitle = originalTitle;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mVoteAverage = voteAvgerage;
        this.mReleaseDate = releaseDate;
        this.mBackdropPath = backdropPath;

        mMoviePosterStr = IMAGE_BASE_URL + mPosterPath;
        mBackdropStr = IMAGE_BASE_URL + mBackdropPath;
    }

    public Movie(){};

    public String getOriginalTitle() { return mOriginalTitle; }

    public String getOverview() { return mOverview; }

    public String getVoteAverage() { return mVoteAverage; }

    public String getReleaseDate() { return mReleaseDate; }

    public String getImagePosterStr() { return mMoviePosterStr; }

    public String getBackdropStr() { return mBackdropStr; }
}
