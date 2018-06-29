package com.example.ro221d.inventoryapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ro221d.inventoryapp.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the product data loader
     */
    private static final int EXISTING_BOOK_LOADER = 0;
    boolean hasRequiredValues = false;
    private Uri mImageUri;
    private int mQuantity;
    private Uri mCurrentBookUri;
    private ImageView mPhoto;
    private EditText mBookNameEditText;
    private EditText mBookDescriptionEditText;
    private EditText mBookPriceEditText;
    private EditText mBookQuantityEditText;
    private EditText mBookSupplierNameEditText;
    private EditText mBookSupplierNumberEditText;
    private TextView mTapPhotoHintText;
    private Button mAddBookButton;
    private Button mRejectBookButton;
    private boolean mBookHasChanged = false;
    private int mCurrentQuantity = 0;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        mBookNameEditText = findViewById(R.id.book_name_edit_text);
        mBookDescriptionEditText = findViewById(R.id.book_description_edit_text);
        mBookPriceEditText = findViewById(R.id.book_price_edit_text);
        mBookQuantityEditText = findViewById(R.id.book_quantity_edit_text);
        mBookSupplierNameEditText = findViewById(R.id.book_supplier_name_edit_text);
        mBookSupplierNumberEditText = findViewById(R.id.book_supplier_number_edit_text);
        mPhoto = (ImageView) findViewById(R.id.book_photo);
        mAddBookButton = (Button) findViewById(R.id.add_book_button);
        mRejectBookButton = (Button) findViewById(R.id.reject_book_button);
        mTapPhotoHintText = (TextView) findViewById(R.id.tap_photo_text_view);


        // If the intent DOES NOT contain a product content URI, then we know that we are
        // creating a new product.
        if (mCurrentBookUri == null) {
            setTitle(getString(R.string.add_new_book_title));
            mTapPhotoHintText.setText(getText(R.string.tap_photo));
            mBookDescriptionEditText.setEnabled(true);
            mBookQuantityEditText.setEnabled(true);
            mBookSupplierNameEditText.setEnabled(true);
            mBookSupplierNumberEditText.setEnabled(true);
            mPhoto.setImageResource(R.drawable.icons8_empty_box_48);
            mAddBookButton.setVisibility(View.GONE);
            mRejectBookButton.setVisibility(View.GONE);
            invalidateOptionsMenu();

        } else {
            setTitle(getString(R.string.edit_book_title));
            mTapPhotoHintText.setText(R.string.tap_photo);
            mBookDescriptionEditText.setEnabled(true);
            mBookSupplierNameEditText.setEnabled(true);
            mBookSupplierNumberEditText.setEnabled(true);
            mBookQuantityEditText.setEnabled(true);
            mAddBookButton.setVisibility(View.VISIBLE);
            mRejectBookButton.setVisibility(View.VISIBLE);

            getSupportLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }
        mBookNameEditText.setOnTouchListener(mTouchListener);
        mBookDescriptionEditText.setOnTouchListener(mTouchListener);
        mBookPriceEditText.setOnTouchListener(mTouchListener);
        mBookQuantityEditText.setOnTouchListener(mTouchListener);
        mBookSupplierNameEditText.setOnTouchListener(mTouchListener);
        mBookSupplierNumberEditText.setOnTouchListener(mTouchListener);
        mAddBookButton.setOnTouchListener(mTouchListener);
        mRejectBookButton.setOnTouchListener(mTouchListener);
        mAddBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBookButton(view);
            }
        });

        mRejectBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectBookButton(view);
            }
        });

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySelector();
                mBookHasChanged = true;
            }
        });
    }

    public void trySelector() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        openSelector();
    }

    private void openSelector() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType(getString(R.string.intent_type));
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSelector();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                mImageUri = data.getData();
                mPhoto.setImageURI(mImageUri);
                mPhoto.invalidate();
            }
        }
    }

    private boolean saveBook() {
        int quantity;
        String nameString = mBookNameEditText.getText().toString().trim();
        String descriptionString = mBookDescriptionEditText.getText().toString().trim();
        String quantityString = mBookQuantityEditText.getText().toString().trim();
        String priceString = mBookPriceEditText.getText().toString().trim();
        String supplierNameString = mBookSupplierNameEditText.getText().toString().trim();
        String supplierNumberString = mBookSupplierNumberEditText.getText().toString().trim();
        if (mCurrentBookUri == null &&
                TextUtils.isEmpty(nameString) &&
                TextUtils.isEmpty(descriptionString) &&
                TextUtils.isEmpty(quantityString) &&
                TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(supplierNameString) &&
                TextUtils.isEmpty(supplierNumberString) &&
                mImageUri == null) {
            hasRequiredValues = true;
            return hasRequiredValues;
        }
        ContentValues values = new ContentValues();
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, getString(R.string.editor_title_error), Toast.LENGTH_SHORT).show();

            return hasRequiredValues;
        } else {
            values.put(BookEntry.COLUMN_BOOK_NAME, nameString);
        }

        if (TextUtils.isEmpty(descriptionString)) {
            Toast.makeText(this, getString(R.string.editor_description_error), Toast.LENGTH_SHORT).show();

            return hasRequiredValues;
        } else {
            values.put(BookEntry.COLUMN_BOOK_DESCRIPTION, descriptionString);
        }


        if (TextUtils.isEmpty(quantityString)) {
            Toast.makeText(this, getString(R.string.editor_quantity_error), Toast.LENGTH_SHORT).show();
            return hasRequiredValues;
        } else {
            // If the quantity is not provided by the user, don't try to parse the string into an
            // integer value. Use 0 by default.
            quantity = Integer.parseInt(quantityString);
            values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        }

        if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, getString(R.string.editor_price_error), Toast.LENGTH_SHORT).show();
            return hasRequiredValues;
        } else {
            values.put(BookEntry.COLUMN_BOOK_PRICE, priceString);
        }

        if (mImageUri == null) {
            Toast.makeText(this, getString(R.string.editor_image_error), Toast.LENGTH_SHORT).show();
            return hasRequiredValues;
        } else {
            values.put(BookEntry.COLUMN_BOOK_PICTURE, mImageUri.toString());
        }

        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);      // optional, nullable
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, supplierNumberString);

        if (mCurrentBookUri == null) {
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING product, so update the product with content URI: mCurrentProductUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentProductUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_book_successful),
                        Toast.LENGTH_SHORT).show();
            }

        }

        hasRequiredValues = true;
        return hasRequiredValues;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new product, hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save book to database
                saveBook();
                if (hasRequiredValues) {
                    // Exit activity
                    finish();
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:

                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the product hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all product attributes, define a projection that contains
        // all columns from the product table
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_DESCRIPTION,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_PICTURE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER,

        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new android.support.v4.content.CursorLoader(this,       // Parent activity context
                mCurrentBookUri,         // Query the content URI for the current product
                projection,                 // Columns to include in the resulting Cursor
                null,                       // No selection clause
                null,                       // No selection arguments
                null);                      // Default sort order
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_DESCRIPTION);
            int pictureColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PICTURE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierNumberColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER);


            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String description = cursor.getString(descriptionColumnIndex);

            String imageUriString = cursor.getString(pictureColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierNumber = cursor.getString(supplierNumberColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            mQuantity = quantity;
            mImageUri = Uri.parse(imageUriString);

            // Update the views on the screen with the values from the database
            mBookNameEditText.setText(name);
            mBookDescriptionEditText.setText(description);
            mPhoto.setImageURI(mImageUri);
            mBookPriceEditText.setText(price);
            mBookSupplierNameEditText.setText(supplierName);
            mBookSupplierNumberEditText.setText(supplierNumber);
            mBookQuantityEditText.setText(Integer.toString(quantity));


        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
// If the loader is invalidated, clear out all the data from the input fields.
        mBookNameEditText.setText("");
        mBookDescriptionEditText.setText("");
        mPhoto.setImageResource(R.drawable.icons8_empty_box_48);
        mBookPriceEditText.setText("");
        mBookSupplierNameEditText.setText("");
        mBookSupplierNumberEditText.setText("");
        mBookQuantityEditText.setText("");

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

    /**
     * Prompt the user to confirm that they want to delete this product.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteProduct();
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

    private void deleteProduct() {
        // Only perform the delete if this is an existing product.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the product at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentProductUri
            // content URI already identifies the product that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    public void addBookButton(View view) {
        mQuantity++;
        displayQuantity();
    }


    public void rejectBookButton(View view) {
        if (mQuantity == 0) {
            Toast.makeText(this, "Can't decrease quantity", Toast.LENGTH_SHORT).show();
        } else {
            mQuantity--;
            displayQuantity();
        }
    }

    public void displayQuantity() {
        mBookQuantityEditText.setText(String.valueOf(mQuantity));
    }
}
