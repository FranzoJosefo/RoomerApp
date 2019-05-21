package com.franciscoolivero.android.roomerapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.franciscoolivero.android.roomerapp.data.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = this.getClass().getName();

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    //CREATE TABLE products (
    // _id INTEGER PRIMARY KEY AUTOINCREMENT,
    // name TEXT NOT NULL,
    // model TEXT NOT NULL,
    // price INTEGER NOT NULL,
    // quantity INTEGER NOT NULL DEFAULT 0,
    // picture BLOB
    // supplierName TEXT NOT NULL
    // supplierEmail TEXT NOT NULL);

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + "("
            + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
            + ProductEntry.COLUMN_PRODUCT_MODEL + " TEXT NOT NULL, "
            + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
            + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
            + ProductEntry.COLUMN_PRODUCT_PICTURE + " BLOB DEFAULT NULL, "
            + ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL,"
            + ProductEntry.COLUMN_PRODUCT_SUPPLIER_EMAIL + " TEXT NOT NULL);";

    private static final String SQL_DELETE_TABLE = "DELETE FROM " + ProductEntry.TABLE_NAME + ";";


    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
        Log.v(LOG_TAG, "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.v(LOG_TAG, "onUpgrade fired");
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);

    }
}
