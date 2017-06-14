package com.smokinmonkey.popularmoviesapp.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by smokinMonkey on 6/12/2017.
 */

public class ReviewDBJsonUtils {

    private static final String LOG_TAG = ReviewDBJsonUtils.class.getSimpleName();

    // variable to store keys
    private static final String RESULTS = "results";
    // vlaues for movie reviews
    private static final String REVIEW_ID = "id";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_URL = "url";

    public static Review[] getReviewFromJson(Context context, String reviewJsonStr)
            throws JSONException{
        JSONObject reviewJson = new JSONObject(reviewJsonStr);
        JSONArray jsonReviewArray = reviewJson.getJSONArray(RESULTS);
        Review[] reviews = new Review[jsonReviewArray.length()];

        for (int i = 0; i < jsonReviewArray.length(); i++) {
            String id;
            String author;
            String content;
            String url;

            JSONObject singleReview = jsonReviewArray.getJSONObject(i);

            id = singleReview.getString(REVIEW_ID);
            author = singleReview.getString(REVIEW_AUTHOR);
            content = singleReview.getString(REVIEW_CONTENT);
            url = singleReview.getString(REVIEW_URL);

            reviews[i] = new Review(id, author, content, url);
        }

        return reviews;
    }
}
