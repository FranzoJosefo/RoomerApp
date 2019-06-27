package com.franciscoolivero.android.roomerapp.Matches;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.franciscoolivero.android.roomerapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchesFragment extends Fragment {

    public MatchesFragment() {
        //Required empty constructor
    }

    FragmentManager fragmentManager;

    @BindView(R.id.matches_list_view)
    ListView matchesListView;
    @BindView(R.id.empty_view)
    RelativeLayout emptyStateView;
    @BindView(R.id.loading_spinner_matches)
    View loadingSpinner;
//    @BindView(R.id.toolbar)
//    android.support.v7.widget.Toolbar toolbar;
//    @BindView(R.id.text_home_default)
//TextView homeDefaultMessage;

    /**
     * Create a new {@link android.widget.ArrayAdapter} of matchs.
     */
    private MatchesAdapter matchAdapter;
    private ArrayList<Match> savedMatchs;
    private ArrayList<Match> dummyMatchs;
    LoaderManager loaderManager;

    /**
     * Constant value for the match loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int PROFILE_LOADER_ID = 1;
    private static final String ROOMER_API_BASE_URL = "https://www.googleapis.com/matchs/v1/volumes?q=";
    private String userQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_matches, container, false);
        ButterKnife.bind(this, rootView);
//        setSupportActionBar(toolbar);
        // Get a reference to the LoaderManager, in order to interact with loaders.
        loaderManager = androidx.loader.app.LoaderManager.getInstance(this);


        if (savedInstanceState != null && savedInstanceState.<Match>getParcelableArrayList("myKey") != null) {
//            homeDefaultMessage.setVisibility(View.GONE);
            matchAdapter = new MatchesAdapter(getContext(), new ArrayList<>());


            savedMatchs = savedInstanceState.getParcelableArrayList("myKey");




            //OPEN WEB PAGE
//            matchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Match currentMatch = matchAdapter.getItem(position);
//                    openWebPage(currentMatch);
//                }
//            });
        }
//        //Test Dummy Matchs
//        generateDummyData();
//        matchAdapter = new MatchAdapter(getActivity(), new ArrayList<>());
//        matchAdapter.addAll(dummyMatchs);
//        matchAdapter.notifyDataSetChanged();
//        matchListView.setAdapter(matchAdapter);


        return rootView;

    }


    private void generateDummyData() {
        dummyMatchs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Match currentMatch = new Match(
                    "Fisurita",
                    "Del Conurbano",
                    25,
                    "https://pbs.twimg.com/profile_images/757202149218607104/gyPP5Hyl.jpg",
                    "superfisu@fisurismo.com",
                    "1165120532",
                    "0541");

            dummyMatchs.add(currentMatch);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        generateDummyData();
        matchAdapter = new MatchesAdapter(getActivity().getBaseContext(), new ArrayList<>());
        matchAdapter.addAll(dummyMatchs);
        matchAdapter.notifyDataSetChanged();
        emptyStateView.setVisibility(View.GONE);
        matchesListView.setAdapter(matchAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        if (savedMatchs != null) {
            savedState.putParcelableArrayList("myKey", savedMatchs);
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
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
//                // Create a new adapter that takes an empty list of Matchs as input
//                matchAdapter = new MatchAdapter(getApplicationContext(), new ArrayList<Match>());
//
//                homeDefaultMessage.setVisibility(View.GONE);
//                loadingSpinner.setVisibility(View.VISIBLE);
//                if (!isConnected()) {
//                    loadingSpinner.setVisibility(View.GONE);
//                    matchListView.setEmptyView(emptyStateView);
//                    emptyStateView.setText(R.string.no_inet);
//
//                } else {
//                    // Set the adapter on the {@link ListView}
//                    // so the list can be populated in the user interface
//                    matchListView.setAdapter(matchAdapter);
//
//                    matchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Match currentMatch = matchAdapter.getItem(position);
//                            openWebPage(currentMatch);
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
//    public void onLoaderReset(android.content.Loader<List<Match>> loader) {
//        // Loader reset, so we can clear out our existing data.
//        Log.i(LOG_TAG, "Loader reset, clear the data from adapter");
//        loader.reset();
//        matchAdapter.clear();
//    }
//
//    public void openWebPage(Match match) {
//        Uri matchUri = Uri.parse(match.getmInfoLink());
//        Intent intent = new Intent(Intent.ACTION_VIEW, matchUri);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public android.content.Loader<List<Match>> onCreateLoader(int i, Bundle bundle) {
//        Log.i(LOG_TAG, "No Loader was previously created OR loader was restarted, creating new MatchLoader.");
//        return new MatchLoader(this, userQuery);
//    }
//
//    @Override
//    public void onLoadFinished(android.content.Loader<List<Match>> loader, List<Match> matchs) {
//        loadingSpinner.setVisibility(View.GONE);
//        matchListView.setEmptyView(emptyStateView);
//        savedMatchs = new ArrayList<>(matchs);
//        savedMatchs.addAll(matchs);
//        // Clear the adapter of previous match data
//        matchAdapter.clear();
//
//        // If there is a valid list of {@link Match}s, then add them to the adapter's
//        // data set. This will trigger the ListView to update.
//        Log.i(LOG_TAG, "Loading finished, add all Matchs to adapter so they can be displayed");
//
//        if (matchs != null && !matchs.isEmpty()) {
//            matchAdapter.addAll(matchs);
//            matchAdapter.notifyDataSetChanged();
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


