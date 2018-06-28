package com.example.ro221d.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ro221d.inventoryapp.data.BookContract;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleTextView = (TextView) view.findViewById(R.id.book_title);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.book_description);
        TextView priceTextView = (TextView) view.findViewById(R.id.book_price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.book_quantity);
        ImageView pictureImageView = (ImageView) view.findViewById(R.id.book_image);

        final int productIdColumnIndex = cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry._ID));
        int titleColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_NAME);
        int descriptionColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_DESCRIPTION);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
        int pictureColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PICTURE);

        String bookName = cursor.getString(titleColumnIndex);
        String bookDescription = cursor.getString(descriptionColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        String bookQuantity = cursor.getString(quantityColumnIndex);
        String pictureUriString = cursor.getString(pictureColumnIndex);
        Uri bookImage = Uri.parse(pictureUriString);
        // If the product model is empty string or null, then hide TextView
        if (TextUtils.isEmpty(bookDescription)) {
            descriptionTextView.setVisibility(View.GONE);


        }
        titleTextView.setText(bookName);
        descriptionTextView.setText(bookDescription);
        priceTextView.setText(bookPrice);
        quantityTextView.setText(bookQuantity);
        pictureImageView.setImageURI(bookImage);
    }
}

