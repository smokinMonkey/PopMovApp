package com.smokinmonkey.popularmoviesapp.utilities;

import java.io.Serializable;

/**
 * Created by smokinMonkey on 6/12/2017.
 */

public class Review implements Serializable {

    private final String LOG_TAG = Review.class.getSimpleName();

    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    public Review(String id, String author, String content, String url) {
        this.mId = id;
        this.mAuthor = author;
        this.mContent = content;
        this.mUrl = url;
    }

    public String getReviewId() { return mId; }
    public String getReviewAuthor() { return mAuthor; }
    public String getReviewContent() { return mContent; }
    public String getReviewUrl() { return mUrl; }

}
