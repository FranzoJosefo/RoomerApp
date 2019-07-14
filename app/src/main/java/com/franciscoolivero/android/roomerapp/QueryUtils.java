package com.franciscoolivero.android.roomerapp;

import okhttp3.OkHttpClient;

/**
 * Helper methods related to requesting and receiving profiles data from Google Roomates API.
 */
public final class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getName();
    private static final String ROOMER_API_HOST = "roomer-backend.herokuapp.com";
    private static final String ROOMER_API_PATH_APD = "apd";
    private static final String ROOMER_API_PATH_GET_USUARIOS_TOKEN = "getUsuariosPorToken";
    public static final OkHttpClient client = new OkHttpClient();
    public static boolean badResponse = false;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class named QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

//    public static void getUsuariosHTTPRequest(String url, String mToken, Activity activity) throws IOException {
//        Log.v(LOG_TAG, "mToken: " + mToken);
//
//        if(activity.getClass().getSimpleName().equals(ProfileActivity.class.getSimpleName())){
//            HttpUrl httpUrl = new HttpUrl.Builder()
//                    .scheme("http")
//                    .host(ROOMER_API_HOST)
//                    .addPathSegment(ROOMER_API_PATH_APD)
//                    .addPathSegment(ROOMER_API_PATH_GET_USUARIOS_TOKEN)
//                    .addQueryParameter("token", mToken)
//                    .build();
//            url = httpUrl.toString();
//            Log.v(LOG_TAG, "URL FOR GET USUARIOS BY TOKEN: " + url);
//        }
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(LOG_TAG, "Problem retrieving the Usuarios JSON results", e);
//                call.cancel();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                final String resultsResponse = response.body().string();
//                Log.v(LOG_TAG, resultsResponse);
//                //CALL NEW ResultParser method
//                List<Profile> profiles = ParserService.extractProfiles(resultsResponse);
//                //Null Check in case fragment gets detached from activity for long running operations.
//                if(activity!=null){
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(activity.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())){
//                                Log.v(LOG_TAG, "ENTRO Results Fragment");
//                                ResultsFragment resultsFragment = ResultsFragment.getInstance();
//                                resultsFragment.updateProfileAdapter(profiles);
//                            } else if(activity.getClass().getSimpleName().equals(SignInActivity.class.getSimpleName())) {
//                                //TODO handle in Sign In when user already created -> Pass Profiles and ask for null.
//                            } else if(activity.getClass().getSimpleName().equals(ProfileActivity.class.getSimpleName())){
//                                ProfileActivity profileActivity = ProfileActivity.getInstance();
//                                profileActivity.updateUIProfileLoaded(profiles);
//
//                            }
//                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
//                        }
//                    });
//                }
//
//            }
//        });
//    }



}
