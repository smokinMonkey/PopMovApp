package com.smokinmonkey.popularmoviesapp.Utilities;

/**
 * Class to store all the values needed for each individual movie parsed
 * from json data.
 *
 * Created by smokinMonkey on 4/21/2017.
 */
public class Movie {

    private final String LOG_TAG = Movie.class.getSimpleName();

    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverview;
    private String mVoteAverage;
    private String mReleaseDate;

    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private String imagePosterStr = "";

    public Movie(String originalTitle, String posterPath, String overview,
                 String voteAvgerage, String releaseDate) {
        this.mOriginalTitle = originalTitle;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mVoteAverage = voteAvgerage;
        this.mReleaseDate = releaseDate;

        imagePosterStr = IMAGE_BASE_URL + mPosterPath;
    }

    public String getOriginalTitle() { return mOriginalTitle; }

    public String getOverview() { return mOverview; }

    public String getVoteAverage() { return mVoteAverage; }

    public String getReleaseDate() { return mReleaseDate; }

    public String getImagePosterStr() { return imagePosterStr; }

}
