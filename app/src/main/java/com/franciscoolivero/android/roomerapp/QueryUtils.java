package com.franciscoolivero.android.roomerapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Query the Google Roomates API and return an {@link List<Roomate>} object to represent a list of Roomates.
     */
    public static List<Roomate> fetchRoomateData(String requestUrl) {

        //The following try catch block generates a 1.5 second delay until we make the request so that we can see the Loading Spinner.
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        /**
         * Extract relevant fields from the JSON response and create a {@link List<Roomate>} object
         */
        List<Roomate> roomates = extractRoomates(jsonResponse);

        /**
         * Return the {@link List<Roomate>}
         */
        return roomates;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
        }
        return url;
    }


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                badResponse = true;
                // I'm thinking I could somehow handle a bad response code such as 404 or 500 but not sure how to do it. HELP!
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the profile JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Roomate} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Roomate> extractRoomates(String jsonResponse) {
        List<Roomate> profiles = new ArrayList<Roomate>();

        //If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding profiles to
//        List<Roomate> profiles = new ArrayList<>();

//         Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
//         is formatted, a JSONException exception object will be thrown.
//         Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponse);
            Log.v("extractRoomates", "JsonRootObject created");
            JSONArray jsonArrayItems = jsonRootObject.getJSONArray("items");
            Log.v("extractRoomates", "jsonArrayItems created");
            for (int i = 0; i < jsonArrayItems.length(); i++) {
                JSONObject currentItem = jsonArrayItems.getJSONObject(i);
                JSONObject currentVolumeInfo = currentItem.getJSONObject("volumeInfo");
                JSONArray currentAuthorArray = currentVolumeInfo.optJSONArray("authors");
                JSONObject currentSaleInfo = currentItem.getJSONObject("saleInfo");

                String profileTitle = currentVolumeInfo.optString("title");
                String profileInfoLink = currentVolumeInfo.optString("infoLink");
                String profileRating = currentVolumeInfo.optString("averageRating");

                //Initialize all of below as null in case the Roomate doesn't contain any of those in the JSON response.
                String profileAmount = null;
                String profileCurrency = null;
                ArrayList<String> profileAuthors = new ArrayList<>();


                //Check if the profile is for sale, if it is retrieve amount (price) and currency code, else leave those as null.
                String saleability = currentSaleInfo.getString("saleability");
                if (saleability.equals(FOR_SALE)) {
                    JSONObject currentListPrice = currentSaleInfo.getJSONObject("listPrice");
                    profileAmount = currentListPrice.getString("amount");
                    profileCurrency = currentListPrice.getString("currencyCode");
                }

                //Check if Author Array is available, some profiles may not list the authors.
                if (currentAuthorArray != null) {
                    String currentAuthor;
                    for (int y = 0; y < currentAuthorArray.length(); y++) {
                        currentAuthor = currentAuthorArray.optString(y);
                        profileAuthors.add(currentAuthor);
                    }
                }


//                Roomate profile = new Roomate(profileTitle, profileAuthors, profileInfoLink, profileAmount, profileCurrency, profileRating);
//                profiles.add(profile);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing Json results in extractRoomates()", e);
        }

        //Return the list of profiles
        return profiles;
    }

}
