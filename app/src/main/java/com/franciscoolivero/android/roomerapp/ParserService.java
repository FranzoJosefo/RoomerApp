package com.franciscoolivero.android.roomerapp;

import android.text.TextUtils;
import android.util.Log;

import com.franciscoolivero.android.roomerapp.Filters.Filter;
import com.franciscoolivero.android.roomerapp.Matches.Match;
import com.franciscoolivero.android.roomerapp.Profile.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParserService {

    private static String LOG_TAG = ParserService.class.getSimpleName();
    private GoogleSignIn account;


    //TODO add filterResults(List<Profile> profiles);

    public static List<Profile> extractProfiles(String jsonResponse, String userToken) {
        List<Profile> profileArray = new ArrayList<>();
        Log.v(LOG_TAG + ": in extractProfiles()", "profileArray<Profile> created");

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        try {
            JSONArray jsonRootArray = new JSONArray(jsonResponse);
            Log.v(LOG_TAG + ": in extractProfiles()", "jsonRootArray created");

            //Recorrer Array de abajo hacia arriba asi siempre leemos primero los ultimos en agregarse
            final int numberOfUsers = jsonRootArray.length() - 1;
            for (int i = numberOfUsers; i >= 0; i--) {
                JSONObject currentProfileJSONObject = jsonRootArray.getJSONObject(i);

                String currentToken = currentProfileJSONObject.optString("token");
                String currentName = currentProfileJSONObject.optString("nombre");
                String currentLastName = currentProfileJSONObject.optString("apellido");
                String currentGender = currentProfileJSONObject.optString("sexo");
                int currentAge = currentProfileJSONObject.optInt("edad");
                String currentDNI = currentProfileJSONObject.optString("dni");
                String currentPhone = currentProfileJSONObject.optString("telefono");
                int currentAreaCode = currentProfileJSONObject.optInt("codArea");
                String currentPictureUrl = currentProfileJSONObject.optString("foto");
                String currentDescription = currentProfileJSONObject.optString("descripcion");

                Profile currentProfile = new Profile(
                        currentToken,
                        currentName,
                        currentLastName,
                        currentGender,
                        currentDNI,
                        currentPhone,
                        currentAreaCode,
                        currentAge,
                        currentPictureUrl,
                        currentDescription
                );


                if (!ParserService.profileAlreadyAdded(profileArray, currentProfile) && !isLoggedUserProfile(currentProfile, userToken)) {
                    profileArray.add(currentProfile);
                }

                Log.v(LOG_TAG + ": in extractProfiles()", "Profile with token {" + currentToken + "} added to profileArray");
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG + ": in extractProfiles()", "ERROR Parsing JSON");
            e.printStackTrace();
            //TODO error handling maybe return exception? throws... etc
        }

        Log.v(LOG_TAG + ": in extractProfiles()", "Return profileArray");
        Log.v(LOG_TAG + ": in extractProfiles()", profileArray.toString());
        return profileArray;
    }

    private static boolean profileAlreadyAdded(List<Profile> profileArray, Profile currentProfile) {
        for (Profile profileTemp : profileArray) {
            if (currentProfile.getmToken().equals(profileTemp.getmToken())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLoggedUserProfile(Profile currentProfile, String userToken) {
        if (currentProfile.getmToken().equals(userToken)) {
            return true;
        }
        return false;
    }


    public static List<Filter> extractFilters(String jsonResponse) {
        List<Filter> filterArray = new ArrayList<>();
        Log.v(LOG_TAG + ": in exrtactFilters()", "filterArray<Filter> created");

        if (TextUtils.isEmpty(jsonResponse)) {
            Log.v(LOG_TAG + ": in exrtactFilters()", "jsonResponse is empty :/");
            return null;
        }

        try {
            JSONArray jsonRootArray = new JSONArray(jsonResponse);
            Log.v(LOG_TAG + ": in extractFilters()", "jsonRootArray created");

            //Recorrer Array de abajo hacia arriba asi siempre leemos primero los ultimos en agregarse
            final int numberOfFilters = jsonRootArray.length() - 1;
            for (int i = numberOfFilters; i >= 0; i--) {
                JSONObject currentFilterJSONObject = jsonRootArray.getJSONObject(i);

                String currentToken = currentFilterJSONObject.optString("token");
                String currentBarrio = currentFilterJSONObject.optString("barrio");
                int currentDinMin = currentFilterJSONObject.optInt("dineroMin");
                int currentDinMax = currentFilterJSONObject.optInt("dineroMax");
                int currentAgeMin = currentFilterJSONObject.optInt("edadMin");
                int currentAgeMax = currentFilterJSONObject.optInt("edadMax");
                String currentGender = currentFilterJSONObject.optString("sexo");

                Filter currentFilter = new Filter(
                        currentToken,
                        currentBarrio,
                        currentDinMax,
                        currentAgeMin,
                        currentAgeMax,
                        currentGender,
                        currentDinMin);


                if (!ParserService.filterAlreadyAdded(filterArray, currentFilter)) {
                    filterArray.add(currentFilter);
                }

                Log.v(LOG_TAG + ": in extractFilters()", "Filter with token {" + currentToken + "} added to filtersArray");
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG + ": in extractFilters()", "ERROR Parsing JSON");
            e.printStackTrace();
            //TODO error handling maybe return exception? throws... etc
        }

        Log.v(LOG_TAG + ": in extractFilters()", "Return filterArray");
        Log.v(LOG_TAG + ": in extractFilters()", filterArray.toString());
        return filterArray;
    }

    private static boolean filterAlreadyAdded(List<Filter> filterArray, Filter currentFilter) {
        for (Filter filterTemp : filterArray) {
            if (currentFilter.getmToken().equals(filterTemp.getmToken())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> extractMatches(String jsonResponse) {
        List<String> matchStringArray = new ArrayList<>();
        Log.v(LOG_TAG + ": in exrtactMatches()", "matchArray<Filter> created");

        if (TextUtils.isEmpty(jsonResponse)) {
            Log.v(LOG_TAG + ": in exrtactMatches()", "jsonResponse is empty :/");
            return null;
        }

        try {
            JSONArray jsonRootArray = new JSONArray(jsonResponse);
            Log.v(LOG_TAG + ": in extractMatches()", "jsonRootArray created");

            //Recorrer Array de abajo hacia arriba asi siempre leemos primero los ultimos en agregarse
            final int numberOfMatches = jsonRootArray.length() - 1;
            for (int i = numberOfMatches; i >= 0; i--) {
                JSONObject currentMatchJSONObject = jsonRootArray.getJSONObject(i);
                String currentMatch = currentMatchJSONObject.optString("match");
                matchStringArray.add(currentMatch);
                Log.v(LOG_TAG + ": in extractMatches()", "Match with token {" + currentMatch + "} added to matchStringArray");
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG + ": in extractMatches()", "ERROR Parsing JSON");
            e.printStackTrace();
            //TODO error handling maybe return exception? throws... etc
        }

        Log.v(LOG_TAG + ": in extractMatches()", "Return matchStringArray");
        Log.v(LOG_TAG + ": in extractMatches()", matchStringArray.toString());
        return matchStringArray;
    }

    public static List<Match> extractMatchesFromProfileJSON(String jsonResponse, List<String> matchTokensList) {
        List<Match> matchList = new ArrayList<>();
        Log.v(LOG_TAG + ": in extractProfiles()", "profileArray<Profile> created");

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        try {
            JSONArray jsonRootArray = new JSONArray(jsonResponse);
            Log.v(LOG_TAG + ": in extractProfiles()", "jsonRootArray created");

            //Recorrer Array de abajo hacia arriba asi siempre leemos primero los ultimos en agregarse
            final int profileArraySize = jsonRootArray.length() - 1;
            for (int i = profileArraySize; i >= 0; i--) {

                JSONObject currentMatchJSONObject = jsonRootArray.getJSONObject(i);

                String currentMail = currentMatchJSONObject.optString("token");
                for (String curToken : matchTokensList) {
                    if (currentMail.equals(curToken)) {
                        String currentName = currentMatchJSONObject.optString("nombre");
                        String currentLastName = currentMatchJSONObject.optString("apellido");
                        int currentAge = currentMatchJSONObject.optInt("edad");
                        String currentPictureUrl = currentMatchJSONObject.optString("foto");
                        String currentPhone = currentMatchJSONObject.optString("telefono");
                        int currentAreaCode = currentMatchJSONObject.optInt("codArea");

                        Match currentMatch = new Match(
                                currentName,
                                currentLastName,
                                currentAge,
                                currentPictureUrl,
                                currentMail,
                                currentPhone,
                                currentAreaCode);

                        if (!matchAlreadyAdded(matchList, currentMatch)) {
                            matchList.add(currentMatch);
                        }
                        Log.v(LOG_TAG + ": in extractMProfile()", "Profile with token {" + currentMail + "} added to profileArray");
                    }

                }

            }

        } catch (
                JSONException e) {
            Log.e(LOG_TAG + ": in extractMProfile()", "ERROR Parsing JSON");
            e.printStackTrace();
            //TODO error handling maybe return exception? throws... etc
        }

        return matchList;
    }

    private static boolean matchAlreadyAdded(List<Match> matchArray, Match currentMatch) {
        for (Match matchTemp : matchArray) {
            if (currentMatch.getmMail().equals(matchTemp.getmMail())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> extractAddedUserLikes(String jsonResponse) {
        List<String> addedUserLikesArray = new ArrayList<>();
        Log.v(LOG_TAG + ": in extAddedUsrLikes()", "matchArray<Filter> created");

        if (TextUtils.isEmpty(jsonResponse)) {
            Log.v(LOG_TAG + ": in extAddedUsrLikes()", "jsonResponse is empty :/");
            return null;
        }

        try {
            JSONArray jsonRootArray = new JSONArray(jsonResponse);
            Log.v(LOG_TAG + ": in extAddedUsrLikes()", "jsonRootArray created");

            //Recorrer Array de abajo hacia arriba asi siempre leemos primero los ultimos en agregarse
            final int numberOfLikes = jsonRootArray.length() - 1;
            for (int i = numberOfLikes; i >= 0; i--) {
                JSONObject currentLikeJSONOBject = jsonRootArray.getJSONObject(i);
                String currentLike = currentLikeJSONOBject.optString("like");
                addedUserLikesArray.add(currentLike);
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG + ": in extAddedUsrLikes()", "ERROR Parsing JSON");
            e.printStackTrace();
            //TODO error handling maybe return exception? throws... etc
        }

        Log.v(LOG_TAG + ": in extAddedUsrLikes()", "Return addedUserLikesArray");
        Log.v(LOG_TAG + ": in extAddedUsrLikes()", addedUserLikesArray.toString());
        return addedUserLikesArray;
    }
}
