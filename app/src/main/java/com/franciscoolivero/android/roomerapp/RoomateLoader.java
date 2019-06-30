//package com.franciscoolivero.android.roomerapp;
//
//import android.content.AsyncTaskLoader;
//import android.content.Context;
//import android.util.Log;
//
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//public class RoomateLoader extends AsyncTaskLoader<List<Roomate>> {
//
//    /** Tag for log messages */
//    private static final String LOG_TAG = RoomateLoader.class.getName();
//    /** Query URL */
//    private String mUrl;
//
//    /**
//     * Constructs a new {@link RoomateLoader}.
//     *
//     * @param context of the activity
//     * @param mUrl to load data from
//     */
//    public RoomateLoader(@NonNull Context context, String mUrl) {
//        super(context);
//        this.mUrl = mUrl;
//    }
//
//    @Override
//    protected void onStartLoading() {
//        Log.v(LOG_TAG, "onStartLoading, forceLoad to execute loadInBackground");
//        forceLoad();
//    }
//
//    @Nullable
//    @Override
//    public List<Roomate> loadInBackground() {
//        Log.d(LOG_TAG, "loadInBackground() executed");
//
//        if(mUrl==null){
//            return null;
//        } else {
//            Log.v(LOG_TAG, "mUrl wasn't null, fetchEartquakeData(mUrl) is triggered)");
//
//            List<Roomate> roomates = QueryUtils.fetchRoomateData(mUrl);
//            Log.v(LOG_TAG, "Roomates were correctly fetched, return the list so that onLoadFinished adds them to the adapter");
//            return roomates;
//
//        }
//    }
//}
