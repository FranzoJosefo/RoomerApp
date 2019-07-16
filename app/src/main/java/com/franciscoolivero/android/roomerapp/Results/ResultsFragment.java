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

import com.franciscoolivero.android.roomerapp.ParserService;
import com.franciscoolivero.android.roomerapp.Profile.Profile;
import com.franciscoolivero.android.roomerapp.Profile.ProfileAdapter;
import com.franciscoolivero.android.roomerapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResultsFragment extends Fragment {

    public ResultsFragment() {
        //Required empty constructor
    }

    FragmentManager fragmentManager;

    @BindView(R.id.profile_list_view)
    ListView profileListView;
    @BindView(R.id.empty_view)
    RelativeLayout emptyStateView;
    @BindView(R.id.loading_spinner_container_results)
    View loadingSpinner;
//    @BindView(R.id.toolbar)
//    android.support.v7.widget.Toolbar toolbar;
//    @BindView(R.id.text_home_default)
//TextView homeDefaultMessage;

    public String userToken;

    public static final OkHttpClient client = new OkHttpClient();

    /**
     * Create a new {@link android.widget.ArrayAdapter} of profiles.
     */
    static private ProfileAdapter profileAdapter;
    private ArrayList<Profile> dummyProfiles;

    /**
     * Constant value for the profile loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int PROFILE_LOADER_ID = 1;
    private static final String ROOMER_API_BASE_URL = "https://www.googleapis.com/profiles/v1/volumes?q=";
    private static final String ROOMER_API_GET_RESULTS = "http://roomer-backend.herokuapp.com/apd/getUsuarios";
    private String userQuery;
    private static String LOG_TAG = ResultsFragment.class.getSimpleName();
    private GoogleSignInAccount account;

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
        dummyProfiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Profile currentProfile = new Profile(
                    "alfred@tengomail.com",
                    "Alfredo_" + i,
                    "Rodriguez_" + i,
                    "M",
                    "36412953" + i * 30000,
                    "68302719" + i * 30000,
                    54911,
                    23 + (i * 3),
                    "https://content-static.upwork.com/uploads/2014/10/01073427/profilephoto1.jpg",
                    "Esto sigue en desarrollo");

            dummyProfiles.add(currentProfile);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account!=null){
            userToken = account.getEmail();
        }
        profileAdapter = new ProfileAdapter(getActivity().getBaseContext(), new ArrayList<>());
        profileListView.setAdapter(profileAdapter);
        if(isConnected()){
            fetchProfileData(ROOMER_API_GET_RESULTS, getContext());
        } else {
            Toast.makeText(getContext(), "Verifique la conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if(account!=null){
            userToken = account.getEmail();
        }
        profileAdapter = new ProfileAdapter(getActivity().getBaseContext(), new ArrayList<>());
        profileListView.setAdapter(profileAdapter);
        if(isConnected()){
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
                List<Profile> profiles = ParserService.extractProfiles(resultsResponse);
                //Null Check in case fragment gets detached from activity for long running operations.
                if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v(LOG_TAG, "ENTRO EN EL RUN DE UI THREAD");
                            updateProfileAdapter(profiles);
                            Log.v(LOG_TAG, "SALIENDO DEL RUN DE UI THREAD");
                        }
                    });
                }

            }
        });
    }


    private void fetchProfileData(String requestUrl, Context context) {
        //The following try catch block generates a 1.5 second delay until we make the request so that we can see the Loading Spinner.
        try {
            loadingSpinner.setVisibility(View.VISIBLE);
            getUsuariosHTTPRequest(requestUrl);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with request: "+requestUrl, e);
            e.printStackTrace();
            //Todo Retry
            Toast.makeText(context, "Hubo un error al cargar, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateProfileAdapter(List<Profile> profileList){
        profileAdapter.clear();
        if(profileList.isEmpty()){
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


