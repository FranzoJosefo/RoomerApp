package com.franciscoolivero.android.roomerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltersFragment extends Fragment {

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
    @BindView(R.id.edit_product_name)
    EditText product_name;
    @BindView(R.id.edit_product_model)
    EditText product_model;
    @BindView(R.id.edit_product_price)
    EditText product_price;
    @BindView(R.id.edit_product_quantity)
    EditText product_quantity;
    @BindView(R.id.edit_product_picture)
    ImageView product_picture;
    @BindView(R.id.edit_supplier_name)
    EditText supplier_name;
    @BindView(R.id.edit_supplier_email)
    EditText supplier_email;
    @BindView(R.id.btn_inc_quantity)
    Button btn_inc_quantity;
    @BindView(R.id.btn_dec_quantity)
    Button btn_dec_quantity;

    private boolean mProductHasChanged = false;
    private String LOG_TAG = getClass().getSimpleName();
    private Uri mCurrentProductUri;
    private Uri selectedImage;

    /**
     * Create a new {@link android.widget.ArrayAdapter} of books.
     */
//    private RoomateAdapter bookAdapter;
//    private ArrayList<Roomate> savedRoomates;
    android.app.LoaderManager loaderManager;

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    private static final String GOOGLE_BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
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

        product_name.setOnTouchListener(mTouchListener);
        product_model.setOnTouchListener(mTouchListener);
        product_price.setOnTouchListener(mTouchListener);
        product_picture.setOnTouchListener(mTouchListener);
        supplier_name.setOnTouchListener(mTouchListener);
        supplier_email.setOnTouchListener(mTouchListener);

        mCurrentProductUri = getActivity().getIntent().getData();

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

//    @Override
//    //Gets fired after calling invalidateOptionsMenu()
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        // If this is a new product, hide the "Delete" menu item.
//        if (mCurrentProductUri == null) {
//            MenuItem menuItemDel = menu.findItem(R.id.action_delete);
//            menuItemDel.setVisible(false);
//            MenuItem menuItemBuy = menu.findItem(R.id.action_buy);
//            menuItemBuy.setVisible(false);
//        }
//        return true;
//    }
//
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
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu options from the res/menu/menu_editor.xml file.
//        // This adds menu items to the app bar.
//        getMenuInflater().inflate(R.menu.menu_details, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // User clicked on a menu option in the app bar overflow menu
//        switch (item.getItemId()) {
//            // Respond to a click on the "Save" menu option
//            case R.id.action_save:
//                // Trigger saveProduct() method to save Product to DB.
//                //Could handle and validate errors here.
//                saveProduct();
//                // Exit Activity
//                finish();
//                return true;
//            // Respond to a click on the "Delete" menu option
//            case R.id.action_delete:
////                showDeleteConfirmationDialog();
//                return true;
//            case R.id.action_buy:
//                String[] emailAddress = {supplier_email.getText().toString()};
//                String emailSubject = getResources().getString(R.string.email_order_request_subject) + " " + product_name.getText().toString();
//                composeEmail(emailAddress, emailSubject, createEmailBody());
//                return true;
//            // Respond to a click on the "Up" arrow button in the app bar
//            case android.R.id.home:
//                // If the product hasn't changed, continue with navigating up to parent activity
//                // which is the {@link CatalogActivity}.
//                if (!mProductHasChanged) {
//                    NavUtils.navigateUpFromSameTask(FiltersFragment.this);
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
//                                NavUtils.navigateUpFromSameTask(FiltersActivity.this);
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

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
//        bookAdapter.clear();
//    }
//
//    public void openWebPage(Roomate book) {
//        Uri bookUri = Uri.parse(book.getmInfoLink());
//        Intent intent = new Intent(Intent.ACTION_VIEW, bookUri);
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
//    public void onLoadFinished(android.content.Loader<List<Roomate>> loader, List<Roomate> books) {
//        loadingSpinner.setVisibility(View.GONE);
//        bookListView.setEmptyView(emptyStateView);
//        savedRoomates = new ArrayList<>(books);
//        savedRoomates.addAll(books);
//        // Clear the adapter of previous book data
//        bookAdapter.clear();
//
//        // If there is a valid list of {@link Roomate}s, then add them to the adapter's
//        // data set. This will trigger the ListView to update.
//        Log.i(LOG_TAG, "Loading finished, add all Roomates to adapter so they can be displayed");
//
//        if (books != null && !books.isEmpty()) {
//            bookAdapter.addAll(books);
//            bookAdapter.notifyDataSetChanged();
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


