package com.franciscoolivero.android.roomerapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.franciscoolivero.android.roomerapp.data.ProductContract.ProductEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Allows user to create a new product or edit an existing one.
 */
public class ProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Gender of the product. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private static final int PICK_IMAGE_REQUEST = 100;
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
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        //Avoid keyboard from opening focused on first EditText
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        product_name.setOnTouchListener(mTouchListener);
        product_model.setOnTouchListener(mTouchListener);
        product_price.setOnTouchListener(mTouchListener);
        product_picture.setOnTouchListener(mTouchListener);
        supplier_name.setOnTouchListener(mTouchListener);
        supplier_email.setOnTouchListener(mTouchListener);

        mCurrentProductUri = getIntent().getData();

        if (mCurrentProductUri == null) {
            setTitle(R.string.editor_activity_title_new_product);
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a product that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.editor_activity_title_edit_product);
            Log.v(LOG_TAG, mCurrentProductUri + " was passed as Intent Data to EditorActivity");
            getSupportLoaderManager().initLoader(PRODUCT_LOADER_ID, null, this);

        }


    }

    @Override
    //Gets fired after calling invalidateOptionsMenu()
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new product, hide the "Delete" menu item.
        if (mCurrentProductUri == null) {
            MenuItem menuItemDel = menu.findItem(R.id.action_delete);
            menuItemDel.setVisible(false);
            MenuItem menuItemBuy = menu.findItem(R.id.action_buy);
            menuItemBuy.setVisible(false);
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
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Trigger saveProduct() method to save Product to DB.
                //Could handle and validate errors here.
                saveProduct();
                // Exit Activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_buy:
                String[] emailAddress = {supplier_email.getText().toString()};
                String emailSubject = getResources().getString(R.string.email_order_request_subject) + " " + product_name.getText().toString();
                composeEmail(emailAddress, emailSubject, createEmailBody());
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the product hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(ProfileActivity.this);
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
                                NavUtils.navigateUpFromSameTask(ProfileActivity.this);
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

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
//                deleteProduct();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
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

    /**
     * Perform the deletion of the product in the database.
     */
//    private void deleteProduct() {
//        int rowsDeleted = 0;
//        if (mCurrentProductUri != null) {
//            rowsDeleted = getContentResolver().delete(mCurrentProductUri,
//                    null,
//                    null);
//        }
//        if (rowsDeleted > 0) {
//            Toast toast = Toast.makeText(this, R.string.editor_delete_product_successful, Toast.LENGTH_SHORT);
//            toast.show();
//            Log.v(LOG_TAG, "Success - # Of rows deleted: " + rowsDeleted);
//        } else {
//            Toast toast = Toast.makeText(this, R.string.editor_delete_product_failed, Toast.LENGTH_SHORT);
//            toast.show();
//            Log.v(LOG_TAG, "Failure - # Of rows deleted: " + rowsDeleted);
//        }
//    }

    /**
     * Insert or Update a product in the database.
     */
    private void saveProduct() {
        long affectedRowOrId;
        String sProduct_name = product_name.getText().toString().trim();
        String sProduct_model = product_model.getText().toString().trim();
        String sProduct_price = String.valueOf(product_price.getText()).trim();
        String sProduct_quantity = String.valueOf(product_quantity.getText()).trim();
        //Convert Drawable to bitmap
        Bitmap bitmapPicture = ((BitmapDrawable) product_picture.getDrawable()).getBitmap();
        //Create ByteArray from bitmap
        byte[] byteArrayPicture = getBitmapAsByteArray(bitmapPicture);
        String sSupplier_name = supplier_name.getText().toString().trim();
        String sSupplier_email = supplier_email.getText().toString().trim();

        if (TextUtils.isEmpty(sProduct_name) || TextUtils.isEmpty(sProduct_model) || TextUtils.isEmpty(sProduct_price) || TextUtils.isEmpty(sSupplier_name) || TextUtils.isEmpty(sSupplier_email)) {
            Toast toast = Toast.makeText(this, "Missing required fields, Try again.", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        float fProduct_price = 0;
        if (!TextUtils.isEmpty(sProduct_price)) {
            if (!isNumeric(sProduct_price)) {
                Toast toast = Toast.makeText(this, "Price must be a number, QA scum", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            fProduct_price = Float.parseFloat(sProduct_price);
            if (fProduct_price == 0) {
                Toast toast = Toast.makeText(this, "Price cannot be 0", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

        }

        // If the quantity is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int iProduct_quantity = 0;


        if (!TextUtils.isEmpty(sProduct_quantity)) {
            iProduct_quantity = Integer.parseInt(sProduct_quantity);
        }

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, sProduct_name);
        values.put(ProductEntry.COLUMN_PRODUCT_MODEL, sProduct_model);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, fProduct_price);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, iProduct_quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_PICTURE, byteArrayPicture);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, sSupplier_name);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL, sSupplier_email);


        if (mCurrentProductUri == null) {
            affectedRowOrId = insertProduct(values);
        } else {
            affectedRowOrId = updateProduct(values);
        }
        //Handles making a Toast in case of success and Error.
        makeToast(affectedRowOrId);

    }

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    /**
     * Insert a new product in the database.
     */
    private long insertProduct(ContentValues values) {
        // Insert the new row, returning the full Uri for the added row (scheme + content authority + path)
        Uri newUriId = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
        String strUriId = String.valueOf(newUriId);
        return Long.valueOf(strUriId.substring(strUriId.lastIndexOf('/') + 1));

    }

    /**
     * Update an existing product in the database.
     */
    private long updateProduct(ContentValues values) {
        return getContentResolver().update(mCurrentProductUri, values, null, null);
    }

    private void makeToast(long affectedRowOrId) {
        String success;
        String failure;
        String logMsg;
        if (mCurrentProductUri == null) {
            success = getString(R.string.editor_insert_product_successful);
            failure = getString(R.string.editor_insert_product_failed);
            logMsg = " with Row ID";
        } else {
            success = getString(R.string.editor_update_product_successful);
            failure = getString(R.string.editor_update_product_failed);
            logMsg = " - # of Rows updated: ";
        }

        if (affectedRowOrId > 0) {
            Toast toast = Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT);
            toast.show();
            Log.v(LOG_TAG, success + logMsg + affectedRowOrId);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), failure, Toast.LENGTH_SHORT);
            toast.show();
            Log.e(LOG_TAG, failure + logMsg + affectedRowOrId);
        }
    }

    /**
     * Handle clicking on the ImageButton to pick an image from the Gallery
     */
    public void pickImage(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * After a user selects an image from the Gallery, update the UI with new data or error if needed.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        selectedImage = null;
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    selectedImage = data.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                        //Check if image isn't 10MB(almost just in case) or bigger, if it is BLOB will cause conflict in DB, so return an Error Toast.
                        byte[] bitmapAsArray = getBitmapAsByteArray(bitmap);
                        int bitmapArrayLength = bitmapAsArray.length;
                        product_picture.setImageBitmap(bitmap);

                        if (bitmapArrayLength > 9999999) {
                            Toast toast = Toast.makeText(this, "Image must be smaller than 1MB", Toast.LENGTH_LONG);
                            toast.show();

                        } else {
                            product_picture.setImageBitmap(bitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public String createEmailBody() {
        String supplierName = supplier_name.getText().toString();
        String productName = product_name.getText().toString();
        String productModel = product_model.getText().toString();
        String emailBody = "Hi, " + supplierName + "\n\n" +
                "I'm writing to you to place an order for the following product: " + "\n\n" +
                "Product Name: " + productName + "\n" +
                "Product Model: " + productModel + "\n" +
                "Quantity: ";

        return emailBody;
    }

    public void composeEmail(String[] addresses, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void decreaseQuantity(View view) {
        String quantity = product_quantity.getText().toString();
        if (!TextUtils.isEmpty(quantity)) {
            int intQuantity = Integer.parseInt(quantity);
            if (intQuantity != 0) {
                intQuantity--;
                product_quantity.setText(String.valueOf(intQuantity));
                return;
            }
        }
        Toast.makeText(this, "Min stock limit reached", Toast.LENGTH_SHORT).show();
    }

    public void increaseQuantity(View view) {
        int intQuantity = 0;
        String quantity = product_quantity.getText().toString();
        if (!TextUtils.isEmpty(quantity)) {
            intQuantity = Integer.parseInt(quantity);
            if (intQuantity == 9999) {
                Toast.makeText(this, "Max stock limit reached", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        intQuantity++;
        product_quantity.setText(String.valueOf(intQuantity));
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader triggered");

        // Since the editor shows all product attributes, define a projection that contains
        // all columns from the product table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_MODEL,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_PICTURE,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,                 // Query the content URI for the current product
                projection,                     // Columns to include in the resulting Cursor
                null,                  // No selection clause
                null,               // No selection arguments
                null);                 // Default sort order
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "onLoadFinished triggered");

        //Check if onLoadFinished was called even after the user updated fields in Edit mode, if so return earlier so that when the user returns from the Image Gallery the views aren't overwritten with DB info.
        if (mProductHasChanged) {
            return;
        }
        //Figure out the index of each column
        if (cursor.getCount() <= 0) return;

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of product attributes that we're interested in
            int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int productModelColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_MODEL);
            int productQuantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int productPriceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int productPictureColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PICTURE);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierEmailColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL);


            String priceRounded = String.format("%.2f", cursor.getFloat(productPriceColumnIndex));

            byte[] byteArrayPicture = cursor.getBlob(productPictureColumnIndex);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayPicture, 0, byteArrayPicture.length);
            if (bitmap != null) {
                product_picture.setImageBitmap(bitmap);
            }

            // Extract out the value from the Cursor for the given column index
            // Update the views on the screen with the values from the database
            product_name.setText(cursor.getString(productNameColumnIndex));
            product_model.setText(cursor.getString(productModelColumnIndex));
            product_price.setText(priceRounded);
            product_quantity.setText(String.valueOf(cursor.getInt(productQuantityColumnIndex)));
            supplier_name.setText(cursor.getString(supplierNameColumnIndex));
            supplier_email.setText(cursor.getString(supplierEmailColumnIndex));

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.v(LOG_TAG, "onLoaderReset triggered");

        product_name.setText("");
        product_model.setText("");
        product_price.setText("");
        product_quantity.setText("");
        product_picture.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder_image));
        supplier_name.setText("");
        supplier_email.setText("");
    }

}