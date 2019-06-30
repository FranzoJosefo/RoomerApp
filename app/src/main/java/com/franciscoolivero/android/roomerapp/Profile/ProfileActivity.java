package com.franciscoolivero.android.roomerapp.Profile;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.franciscoolivero.android.roomerapp.Filters.FiltersActivity;
import com.franciscoolivero.android.roomerapp.R;
import com.franciscoolivero.android.roomerapp.data.ProductContract.ProductEntry;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Allows user to create a new product or edit an existing one.
 */
public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * Gender of the product. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int PRODUCT_LOADER_ID = 0;
    // Find all relevant views that we will need to read user input from
    @BindView(R.id.edit_user_name)
    EditText user_name;
    @BindView(R.id.edit_user_last_name)
    EditText user_last_name;
    @BindView(R.id.edit_user_dni)
    EditText user_dni;
    @BindView(R.id.edit_user_age)
    EditText user_age;
    @BindView(R.id.spinner_gender)
    Spinner user_spinner_gender;
    @BindView(R.id.edit_user_area_code)
    EditText user_area_code;
    @BindView(R.id.edit_user_phone)
    EditText user_phone;
    @BindView(R.id.edit_user_picture)
    ImageButton user_image;

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
    public String postUrl = "http://roomer-backend.herokuapp.com/apd/insertUsuario";


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private boolean mProductHasChanged = false;
    private String LOG_TAG = getClass().getSimpleName();
    private Uri mCurrentAccount;
    private Uri selectedImage;
    private String currentAccountGoogleEmail;
    private String currentAccountImageURL;
    private String userToken;
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
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        //Avoid keyboard from opening focused on first EditText
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        user_name.setOnTouchListener(mTouchListener);
        user_last_name.setOnTouchListener(mTouchListener);
        user_age.setOnTouchListener(mTouchListener);
        user_dni.setOnTouchListener(mTouchListener);
        user_phone.setOnTouchListener(mTouchListener);
        user_spinner_gender.setOnTouchListener(mTouchListener);
        user_area_code.setOnTouchListener(mTouchListener);
        user_image.setOnTouchListener(mTouchListener);


        account = getIntent().getParcelableExtra("account");

        //TODO add if something like mRedirectedFromLogin once Profile menu is implemented.
        setTitle(R.string.editor_activity_title_profile_create);
        if (account != null) {
            user_name.setText(account.getGivenName());
            user_last_name.setText(account.getFamilyName());
            currentAccountGoogleEmail = account.getEmail();
            currentAccountImageURL = account.getPhotoUrl().toString();
            userToken = account.getEmail();
            Log.v(LOG_TAG, "IdToken for email:" + account.getEmail() + "\nis "+ account.getIdToken());

                    Glide.with(user_image.getContext())
                            .load(currentAccountImageURL)
                            .into(user_image);

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a product that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.editor_activity_title_profile_edit);
            Log.v(LOG_TAG, account + " was passed as Intent Data to EditorActivity");
            //TODO Delete
//            getSupportLoaderManager().initLoader(PRODUCT_LOADER_ID, null, this);

            //TODO Make a GET request
//            http://roomer-backend.herokuapp.com/apd/getUsuariosPorToken?token=thisIsAtoken

        }


        List<String> categories = new ArrayList<String>();
        categories.add("Seleccionar");
        categories.add("Otro");
        categories.add("F");
        categories.add("M");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        user_spinner_gender.setAdapter(dataAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    //Gets fired after calling invalidateOptionsMenu()
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new product, hide the "Delete" menu item.
        if (mCurrentAccount == null) {
            MenuItem menuItemDel = menu.findItem(R.id.action_delete);
            menuItemDel.setVisible(false);
            MenuItem menuItemBuy = menu.findItem(R.id.action_buy);
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
                if (saveProduct()) {
                    // Exit Activity

                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_buy:
//                String[] emailAddress = {supplier_email.getText().toString()};
                String emailSubject = getResources().getString(R.string.email_order_request_subject) + " " + user_name.getText().toString();
//                composeEmail(emailAddress, emailSubject, createEmailBody());
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the product hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mProductHasChanged) {
                    //This will take me to wherever I came from. Not to be used in this class and method ;).
//                    NavUtils.navigateUpFromSameTask(ProfileActivity.this);
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
    private boolean saveProduct() {
        String sUser_name = user_name.getText().toString().trim();
        String sUser_last_name = user_last_name.getText().toString().trim();
        String sUser_age = String.valueOf(user_age.getText()).trim();
        String sUser_gender = user_spinner_gender.getSelectedItem().toString();
        String sUser_phone = String.valueOf(user_phone.getText()).trim();
        String sUser_area_code = String.valueOf(user_area_code.getText()).trim();
        String sUser_dni = String.valueOf(user_dni.getText()).trim();
//        //Convert Drawable to bitmap
//        Bitmap bitmapPicture = ((BitmapDrawable) user_image.getDrawable()).getBitmap();
//        //Create ByteArray from bitmap
//        byte[] byteArrayPicture = getBitmapAsByteArray(bitmapPicture);

        if (TextUtils.isEmpty(sUser_name)
                || TextUtils.isEmpty(sUser_last_name)
                || TextUtils.isEmpty(sUser_age)
                || sUser_gender.equals("Seleccionar")
                || TextUtils.isEmpty(sUser_phone)
                || TextUtils.isEmpty(sUser_area_code)
                || TextUtils.isEmpty(sUser_dni)) {
            Toast toast = Toast.makeText(this, "Todos los campos son requeridos.", Toast.LENGTH_SHORT);
            toast.show();

            return false;
        }

        if (!TextUtils.isEmpty(sUser_age)) {
            if (!isNumeric(sUser_age)) {
                Toast toast = Toast.makeText(this, "Price must be a number, QA scum", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

        }

        String postBodyInsertarUsuario = "{\n" +
                "    \"token\": \"" + userToken + "\",\n" +
                "    \"nombre\": \"" + sUser_name + "\",\n" +
                "    \"apellido\": \"" + sUser_last_name + "\",\n" +
                "    \"sexo\": \"" + sUser_gender + "\",\n" +
                "    \"edad\": \"" + sUser_age + "\",\n" +
                "    \"dni\": \"" + sUser_dni + "\",\n" +
                "    \"telefono\": \"" + sUser_phone + "\",\n" +
                "    \"codArea\": \"" + sUser_area_code + "\",\n" +
                "    \"foto\": \"" + currentAccountImageURL + "\",\n" +
                "    \"descripcion\": \"Esto todavia esta bajo construccion\"\n" +
                "}";

        try {
            postRequest(postUrl, postBodyInsertarUsuario);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        //Handles making a Toast in case of success and Error.
//        makeToast(affectedRowOrId);
        return true;

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
//    private long updateProduct(ContentValues values) {
//        return getContentResolver().update(mCurrentProductUri, values, null, null);
//    }
    private void makeToast(long affectedRowOrId) {
        String success;
        String failure;
        String logMsg;
        if (mCurrentAccount == null) {
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
//                        product_picture.setImageBitmap(bitmap);

                        if (bitmapArrayLength > 9999999) {
                            Toast toast = Toast.makeText(this, "Image must be smaller than 1MB", Toast.LENGTH_LONG);
                            toast.show();

                        } else {
//                            product_picture.setImageBitmap(bitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

//    public String createEmailBody() {
//        String supplierName = supplier_name.getText().toString();
//        String productName = user_name.getText().toString();
//        String productModel = user_last_name.getText().toString();
//        String emailBody = "Hi, " + supplierName + "\n\n" +
//                "I'm writing to you to place an order for the following product: " + "\n\n" +
//                "Product Name: " + productName + "\n" +
//                "Product Model: " + productModel + "\n" +
//                "Quantity: ";
//
//        return emailBody;
//    }

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


//    public void decreaseQuantity(View view) {
//        String quantity = product_quantity.getText().toString();
//        if (!TextUtils.isEmpty(quantity)) {
//            int intQuantity = Integer.parseInt(quantity);
//            if (intQuantity != 0) {
//                intQuantity--;
//                product_quantity.setText(String.valueOf(intQuantity));
//                return;
//            }
//        }
//        Toast.makeText(this, "Min stock limit reached", Toast.LENGTH_SHORT).show();
//    }

//    public void increaseQuantity(View view) {
//        int intQuantity = 0;
//        String quantity = product_quantity.getText().toString();
//        if (!TextUtils.isEmpty(quantity)) {
//            intQuantity = Integer.parseInt(quantity);
//            if (intQuantity == 9999) {
//                Toast.makeText(this, "Max stock limit reached", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//        intQuantity++;
//        product_quantity.setText(String.valueOf(intQuantity));
//    }


    public void postRequest(String postUrl, String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                Log.d("TAG", myResponse);


                try {
                    JSONObject json = new JSONObject(myResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUIProfileSaved();

//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    private void updateUIProfileSaved() {
        Toast.makeText(this, "POST was succesfull", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, FiltersActivity.class);
        startActivity(intent);
    }

}