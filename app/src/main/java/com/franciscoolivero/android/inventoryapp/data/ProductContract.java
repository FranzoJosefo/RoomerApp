package com.franciscoolivero.android.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Inventory app.
 */
public class ProductContract {
    //To prevent someone from accidentally instantiating the contract class,
    //give it an empty constructor.

    private ProductContract() {
    }

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.franciscoolivero.android.inventoryapp";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.products/products/ is a valid path for
     * looking at product data. content://com.example.android.products/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PRODUCTS = "products";

    /**
     * Inner class that defines constant values for the products database table.
     * Each entry in the table represents a single product.
     */
    public static final class ProductEntry implements BaseColumns {


        /**
         * Name of the database table for products
         */
        public static final String TABLE_NAME = "products";

        /**
         * The content URI to access the product data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * Unique ID number for the product (only for use in the database table)
         *
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of the the product
         *
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_NAME = "name";


        /**
         * Model of the the product
         *
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_MODEL = "model";

        /**
         * Available Quantity (Stock) of the product
         *
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";

        /**
         * Price of the product
         *
         * Type: INTEGER
         *
         */
        public static final String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Picture of the product
         *
         * Type: BLOB
         */
        public static final String COLUMN_PRODUCT_PICTURE = "picture";

        /**
         * Supplier name
         *
         * Type: String
         */
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "supplierName";

        /**
         * Supplier email
         *
         * Type: String
         */
        public static final String COLUMN_PRODUCT_SUPPLIER_EMAIL = "supplierEmail";

    }

}
