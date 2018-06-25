package com.example.ro221d.inventoryapp.data;

import android.provider.BaseColumns;

public class BookContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    private BookContract() {
    }

    /**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single book.
     */
    public static final class BookEntry implements BaseColumns {

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
    }
}
