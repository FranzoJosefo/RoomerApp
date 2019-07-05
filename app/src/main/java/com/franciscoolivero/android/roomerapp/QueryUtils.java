package com.franciscoolivero.android.roomerapp;

/**
 * Helper methods related to requesting and receiving profiles data from Google Roomates API.
 */
public final class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getName();
    private static final String FOR_SALE = "FOR_SALE";
    public static boolean badResponse = false;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class named QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }



}
