package com.franciscoolivero.android.roomerapp.Matches;

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

import com.franciscoolivero.android.roomerapp.ParserService;
import com.franciscoolivero.android.roomerapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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
import okhttp3.Response;

public class MatchesFragment extends Fragment {

    public MatchesFragment() {
        //Required empty constructor
    }

    private final OkHttpClient client = new OkHttpClient();
    private static final String ROOMER_API_HOST = "roomer-backend.herokuapp.com";
    private static final String ROOMER_API_PATH_APD = "apd";
    private static final String ROOMER_API_PATH_GET_MATCHES_TOKEN = "getMatchPorToken";
    private static final String ROOMER_API_PATH_GET_USUARIOS_TOKEN = "getUsuariosPorToken";
    private static final String ROOMER_API_GET_RESULTS = "http://roomer-backend.herokuapp.com/apd/getUsuarios";
    private static final String ROOMER_API_POST_MATCHES = "http://roomer-backend.herokuapp.com/apd/insertFiltro";
    private String userToken;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private GoogleSignInAccount account;
    private static String LOG_TAG = MatchesFragment.class.getSimpleName();


    @BindView(R.id.matches_list_view)
    ListView matchesListView;
    @BindView(R.id.empty_view)
    RelativeLayout emptyStateView;
    @BindView(R.id.loading_spinner_container_matches)
    View loadingSpinner;


    /**
     * Create a new {@link android.widget.ArrayAdapter} of matchs.
     */
    static private MatchesAdapter matchAdapter;

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
        View rootView = inflater.inflate(R.layout.fragment_matches, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


//    private void generateDummyData() {
//        dummyMatchs = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Match currentMatch = new Match(
//                    "Fisurita",
//                    "Del Conurbano",
//                    25,
//                    "https://pbs.twimg.com/profile_images/757202149218607104/gyPP5Hyl.jpg",
//                    "superfisu@fisurismo.com",
//                    "1165120532",
//                    "0541");
//
//            dummyMatchs.add(currentMatch);
//        }
//    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            account = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (account != null) {
                userToken = account.getEmail();
            }
            matchAdapter = new MatchesAdapter(getActivity().getBaseContext(), new ArrayList<>());
            matchesListView.setAdapter(matchAdapter);
            if (isConnected()) {
                fetchMatches(ROOMER_API_PATH_GET_MATCHES_TOKEN, getContext());
            } else {
                Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            account = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (account != null) {
                userToken = account.getEmail();
            }
            matchAdapter = new MatchesAdapter(getActivity().getBaseContext(), new ArrayList<>());
            matchesListView.setAdapter(matchAdapter);
            if (isConnected()) {
                fetchMatches(ROOMER_API_PATH_GET_MATCHES_TOKEN, getContext());
            } else {
                Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
            }
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

    private void fetchMatches(String requestUrl, Context context) {
        try {
            loadingSpinner.setVisibility(View.VISIBLE);
            if (isConnected()) {
                getMatchesHTTPRequest();
            } else {
                Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with request: " + requestUrl, e);
            e.printStackTrace();
            //Todo Retry
            Toast.makeText(context, "Hubo un error al cargar, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }
    }

    private void getMatchesHTTPRequest() throws IOException {
        String url;

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(ROOMER_API_HOST)
                .addPathSegment(ROOMER_API_PATH_APD)
                .addPathSegment(ROOMER_API_PATH_GET_MATCHES_TOKEN)
                .addQueryParameter("token", userToken)
                .build();
        url = httpUrl.toString();
        Log.v(LOG_TAG, "URL FOR GET MATCHES BY TOKEN: " + url);

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
                List<String> matchesTokens = ParserService.extractMatches(resultsResponse);
                Log.v(LOG_TAG, "Match Tokens Array: "+matchesTokens);


                //Null Check in case fragment gets detached from activity for long running operations.
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v(LOG_TAG, "ENTRO EN EL RUN DE UI THREAD");
                            if (matchesTokens != null) {
                                if (isConnected()) {
                                    fetchMatchesProfiles(getContext(), matchesTokens);
                                } else {
                                    Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
                                }

                            }
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
                        }
                    });
                }

            }
        });
    }

    private void fetchMatchesProfiles(Context context, List<String> tokenMatchStringArray) {
        List<Match> matches = new ArrayList<>();
        //The following try catch block generates a 1.5 second delay until we make the request so that we can see the Loading Spinner.
        try {
            if (tokenMatchStringArray.isEmpty()) {
                Log.v(LOG_TAG, "tokenMatchStringAarray is Empty");
                updateUIemptyMatches();
            } else {
                if (isConnected()) {
                    getMatchesbyTokenHTTPRequest(ROOMER_API_GET_RESULTS, tokenMatchStringArray);
                } else {
                    Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
                }
            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with request: " + ROOMER_API_GET_RESULTS, e);
            e.printStackTrace();
            //Todo Retry
            Toast.makeText(context, "Hubo un error al cargar, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }
    }

    private void getMatchesbyTokenHTTPRequest(String url, List<String> tokenMatchArray) throws IOException {

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
                List<Match> matches = ParserService.extractMatchesFromProfileJSON(resultsResponse, tokenMatchArray);

                //Null Check in case fragment gets detached from activity for long running operations.
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD - getuSUARIOSBYTOKENHTTP");
                            updateMatchesAdapter(matches);

                        }
                    });
                }

            }
        });
    }

    private void updateMatchesAdapter(List<Match> matches) {
        matchAdapter.clear();
        emptyStateView.setVisibility(View.GONE);
        loadingSpinner.setVisibility(View.GONE);
        matchAdapter.addAll(matches);
        matchesListView.setVisibility(View.VISIBLE);
        matchAdapter.notifyDataSetChanged();
        Log.v(LOG_TAG, "AGREGO TODO AL MATCH ADAPTER");
    }

    private void updateUIemptyMatches() {
        Log.v(LOG_TAG, "MatchList is empty");
        loadingSpinner.setVisibility(View.GONE);
        emptyStateView.setVisibility(View.VISIBLE);
    }

}


