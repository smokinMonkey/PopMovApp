package com.smokinmonkey.popularmoviesapp.utilities;

import java.io.Serializable;

/**
 * Created by smokinMonkey on 6/12/2017.
 */

public class Preview implements Serializable {

    private final String LOG_TAG = Preview.class.getSimpleName();

    private String mId;
    private String mKey;
    private String mName;
    private String mSite;
    private String mType;

    public Preview(String id, String key, String name, String site, String type) {
        this.mId = id;
        this.mKey = key;
        this.mName = name;
        this.mSite = site;
        this.mType = type;
    }

    public String getPreviewId() { return mId; }
    public String getPreviewKey() { return mKey; }
    public String getPreviewName() { return mName; }
    public String getPreviewSite() { return mSite; }
    public String getPreviewType() { return mType; }

}
