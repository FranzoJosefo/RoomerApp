package com.franciscoolivero.android.roomerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.franciscoolivero.android.roomerapp.Filters.FiltersFragment;
import com.franciscoolivero.android.roomerapp.Matches.MatchesFragment;
import com.franciscoolivero.android.roomerapp.Profile.ProfileActivity;
import com.franciscoolivero.android.roomerapp.Results.ResultsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;

    private boolean filter_selected = false;

    private boolean results_selected = false;

    private boolean matches_selected = false;

    private String userToken;
    private GoogleSignInAccount account;


    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nav_bar);

        account = getIntent().getParcelableExtra("account");
        userToken = account.getEmail();

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Busquedas");
        loadFragment(new ResultsFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_busquedas:
                    filter_selected = false;
                    matches_selected = false;
                    results_selected = true;
                    fragment = new ResultsFragment();
                    loadFragment(fragment);
                    toolbar.setTitle("Busquedas");
                    invalidateOptionsMenu();
                    return true;
                case R.id.navigation_filtros:
                    filter_selected = true;
                    matches_selected = false;
                    results_selected = false;
                    fragment = new FiltersFragment();
                    loadFragment(fragment);
                    toolbar.setTitle("Filtros");
                    invalidateOptionsMenu();
                    return true;
                case R.id.navigation_matches:
                    filter_selected = false;
                    matches_selected = true;
                    results_selected = false;
                    fragment = new MatchesFragment();
                    loadFragment(fragment);
                    toolbar.setTitle("Matches");
                    invalidateOptionsMenu();
                    return true;

            }
            return false;
        }
    };




    //TODO IMPLEMENT MENU WITH OPTIONS : PROFILE / LOGOUT / HELP ?
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    //Gets fired after calling invalidateOptionsMenu()
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(!filter_selected) {
            MenuItem menuSave = menu.findItem(R.id.action_save);
            menuSave.setVisible(false);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);      // Disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // Remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // Remove the icon
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //TODO HANDLE FILTER SAVE (HOW DO I CALL SAVE FROM HERE TO FILTERS? :(
                // Trigger saveProduct() method to save Product to DB.
//                //Could handle and validate errors here.
////                saveProduct();
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
                // Exit Activity
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_logout:
//                showDeleteConfirmationDialog();
                return true;
            case R.id.nav_profile:
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                intentProfile.putExtra("account", account);
                intentProfile.putExtra("intentFromActivity", this.getClass().getSimpleName());
                startActivity(intentProfile);
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the product hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
//                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(MainActivity.this);
                    return true;
//                }

//                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
//                // Create a click listener to handle the user confirming that
//                // changes should be discarded.
//                DialogInterface.OnClickListener discardButtonClickListener =
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // User clicked "Discard" button, navigate to parent activity.
//                                NavUtils.navigateUpFromSameTask(FiltersActivity.this);
//                            }
//                        };
//
//                // Show a dialog that notifies the user they have unsaved changes
//                showUnsavedChangesDialog(discardButtonClickListener);
//                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
