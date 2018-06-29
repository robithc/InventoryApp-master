package com.example.ro221d.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ro221d.inventoryapp.data.BookContract;

import static android.content.ContentValues.TAG;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView titleTextView = (TextView) view.findViewById(R.id.book_title);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.book_description);
        TextView priceTextView = (TextView) view.findViewById(R.id.book_price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.book_quantity);
        ImageView pictureImageView = (ImageView) view.findViewById(R.id.book_image);
        ImageButton saleImageButton = (ImageButton) view.findViewById(R.id.sale_button);

        final int productIdColumnIndex = cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry._ID));
        int titleColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
        int descriptionColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_DESCRIPTION);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
        int pictureColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PICTURE);

        String bookName = cursor.getString(titleColumnIndex);
        String bookDescription = cursor.getString(descriptionColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        final int bookQuantity = cursor.getInt(quantityColumnIndex);
        String pictureUriString = cursor.getString(pictureColumnIndex);
        Uri bookImageUri = Uri.parse(pictureUriString);
        // If the product model is empty string or null, then hide TextView
        if (TextUtils.isEmpty(bookDescription)) {
            descriptionTextView.setVisibility(View.GONE);


        }
        titleTextView.setText(bookName);
        descriptionTextView.setText(bookDescription);
        priceTextView.setText(bookPrice);
        quantityTextView.setText(String.valueOf(bookQuantity));
        pictureImageView.setImageURI(bookImageUri);
        saleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri productUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, productIdColumnIndex);
                adjustProductQuantity(context, productUri, bookQuantity);
            }
        });
    }

    private void adjustProductQuantity(Context context, Uri productUri, int currentQuantityInStock) {

        // Subtract 1 from current value if quantity of product >= 1
        int newQuantityValue = (currentQuantityInStock >= 1) ? currentQuantityInStock - 1 : 0;

        if (currentQuantityInStock == 0) {
            Toast.makeText(context.getApplicationContext(), R.string.toast_out_of_stock_msg, Toast.LENGTH_SHORT).show();
        }

        // Update table by using new value of quantity
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, newQuantityValue);
        int numRowsUpdated = context.getContentResolver().update(productUri, contentValues, null, null);
        if (numRowsUpdated > 0) {
            // Show error message in Logs with info about pass update.
            Log.i(TAG, context.getString(R.string.buy_msg_confirm));
        } else {
            Toast.makeText(context.getApplicationContext(), R.string.no_product_in_stock, Toast.LENGTH_SHORT).show();
            // Show error message in Logs with info about fail update.
            Log.e(TAG, context.getString(R.string.error_msg_stock_update));
        }


    }
}

