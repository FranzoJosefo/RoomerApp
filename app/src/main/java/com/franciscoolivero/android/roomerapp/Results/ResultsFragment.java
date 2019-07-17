package com.franciscoolivero.android.roomerapp.Results;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.franciscoolivero.android.roomerapp.Filters.Filter;
import com.franciscoolivero.android.roomerapp.ParserService;
import com.franciscoolivero.android.roomerapp.Profile.Profile;
import com.franciscoolivero.android.roomerapp.Profile.ProfileAdapter;
import com.franciscoolivero.android.roomerapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultsFragment extends Fragment {

    public ResultsFragment() {
        //Required empty constructor
    }

    @BindView(R.id.profile_list_view)
    ListView profileListView;
    @BindView(R.id.empty_view)
    RelativeLayout emptyStateView;
    @BindView(R.id.loading_spinner_container_results)
    View loadingSpinner;

    private String userToken;

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Create a new {@link android.widget.ArrayAdapter} of profiles.
     */
    private ProfileAdapter profileAdapter;
//    private ArrayList<Profile> dummyProfiles;

    /**
     * Constant value for the profile loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final String ROOMER_API_GET_RESULTS = "http://roomer-backend.herokuapp.com/apd/getUsuarios";
    private static String LOG_TAG = ResultsFragment.class.getSimpleName();
    private GoogleSignInAccount account;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String ROOMER_API_HOST = "roomer-backend.herokuapp.com";
    private static final String ROOMER_API_PATH_APD = "apd";
    private static final String ROOMER_API_PATH_GET_LIKES_TOKEN = "getLikesPorToken";
    private static final String ROOMER_API_POST_LIKE = "http://roomer-backend.herokuapp.com/apd/insertLike";
    private static final String ROOMER_API_POST_MATCH = "http://roomer-backend.herokuapp.com/apd/insertMatch";
    private static final String ROOMER_API_PATH_GET_USUARIOS_TOKEN = "getUsuariosPorToken";
    private static final String ROOMER_API_PATH_GET_FILTROS_TOKEN = "getFiltrosPorToken";
    private static final String ROOMER_API_GET_FILTERS = "http://roomer-backend.herokuapp.com/apd/getFiltros";

    private Profile myProfile;
    private Filter myFilter;
    private List<String> myLikes;
    private List<Profile> allProfiles;
    private List<Filter> allFilters;

    private String addedUserToken;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void generateDummyData() {
//        dummyProfiles = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Profile currentProfile = new Profile(
//                    "alfred@tengomail.com",
//                    "Alfredo_" + i,
//                    "Rodriguez_" + i,
//                    "M",
//                    "36412953" + i * 30000,
//                    "68302719" + i * 30000,
//                    54911,
//                    23 + (i * 3),
//                    "https://content-static.upwork.com/uploads/2014/10/01073427/profilephoto1.jpg",
//                    "Esto sigue en desarrollo");
//
//            dummyProfiles.add(currentProfile);
//        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null) {
            userToken = account.getEmail();
        }
        profileAdapter = new ProfileAdapter(getActivity().getBaseContext(), new ArrayList<>(), ResultsFragment.this);
        profileListView.setAdapter(profileAdapter);
        if (isConnected()) {
            fetchProfileData(ROOMER_API_GET_RESULTS, getContext());
        } else {
            Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null) {
            userToken = account.getEmail();
        }
        profileAdapter = new ProfileAdapter(getActivity().getBaseContext(), new ArrayList<>(), ResultsFragment.this);
        profileListView.setAdapter(profileAdapter);
        if (isConnected()) {
            fetchProfileData(ROOMER_API_GET_RESULTS, getContext());
        } else {
            Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    private void fetchProfileData(String requestUrl, Context context) {
        //The following try catch block generates a 1.5 second delay until we make the request so that we can see the Loading Spinner.
        try {
            loadingSpinner.setVisibility(View.VISIBLE);
            getUsuariosHTTPRequest(requestUrl);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with request: " + requestUrl, e);
            e.printStackTrace();
            //Todo Retry
            Toast.makeText(context, "Hubo un error al cargar, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }
    }

    private void getUsuariosHTTPRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the Usuarios JSON results", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String resultsResponse = response.body().string();
                Log.v(LOG_TAG, resultsResponse);
                //CALL NEW ResultParser method
//                List<Profile> profiles = ParserService.extractProfiles(resultsResponse, userToken);
                allProfiles = ParserService.extractProfiles(resultsResponse, userToken);
                //Null Check in case fragment gets detached from activity for long running operations.
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v(LOG_TAG, "ENTRO EN EL RUN DE UI THREAD");
//                            updateProfileAdapter(profiles);
                            fetchMyFilters();
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
                        }
                    });
                }

            }
        });
    }

    private void fetchMyFilters() {
        try {
            getFiltrosbyTokenHttpRequest(userToken);

        } catch (IOException e) {
            e.printStackTrace();
            //Todo Retry
            Toast.makeText(getContext(), "Hubo un error al cargar, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }
    }

    private void getFiltrosbyTokenHttpRequest(String mToken) throws IOException {
        Log.v(LOG_TAG, "mToken: " + mToken);

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(ROOMER_API_HOST)
                .addPathSegment(ROOMER_API_PATH_APD)
                .addPathSegment(ROOMER_API_PATH_GET_FILTROS_TOKEN)
                .addQueryParameter("token", mToken)
                .build();
        String url = httpUrl.toString();
        Log.v(LOG_TAG, "URL FOR GET FILTROS BY TOKEN: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the FILTROS JSON results", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String resultsResponse = response.body().string();
                Log.v(LOG_TAG, resultsResponse);
                List<Filter> myFilters = ParserService.extractFilters(resultsResponse);
                myFilter = myFilters.get(0);
                allProfiles = ParserService.extractProfilesComparedWithMyFilters(myFilter, allProfiles);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fetchAllFilters();
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
                        }
                    });
                }

            }
        });
    }

    private void fetchAllFilters() {
        try {
            getFiltrosHTTPRequest(ROOMER_API_GET_FILTERS);

        } catch (IOException e) {
            e.printStackTrace();
            //Todo Retry
            Toast.makeText(getContext(), "Hubo un error al cargar, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }
    }

    private void getFiltrosHTTPRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the Usuarios JSON results", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String resultsResponse = response.body().string();
                Log.v(LOG_TAG, resultsResponse);
                //CALL NEW ResultParser method
                allFilters = ParserService.extractFilters(resultsResponse);
                allProfiles = ParserService.extractProfilesMyFiltersUsersFilters(allFilters, myFilter, allProfiles);
                //Null Check in case fragment gets detached from activity for long running operations.
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v(LOG_TAG, "ENTRO EN EL RUN DE UI THREAD");
                            fetchMyProfile();
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
                        }
                    });
                }

            }
        });
    }


    public boolean insertLike(String mAddedUserToken) {
        addedUserToken = mAddedUserToken;
        String postBodyInsertarLike = "{\n" +
                "    \"token\": \"" + userToken + "\",\n" +
                "    \"like\": \"" + addedUserToken + "\"\n" +
                "}";

        try {
            postLikeRequest(ROOMER_API_POST_LIKE, postBodyInsertarLike);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error al likear, pruebe de nuevo!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void postLikeRequest(String postUrl, String postBody) throws IOException {

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem posting Profile information to Backend", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                Log.d("TAG", myResponse);


                try {
                    JSONObject json = new JSONObject(myResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkForAddedUserLikes();
                        Toast.makeText(getContext(), "Like exitoso!", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    private void checkForAddedUserLikes() {
        try {
            getAddedUserLikesbyTokenHTTPRequest(addedUserToken);
        } catch (IOException e) {
            Log.v(LOG_TAG, "Error in checkForAddedUserLikes - getAddedUserLikesbyToken Failed");
            e.printStackTrace();
        }
    }

    private void getAddedUserLikesbyTokenHTTPRequest(String token) throws IOException {
        Log.v(LOG_TAG, "token: " + token);

        String url;

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(ROOMER_API_HOST)
                .addPathSegment(ROOMER_API_PATH_APD)
                .addPathSegment(ROOMER_API_PATH_GET_LIKES_TOKEN)
                .addQueryParameter("token", token)
                .build();
        url = httpUrl.toString();
        Log.v(LOG_TAG, "URL FOR GET Added User LIKES BY TOKEN: " + url);


        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the Added User LIKES JSON results", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String resultsResponse = response.body().string();
                Log.v(LOG_TAG, resultsResponse);
                //CALL NEW ResultParser method
                List<String> addedUserLikes = ParserService.extractAddedUserLikes(resultsResponse);
                //Null Check in case fragment gets detached from activity for long running operations.
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkForMatch(addedUserLikes);
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
                        }
                    });
                }

            }
        });
    }

    private void checkForMatch(List<String> addedUserLikes) {
        boolean isMatch = false;
        for (String currentLike : addedUserLikes) {
            if (currentLike.equals(userToken)) {
                isMatch = true;
            }
        }
        if (isMatch) {
            insertMatch(addedUserToken, userToken);
            insertMatch(userToken, addedUserToken);
        }
    }

    private void insertMatch(String userToken, String matchToken) {
        String postBodyMatch = "{\n" +
                "    \"token\": \"" + userToken + "\",\n" +
                "    \"match\": \"" + matchToken + "\"\n" +
                "}";
        try {
            postMatchRequest(ROOMER_API_POST_MATCH, postBodyMatch);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error guardando informacion, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }
    }

    private void postMatchRequest(String postUrl, String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem posting Profile information to Backend", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                Log.d("TAG", myResponse);


                try {
                    JSONObject json = new JSONObject(myResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Match exitoso!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void fetchMyProfile() {
        try {
            getUsuariosbyTokenHTTPRequest(userToken);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error guardando informacion, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }

    }

    public void getUsuariosbyTokenHTTPRequest(String mToken) throws IOException {
        Log.v(LOG_TAG, "mToken: " + mToken);

        String url;

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(ROOMER_API_HOST)
                .addPathSegment(ROOMER_API_PATH_APD)
                .addPathSegment(ROOMER_API_PATH_GET_USUARIOS_TOKEN)
                .addQueryParameter("token", mToken)
                .build();
        url = httpUrl.toString();
        Log.v(LOG_TAG, "URL FOR GET USUARIOS BY TOKEN: " + url);


        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the Usuarios JSON results", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String resultsResponse = response.body().string();
                Log.v(LOG_TAG, resultsResponse);
                //CALL NEW ResultParser method
                List<Profile> profiles = ParserService.extractMyProfile(resultsResponse, userToken);
                myProfile = profiles.get(0);
                allProfiles = ParserService.extractProfilesComparedWithUsersFilters(allFilters, allProfiles, myProfile);
                //Null Check in case fragment gets detached from activity for long running operations.
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fetchMyLikes();
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
                        }
                    });
                }

            }
        });
    }

    private void fetchMyLikes() {
        try {
            getLikesbyTokenHTTPRequest(userToken);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error guardando informacion, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }

    }

    private void getLikesbyTokenHTTPRequest(String token) throws IOException {
        Log.v(LOG_TAG, "token: " + token);

        String url;

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(ROOMER_API_HOST)
                .addPathSegment(ROOMER_API_PATH_APD)
                .addPathSegment(ROOMER_API_PATH_GET_LIKES_TOKEN)
                .addQueryParameter("token", token)
                .build();
        url = httpUrl.toString();
        Log.v(LOG_TAG, "URL FOR GET Added User LIKES BY TOKEN: " + url);


        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the Added User LIKES JSON results", e);
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String resultsResponse = response.body().string();
                Log.v(LOG_TAG, resultsResponse);
                //CALL NEW ResultParser method
                myLikes = ParserService.extractAddedUserLikes(resultsResponse);
                allProfiles = ParserService.extractProfilesRemoveLikedUsers(allProfiles, myLikes);
                //Null Check in case fragment gets detached from activity for long running operations.
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateProfileAdapter(allProfiles);
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
                        }
                    });
                }

            }
        });
    }


    private void updateProfileAdapter(List<Profile> profileList) {
        profileAdapter.clear();
        if (profileList.isEmpty()) {
            loadingSpinner.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
            profileListView.setVisibility(View.VISIBLE);
            profileAdapter.notifyDataSetChanged();
        } else {
            emptyStateView.setVisibility(View.GONE);
            loadingSpinner.setVisibility(View.GONE);
            profileAdapter.addAll(profileList);
            profileListView.setVisibility(View.VISIBLE);
            profileAdapter.notifyDataSetChanged();
            Log.v(LOG_TAG, "AGREGO TODO AL PROFILE ADAPTER");
        }
    }
}


