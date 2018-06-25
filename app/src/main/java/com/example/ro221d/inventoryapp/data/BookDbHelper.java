package com.example.ro221d.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ro221d.inventoryapp.data.BookContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = BookDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "library.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BookDbHelper}.
     *
     * @param context of the app
     */
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_PRICE + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER + " INTEGER );";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);

        Log.d("successfully message", "created table of db");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
