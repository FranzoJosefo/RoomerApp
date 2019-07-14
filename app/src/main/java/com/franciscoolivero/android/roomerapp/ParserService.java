package com.franciscoolivero.android.roomerapp;

import android.text.TextUtils;
import android.util.Log;

import com.franciscoolivero.android.roomerapp.Filters.Filter;
import com.franciscoolivero.android.roomerapp.Profile.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParserService {

    private static String LOG_TAG = ParserService.class.getSimpleName();


    //TODO add filterResults(List<Profile> profiles);

    public static List<Profile> extractProfiles(String jsonResponse) {
        List<Profile> profileArray = new ArrayList<>();
        Log.v(LOG_TAG + ": in extractProfiles()", "profileArray<Profile> created");

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        try {
            JSONArray jsonRootArray = new JSONArray(jsonResponse);
            Log.v(LOG_TAG + ": in extractProfiles()", "jsonRootArray created");

            //Recorrer Array de abajo hacia arriba asi siempre leemos primero los ultimos en agregarse
            final int numberOfUsers = jsonRootArray.length()-1;
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


                if(!ParserService.profileAlreadyAdded(profileArray, currentProfile)){
                    profileArray.add(currentProfile);
                }

                Log.v(LOG_TAG + ": in extractProfiles()", "Profile with token {"+currentToken+"} added to profileArray");
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

    private static boolean profileAlreadyAdded(List<Profile> profileArray, Profile currentProfile){
        for (Profile profileTemp:profileArray) {
            if(currentProfile.getmToken().equals(profileTemp.getmToken())){
                return true;
            }
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
            final int numberOfFilters = jsonRootArray.length()-1;
            for (int i = numberOfFilters; i >= 0; i--) {
                JSONObject currentProfileJSONObject = jsonRootArray.getJSONObject(i);

                String currentToken = currentProfileJSONObject.optString("token");
                String currentBarrio = currentProfileJSONObject.optString("barrio");
                int currentDinMin = currentProfileJSONObject.optInt("dineroMin");
                int currentDinMax = currentProfileJSONObject.optInt("dineroMax");
                int currentAgeMin = currentProfileJSONObject.optInt("edadMin");
                int currentAgeMax = currentProfileJSONObject.optInt("edadMax");
                String currentGender = currentProfileJSONObject.optString("sexo");

                Filter currentFilter = new Filter(
                        currentToken,
                        currentBarrio,
                        currentDinMax,
                        currentAgeMin,
                        currentAgeMax,
                        currentGender,
                        currentDinMin);


                if(!ParserService.filterAlreadyAdded(filterArray, currentFilter)){
                    filterArray.add(currentFilter);
                }

                Log.v(LOG_TAG + ": in extractProfiles()", "Profile with token {"+currentToken+"} added to profileArray");
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG + ": in extractProfiles()", "ERROR Parsing JSON");
            e.printStackTrace();
            //TODO error handling maybe return exception? throws... etc
        }

        Log.v(LOG_TAG + ": in extractProfiles()", "Return profileArray");
        Log.v(LOG_TAG + ": in extractProfiles()", filterArray.toString());
        return filterArray;
    }

    private static boolean filterAlreadyAdded(List<Filter> filterArray, Filter currentFilter){
        for (Filter filterTemp:filterArray) {
            if(currentFilter.getmToken().equals(filterTemp.getmToken())){
                return true;
            }
        }
        return false;
    }
}
