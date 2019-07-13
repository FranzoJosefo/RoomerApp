package com.franciscoolivero.android.roomerapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.franciscoolivero.android.roomerapp.Filters.FiltersFragment;
import com.franciscoolivero.android.roomerapp.Matches.MatchesFragment;
import com.franciscoolivero.android.roomerapp.Profile.ProfileActivity;
import com.franciscoolivero.android.roomerapp.Results.ResultsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
        return account.getEmail();
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nav_bar);

        account = getIntent().getParcelableExtra("account");
        if (account != null) {
            userToken = account.getEmail();
        }

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Busquedas");
        loadFragment(new ResultsFragment(), "busquedas");
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
                    loadFragment(fragment, "busquedas");
                    toolbar.setTitle("Busquedas");
                    invalidateOptionsMenu();
                    return true;
                case R.id.navigation_filtros:
                    filter_selected = true;
                    matches_selected = false;
                    results_selected = false;
                    fragment = new FiltersFragment();
                    loadFragment(fragment, "filtros");
                    toolbar.setTitle("Filtros");
                    invalidateOptionsMenu();
                    return true;
                case R.id.navigation_matches:
                    filter_selected = false;
                    matches_selected = true;
                    results_selected = false;
                    fragment = new MatchesFragment();
                    loadFragment(fragment, "matches");
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
        if (!filter_selected) {
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
    public void onBackPressed() {
        // If the product hasn't changed, continue with handling back button press
        FiltersFragment fragment = (FiltersFragment) getSupportFragmentManager().findFragmentByTag("filtros");
        if (fragment != null && fragment.isVisible()) {
            if (fragment.ismProductHasChanged()) {
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, close the current activity.
                                filter_selected = false;
                                matches_selected = false;
                                results_selected = true;
                                ResultsFragment resultsFragment;
                                resultsFragment = new ResultsFragment();
                                loadFragment(resultsFragment, "busquedas");
                                toolbar.setTitle("Busquedas");
                                invalidateOptionsMenu();
                            }
                        };

                // Show dialog that there are unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                hideKeyboard(this);
                if(isConnected()){
                    FiltersFragment fragment = (FiltersFragment) getSupportFragmentManager().findFragmentByTag("filtros");
                    fragment.saveFilters();
                } else {
                    Toast noInternetToast = Toast.makeText(this, "Revise su conexion a Internet", Toast.LENGTH_SHORT);
                    noInternetToast.show();
                }

                return true;
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
//                    NavUtils.navigateUpFromSameTask(MainActivity.this);
//                    return true;
//                }
//
//                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
//                // Create a click listener to handle the user confirming that
//                // changes should be discarded.
//                DialogInterface.OnClickListener discardButtonClickListener =
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // User clicked "Discard" button, navigate to parent activity.
//                                filter_selected = false;
//                                matches_selected = false;
//                                results_selected = true;
//                                ResultsFragment fragment;
//                                fragment = new ResultsFragment();
//                                loadFragment(fragment, "busquedas");
//                                toolbar.setTitle("Busquedas");
//                                invalidateOptionsMenu();
//                            }
//                        };
//
//                // Show a dialog that notifies the user they have unsaved changes
//                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void loadFragment(Fragment fragment, String tag) {
        // load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
