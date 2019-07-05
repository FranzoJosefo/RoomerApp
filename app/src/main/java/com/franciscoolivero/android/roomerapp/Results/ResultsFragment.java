package com.franciscoolivero.android.roomerapp.Results;

import android.app.Activity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
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
    @BindView(R.id.loading_spinner)
    View loadingSpinner;
//    @BindView(R.id.toolbar)
//    android.support.v7.widget.Toolbar toolbar;
//    @BindView(R.id.text_home_default)
//TextView homeDefaultMessage;

    /**
     * Create a new {@link android.widget.ArrayAdapter} of profiles.
     */
    static private ProfileAdapter profileAdapter;
    private ArrayList<Profile> savedProfiles;
    private ArrayList<Profile> dummyProfiles;
    LoaderManager loaderManager;

    /**
     * Constant value for the profile loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int PROFILE_LOADER_ID = 1;
    private static final String ROOMER_API_BASE_URL = "https://www.googleapis.com/profiles/v1/volumes?q=";
    private static final String ROOMER_API_GET_RESULTS = "http://roomer-backend.herokuapp.com/apd/getUsuarios";
    private String userQuery;
    private static String LOG_TAG = ResultsFragment.class.getSimpleName();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, rootView);
//        setSupportActionBar(toolbar);
        // Get a reference to the LoaderManager, in order to interact with loaders.
//        loaderManager = androidx.loader.app.LoaderManager.getInstance(this);





        if (savedInstanceState != null && savedInstanceState.<Profile>getParcelableArrayList("myKey") != null) {
//            homeDefaultMessage.setVisibility(View.GONE);
            profileAdapter = new ProfileAdapter(getContext(), new ArrayList<>());


            savedProfiles = savedInstanceState.getParcelableArrayList("myKey");


            //OPEN WEB PAGE
//            profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Profile currentProfile = profileAdapter.getItem(position);
//                    openWebPage(currentProfile);
//                }
//            });
        }
//        //Test Dummy Profiles
//        generateDummyData();
//        profileAdapter = new ProfileAdapter(getActivity(), new ArrayList<>());
//        profileAdapter.addAll(dummyProfiles);
//        profileAdapter.notifyDataSetChanged();
//        profileListView.setAdapter(profileAdapter);


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
        fetchProfileData(ROOMER_API_GET_RESULTS, getActivity(), getContext());
//        generateDummyData();
        profileAdapter = new ProfileAdapter(getActivity().getBaseContext(), new ArrayList<>());
        profileListView.setAdapter(profileAdapter);
        emptyStateView.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        if (savedProfiles != null) {
            savedState.putParcelableArrayList("myKey", savedProfiles);
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    public void getUsuariosHTTPRequest(String url, Activity activity) throws IOException {

        OkHttpClient client = new OkHttpClient();

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


    private void fetchProfileData(String requestUrl, Activity activity, Context context) {
        //The following try catch block generates a 1.5 second delay until we make the request so that we can see the Loading Spinner.
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            getUsuariosHTTPRequest(requestUrl, activity);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with request: "+requestUrl, e);
            e.printStackTrace();
            //Todo Retry
            Toast.makeText(context, "Hubo un error al cargar, pruebe de nuevo!", Toast.LENGTH_LONG).show();
        }
    }






    private static void updateProfileAdapter(List<Profile> profileList){
        profileAdapter.addAll(profileList);
        profileAdapter.notifyDataSetChanged();
        Log.v(LOG_TAG, "AGREGO TODO AL PROFILE ADAPTER");
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the options menu from XML
//        MenuInflater inflater = getContext().getMenuInflater();
//        inflater.inflate(R.menu.menu_toolbar, menu);
//
//        // Get the SearchView
//        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
//
//        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                searchView.clearFocus();
//                // Create a new adapter that takes an empty list of Profiles as input
//                profileAdapter = new ProfileAdapter(getApplicationContext(), new ArrayList<Profile>());
//
//                homeDefaultMessage.setVisibility(View.GONE);
//                loadingSpinner.setVisibility(View.VISIBLE);
//                if (!isConnected()) {
//                    loadingSpinner.setVisibility(View.GONE);
//                    profileListView.setEmptyView(emptyStateView);
//                    emptyStateView.setText(R.string.no_inet);
//
//                } else {
//                    // Set the adapter on the {@link ListView}
//                    // so the list can be populated in the user interface
//                    profileListView.setAdapter(profileAdapter);
//
//                    profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Profile currentProfile = profileAdapter.getItem(position);
//                            openWebPage(currentProfile);
//                        }
//                    });
//                    s = s.replaceAll(" ", "%20");
//                    userQuery = GOOGLE_BOOKS_BASE_URL + s + "&maxResults=40";
//
//                    startLoader();
//
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
//
//        return true;
//    }

//    private void startLoader() {
//
//        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
//        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
//        // because this activity implements the LoaderCallbacks interface).
//        Log.i(LOG_TAG, "Loader will be initialized. If it doesn't exist, create loader, if else reuse.");
//        if (loaderManager == null) {
//            loaderManager.initLoader(BOOK_LOADER_ID, null, this).forceLoad();
//        } else {
//            loaderManager.restartLoader(BOOK_LOADER_ID, null, this);
//        }
//
//        Log.i(LOG_TAG, "Loader Initialized.");
//    }


//    @Override
//    public void onLoaderReset(android.content.Loader<List<Profile>> loader) {
//        // Loader reset, so we can clear out our existing data.
//        Log.i(LOG_TAG, "Loader reset, clear the data from adapter");
//        loader.reset();
//        profileAdapter.clear();
//    }
//
//    public void openWebPage(Profile profile) {
//        Uri profileUri = Uri.parse(profile.getmInfoLink());
//        Intent intent = new Intent(Intent.ACTION_VIEW, profileUri);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public android.content.Loader<List<Profile>> onCreateLoader(int i, Bundle bundle) {
//        Log.i(LOG_TAG, "No Loader was previously created OR loader was restarted, creating new ProfileLoader.");
//        return new ProfileLoader(this, userQuery);
//    }
//
//    @Override
//    public void onLoadFinished(android.content.Loader<List<Profile>> loader, List<Profile> profiles) {
//        loadingSpinner.setVisibility(View.GONE);
//        profileListView.setEmptyView(emptyStateView);
//        savedProfiles = new ArrayList<>(profiles);
//        savedProfiles.addAll(profiles);
//        // Clear the adapter of previous profile data
//        profileAdapter.clear();
//
//        // If there is a valid list of {@link Profile}s, then add them to the adapter's
//        // data set. This will trigger the ListView to update.
//        Log.i(LOG_TAG, "Loading finished, add all Profiles to adapter so they can be displayed");
//
//        if (profiles != null && !profiles.isEmpty()) {
//            profileAdapter.addAll(profiles);
//            profileAdapter.notifyDataSetChanged();
//        }
//
//        if (QueryUtils.badResponse) {
//            emptyStateView.setText(R.string.bad_response);
//            //Set badResponse to false again, to avoid constantly entering into this If statement after the user received a bad response.
//            QueryUtils.badResponse = false;
//        } else {
//            emptyStateView.setText(R.string.empty_state);
//        }
//    }
}


