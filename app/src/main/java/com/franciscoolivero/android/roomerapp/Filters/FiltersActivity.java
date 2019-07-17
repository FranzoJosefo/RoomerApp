package com.franciscoolivero.android.roomerapp.Filters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.franciscoolivero.android.roomerapp.MainActivity;
import com.franciscoolivero.android.roomerapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

/**
 * Allows user to create a new product or edit an existing one.
 */
public class FiltersActivity extends AppCompatActivity {

    private boolean mProductHasChanged = false;
    private GoogleSignInAccount account;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ButterKnife.bind(this);

        loadFragment(new FiltersFragment(), "filtros");
        setTitle(R.string.editor_activity_title_filters_create);
        // Invalidate the options menu, so the "Delete" menu option can be hidden.
        // (It doesn't make sense to delete a product that hasn't been created yet.)
        invalidateOptionsMenu();
    }

    private void loadFragment(Fragment fragment, String tag) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    //Gets fired after calling invalidateOptionsMenu()
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItemDel = menu.findItem(R.id.nav_profile);
        menuItemDel.setVisible(false);
        MenuItem menuItemBuy = menu.findItem(R.id.action_logout);
        menuItemBuy.setVisible(false);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);      // Disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // Remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // Remove the icon
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                hideKeyboard(this);
                // Trigger saveProduct() method to save Product to DB.
                //Could handle and validate errors here.
//                saveProduct();
                if(isConnected()){
                    FiltersFragment fragment = (FiltersFragment) getSupportFragmentManager().findFragmentByTag("filtros");
                    if(fragment.saveFilters()){
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                        // Exit Activity
                    }
                } else {
                    Toast noInternetToast = Toast.makeText(this, "Revise su conexion a Internet", Toast.LENGTH_SHORT);
                    noInternetToast.show();
                }


                return true;
            // Respond to a click on the "Delete" menu option
        }
        return super.onOptionsItemSelected(item);
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

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}