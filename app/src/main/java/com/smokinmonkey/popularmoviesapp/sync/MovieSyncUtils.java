package com.smokinmonkey.popularmoviesapp.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by smokinMonkey on 5/19/2017.
 */

public class MovieSyncUtils {

    public static void startSync(@NonNull final Context context) {
        Intent intentToStartSync = new Intent(context, MovieSyncIntentService.class);
        context.startService(intentToStartSync);
    }
}
