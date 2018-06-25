package com.example.ro221d.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ro221d.inventoryapp.data.BookContract.BookEntry;
import com.example.ro221d.inventoryapp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {
    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new BookDbHelper(this);
        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER};

        Cursor cursor = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        TextView displayTextView = (TextView) findViewById(R.id.display_table_text_view);

        try {
            displayTextView.setText("Book table contents " + cursor.getCount() + " books.\n\n");
            displayTextView.append(BookEntry._ID + " -" +
                    BookEntry.COLUMN_BOOK_NAME + " - " +
                    BookEntry.COLUMN_BOOK_PRICE + " - " +
                    BookEntry.COLUMN_BOOK_QUANTITY + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " - " +
                    BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER + "\n");

            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierNumberColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                int currentSupplierNumber = cursor.getInt(supplierNumberColumnIndex);
                displayTextView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierNumber
                ));
            }
        } finally {
            cursor.close();
        }
    }

    private void insertBook() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, "Witcher");
        values.put(BookEntry.COLUMN_BOOK_PRICE, 5);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, 2);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, "John");
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, 555333111);
        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_sample_data:
                insertBook();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_sample_book:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


