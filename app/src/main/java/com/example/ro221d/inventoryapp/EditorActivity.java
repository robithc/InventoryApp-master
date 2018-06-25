package com.example.ro221d.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ro221d.inventoryapp.data.BookContract;
import com.example.ro221d.inventoryapp.data.BookDbHelper;
import com.example.ro221d.inventoryapp.data.BookContract.BookEntry;


import com.example.ro221d.inventoryapp.data.BookContract;
import com.example.ro221d.inventoryapp.data.BookDbHelper;

public class EditorActivity extends AppCompatActivity {
    private EditText mBookNameEditText;
    private EditText mBookPriceEditText;
    private EditText mBookQuantityEditText;
    private EditText mBookSupplierNameEditText;
    private EditText mBookSupplierNumberEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
//        TextView bookTitle = findViewById(R.id.book_name_text_view);
//        TextView bookPrice = findViewById(R.id.book_price_text_view);
//        TextView bookQuantity = findViewById(R.id.book_quantity_text_view);
//        TextView supplierName = findViewById(R.id.book_supplier_name_text_view);
//        TextView supplierNumber = findViewById(R.id.book_supplier_number_text_view);
        mBookNameEditText = findViewById(R.id.book_name_edit_text);
        mBookPriceEditText = findViewById(R.id.book_price_edit_text);
        mBookQuantityEditText = findViewById(R.id.book_quantity_edit_text);
        mBookSupplierNameEditText = findViewById(R.id.book_supplier_name_edit_text);
        mBookSupplierNumberEditText = findViewById(R.id.book_supplier_number_edit_text);

    }

    private void insertBook() {
        String titleString = mBookNameEditText.getText().toString().trim();
        String priceString = mBookPriceEditText.getText().toString().trim();
        int price = Integer.parseInt(priceString);
        String quantityString = mBookQuantityEditText.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        String supplierName = mBookSupplierNameEditText.getText().toString().trim();
        String supplierNumber = mBookSupplierNumberEditText.getText().toString().trim();
        int supplierPhoneNumber = Integer.parseInt(supplierNumber);
        BookDbHelper mDbHelper = new BookDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, titleString);
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierName);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);
        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);
        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save book to database

                insertBook();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:

                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
}
