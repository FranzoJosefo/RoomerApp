package com.franciscoolivero.android.roomerapp.Filters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.franciscoolivero.android.roomerapp.MainActivity;
import com.franciscoolivero.android.roomerapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.ByteArrayOutputStream;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

/**
 * Allows user to create a new product or edit an existing one.
 */
public class FiltersActivity extends AppCompatActivity{

    /**
     * Gender of the product. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int PRODUCT_LOADER_ID = 0;
    // Find all relevant views that we will need to read user input from
//    @BindView(R.id.edit_user_name)
//    EditText product_name;
//    @BindView(R.id.edit_user_last_name)
//    EditText product_model;
//    @BindView(R.id.edit_product_price)
//    EditText product_price;
//    @BindView(R.id.edit_product_quantity)
//    EditText product_quantity;
//    @BindView(R.id.edit_product_picture)
//    ImageView product_picture;
//    @BindView(R.id.edit_supplier_name)
//    EditText supplier_name;
//    @BindView(R.id.edit_supplier_email)
//    EditText supplier_email;
//    @BindView(R.id.btn_inc_quantity)
//    Button btn_inc_quantity;
//    @BindView(R.id.btn_dec_quantity)
//    Button btn_dec_quantity;

    private boolean mProductHasChanged = false;
    private String LOG_TAG = getClass().getSimpleName();
    private Uri mCurrentProductUri;
    private Uri selectedImage;
    private GoogleSignInAccount account;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //Reduce the quality to avoid issues with BLOB (BLOB cannot exceed 1mb).
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return outputStream.toByteArray();
    }

    // OnTouchListener that listens for any user touches on a View, implying that they are modifying
    // the view, and we change the mProductHasChanged boolean to true.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ButterKnife.bind(this);

        loadFragment(new FiltersFragment(), "filtros");

//        //Avoid keyboard from opening focused on first EditText
//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
////        product_name.setOnTouchListener(mTouchListener);
////        product_model.setOnTouchListener(mTouchListener);
////        product_price.setOnTouchListener(mTouchListener);
////        product_picture.setOnTouchListener(mTouchListener);
////        supplier_name.setOnTouchListener(mTouchListener);
////        supplier_email.setOnTouchListener(mTouchListener);
//
        mCurrentProductUri = getIntent().getData();
        account = getIntent().getParcelableExtra("account");


        if (mCurrentProductUri == null) {
            setTitle(R.string.editor_activity_title_filters_create);
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a product that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.editor_activity_title_filters_create);
            Log.v(LOG_TAG, mCurrentProductUri + " was passed as Intent Data to EditorActivity");
//            getSupportLoaderManager().initLoader(PRODUCT_LOADER_ID, null, this);

        }


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
        // If this is a new product, hide the "Delete" menu item.
        if (mCurrentProductUri == null) {
            MenuItem menuItemDel = menu.findItem(R.id.nav_profile);
            menuItemDel.setVisible(false);
            MenuItem menuItemBuy = menu.findItem(R.id.action_logout);
            menuItemBuy.setVisible(false);
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
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
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
                        finish();
                    }
                } else {
                    Toast noInternetToast = Toast.makeText(this, "Revise su conexion a Internet", Toast.LENGTH_SHORT);
                    noInternetToast.show();
                }


                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.nav_profile:
//                showDeleteConfirmationDialog();
                return true;
            case R.id.action_logout:
                //TODO HANDLE LOGOUT
//                String[] emailAddress = {supplier_email.getText().toString()};
//                String emailSubject = getResources().getString(R.string.email_order_request_subject) + " " + product_name.getText().toString();
//                composeEmail(emailAddress, emailSubject, createEmailBody());
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the product hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(FiltersActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(FiltersActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }
        return super.onOptionsItemSelected(item);
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

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }



}