package com.franciscoolivero.android.roomerapp.Profile;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.franciscoolivero.android.roomerapp.QueryUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProfileLoader extends AsyncTaskLoader<List<Profile>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ProfileLoader.class.getName();
    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link ProfileLoader}.
     *
     * @param context of the activity
     * @param mUrl to load data from
     */
    public ProfileLoader(@NonNull Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG, "onStartLoading, forceLoad to execute loadInBackground");
        forceLoad();
    }

    @Nullable
    @Override
    public List<Profile> loadInBackground() {
        Log.d(LOG_TAG, "loadInBackground() executed");

        if(mUrl==null){
            return null;
        } else {
            Log.v(LOG_TAG, "mUrl wasn't null, fetchEartquakeData(mUrl) is triggered)");

            List<Profile> profiles = QueryUtils.fetchProfileData(mUrl);
            Log.v(LOG_TAG, "Books were correctly fetched, return the list so that onLoadFinished adds them to the adapter");
            return profiles;

        }
    }
}
