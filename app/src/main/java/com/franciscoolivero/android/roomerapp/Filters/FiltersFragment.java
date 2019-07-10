package com.franciscoolivero.android.roomerapp.Filters;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.franciscoolivero.android.roomerapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

public class FiltersFragment extends Fragment{

    public FiltersFragment() {
        //Required empty constructor
    }


    /**
     * Gender of the product. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private static final int PICK_IMAGE_REQUEST = 100;

//    @Override
//    public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> data) {
//
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<List<String>> loader) {
//
//    }

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

    /**
     * Create a new {@link android.widget.ArrayAdapter} of profiles.
     */
//    private RoomateAdapter profileAdapter;
//    private ArrayList<Roomate> savedRoomates;
    android.app.LoaderManager loaderManager;

    /**
     * Constant value for the profile loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    private static final String GOOGLE_BOOKS_BASE_URL = "https://www.googleapis.com/profiles/v1/volumes?q=";
    private String userQuery;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filters, container, false);
        ButterKnife.bind(this, rootView);



        //Avoid keyboard from opening focused on first EditText
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        product_name.setOnTouchListener(mTouchListener);
//        product_model.setOnTouchListener(mTouchListener);
//        product_price.setOnTouchListener(mTouchListener);
//        product_picture.setOnTouchListener(mTouchListener);
//        supplier_name.setOnTouchListener(mTouchListener);
//        supplier_email.setOnTouchListener(mTouchListener);

        mCurrentProductUri = getActivity().getIntent().getData();
        account = getActivity().getIntent().getParcelableExtra("account");

        // Spinner element
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        Spinner spinnerSexo = (Spinner) rootView.findViewById(R.id.spinnerSexo);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
//                    Toast.makeText(getContext(), item.toString(),
//                            Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getContext(), "Selected",
//                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        spinnerSexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
//                    Toast.makeText(getContext(), item.toString(),
//                            Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getContext(), "Selected",
//                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Otro");
        categories.add("F");
        categories.add("M");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // attaching data adapter to spinner
        spinnerSexo.setAdapter(dataAdapter);


        // Spinner Drop down elements
        List<String> categoriesBarrios = new ArrayList<String>();
        categoriesBarrios.add("Belgrano");
        categoriesBarrios.add("Avellaneda");
        categoriesBarrios.add("Palermo");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterSpinnerBarrio = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoriesBarrios);

        // Drop down layout style - list view with radio button
        dataAdapterSpinnerBarrio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapterSpinnerBarrio);

//        if (mCurrentProductUri == null) {
//            getActivity().setTitle(R.string.editor_activity_title_new_product);
//            // Invalidate the options menu, so the "Delete" menu option can be hidden.
//            // (It doesn't make sense to delete a product that hasn't been created yet.)
//            getActivity().invalidateOptionsMenu();
//        } else {
//            getActivity().setTitle(R.string.editor_activity_title_edit_product);
//            Log.v(LOG_TAG, mCurrentProductUri + " was passed as Intent Data to EditorActivity");
//            getActivity().getSupportLoaderManager().initLoader(PRODUCT_LOADER_ID, null, getActivity().this);
//
//        }

        return rootView;

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    //Gets fired after calling invalidateOptionsMenu()
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        // If this is a new product, hide the "Delete" menu item.
//        if ( == null) {
//            MenuItem menuItemDel = menu.findItem(R.id.action_delete);
//            menuItemDel.setVisible(false);
//            MenuItem menuItemBuy = menu.findItem(R.id.action_buy);
//            menuItemBuy.setVisible(false);
//        }
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setHomeButtonEnabled(false);      // Disable the button
//            actionBar.setDisplayHomeAsUpEnabled(false); // Remove the left caret
//            actionBar.setDisplayShowHomeEnabled(false); // Remove the icon
//        }
//        return true;
//    }


//    @Override
//    public void onBackPressed() {
//        // If the product hasn't changed, continue with handling back button press
//        if (!mProductHasChanged) {
//            super.onBackPressed();
//            return;
//        }
//
//        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
//        // Create a click listener to handle the user confirming that changes should be discarded.
//        DialogInterface.OnClickListener discardButtonClickListener =
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // User clicked "Discard" button, close the current activity.
//                        finish();
//                    }
//                };
//
//        // Show dialog that there are unsaved changes
//        showUnsavedChangesDialog(discardButtonClickListener);
//    }

//    @Override
//    public onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu options from the res/menu/menu_editor.xml file.
//        // This adds menu items to the app bar.
//        getMenuInflater().inflate(R.menu.menu_options, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // User clicked on a menu option in the app bar overflow menu
//        switch (item.getItemId()) {
//            // Respond to a click on the "Save" menu option
//            case R.id.action_save:
//                // Trigger saveProduct() method to save Product to DB.
//                //Could handle and validate errors here.
////                saveProduct();
//                // Exit Activity
////                finish();
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//                return true;
//            // Respond to a click on the "Delete" menu option
//            case R.id.action_delete:
////                showDeleteConfirmationDialog();
//                return true;
////            case R.id.action_buy:
////                String[] emailAddress = {supplier_email.getText().toString()};
////                String emailSubject = getResources().getString(R.string.email_order_request_subject) + " " + product_name.getText().toString();
////                composeEmail(emailAddress, emailSubject, createEmailBody());
////                return true;
//            // Respond to a click on the "Up" arrow button in the app bar
//            case android.R.id.home:
//                // If the product hasn't changed, continue with navigating up to parent activity
//                // which is the {@link CatalogActivity}.
//                if (!mProductHasChanged) {
//                    NavUtils.navigateUpFromSameTask(getActivity());
//
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
//                                NavUtils.navigateUpFromSameTask(getActivity());
//                            }
//                        };
//
//                // Show a dialog that notifies the user they have unsaved changes
//                showUnsavedChangesDialog(discardButtonClickListener);
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void showUnsavedChangesDialog(
//            DialogInterface.OnClickListener discardButtonClickListener) {
//        // Create an AlertDialog.Builder and set the message, and click listeners
//        // for the positive and negative buttons on the dialog.
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage(R.string.unsaved_changes_dialog_msg);
//        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
//        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User clicked the "Keep editing" button, so dismiss the dialog
//                // and continue editing the product.
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//            }
//        });
//
//        // Create and show the AlertDialog
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
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
//    public void onLoaderReset(android.content.Loader<List<Roomate>> loader) {
//        // Loader reset, so we can clear out our existing data.
//        Log.i(LOG_TAG, "Loader reset, clear the data from adapter");
//        loader.reset();
//        profileAdapter.clear();
//    }
//
//    public void openWebPage(Roomate profile) {
//        Uri profileUri = Uri.parse(profile.getmInfoLink());
//        Intent intent = new Intent(Intent.ACTION_VIEW, profileUri);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public android.content.Loader<List<Filter>> onCreateLoader(int i, Bundle bundle) {
//        Log.i(LOG_TAG, "No Loader was previously created OR loader was restarted, creating new RoomateLoader.");
////        return new RoomateLoader(this, userQuery);
//        return new;
//    }

//    @Override
//    public void onLoadFinished(android.content.Loader<List<Roomate>> loader, List<Roomate> profiles) {
//        loadingSpinner.setVisibility(View.GONE);
//        profileListView.setEmptyView(emptyStateView);
//        savedRoomates = new ArrayList<>(profiles);
//        savedRoomates.addAll(profiles);
//        // Clear the adapter of previous profile data
//        profileAdapter.clear();
//
//        // If there is a valid list of {@link Roomate}s, then add them to the adapter's
//        // data set. This will trigger the ListView to update.
//        Log.i(LOG_TAG, "Loading finished, add all Roomates to adapter so they can be displayed");
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


