package com.smokinmonkey.popularmoviesapp.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by smokinMonkey on 6/12/2017.
 */

public class PreviewDBJsonUtils {

    private static final String LOG_TAG = PreviewDBJsonUtils.class.getSimpleName();

    // variables to store keys
    private static final String RESULTS = "results";
    // values for playing preview
    private static final String PREVIEW_ID = "id";
    private static final String PREVIEW_KEY = "key";
    private static final String PREVIEW_NAME = "name";
    private static final String PREVIEW_SITE = "site";
    private static final String PREVIEW_TYPE = "type";

    public static Preview[] getPreviewFromJson(Context context, String previewJsonStr)
            throws JSONException {

        JSONObject previewJson = new JSONObject(previewJsonStr);
        JSONArray jsonPreviewArray = previewJson.getJSONArray(RESULTS);

        Preview[] previews = new Preview[jsonPreviewArray.length()];

        for(int i = 0; i < jsonPreviewArray.length(); i++) {
            String id;
            String key;
            String name;
            String site;
            String type;

            JSONObject singlePreview = jsonPreviewArray.getJSONObject(i);

            id = singlePreview.getString(PREVIEW_ID);
            key = singlePreview.getString(PREVIEW_KEY);
            name = singlePreview.getString(PREVIEW_NAME);
            site = singlePreview.getString(PREVIEW_SITE);
            type = singlePreview.getString(PREVIEW_TYPE);

            previews[i] = new Preview(id, key, name, site, type);
        }

        return previews;
    }
}
