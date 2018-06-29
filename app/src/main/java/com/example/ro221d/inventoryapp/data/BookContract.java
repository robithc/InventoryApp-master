package com.example.ro221d.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {


    public static final String LOG_TAG = BookProvider.class.getSimpleName();


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    private BookContract() {
    }

    /**
     * Building URI
     */
    // authority
    public static final String CONTENT_AUTHORITY = "com.example.android.book";
    // base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    // path to table name
    public static final String PATH_BOOKS = "books";





    /**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single book.
     */
    public static final class BookEntry implements BaseColumns {


        /**
         * The content URI to access the book data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;





        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "Books";

        /**
         * Unique ID number for the book (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the book.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_NAME = "name";

        /**
         * Price of the book.
         * <p>
         * Type: TEXT
         */

        public final static String COLUMN_BOOK_DESCRIPTION = "description";

        /**
         * Price of the book.
         * <p>
         * Type: TEXT
         */


        public final static String COLUMN_BOOK_PRICE = "price";

        /**
         * Price of the book.
         * <p>
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_QUANTITY = "quantity";

        /**
         * Quantity of the book.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";

        /**
         * Name of the supplier.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        /**
         * Number of the supplier.
         *
         * Type: INTEGER
         */

        public static final String COLUMN_BOOK_PICTURE = "picture";

        /**
         * Price of the product.
         * <p>
         * Type: INTEGER
         */
    }
}
