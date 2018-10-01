package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ItemDbHelper;
import com.example.android.inventoryapp.data.ItemContract.ItemEntry;

public class ItemEditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
   //identifying the loader
   private static final int EXISTING_ITEM_LOADER = 0;

   private Uri mCurrentItemUri;

    //Initializing the different editText
    private EditText itemNameEditText;
    private EditText itemPriceEditText;
    private EditText itemQuantityEditText;
    private EditText itemSupplierNameEditText;
    private EditText itemSupplierContactEditText;

    private boolean mItemHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editor);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if(mCurrentItemUri==null){
            setTitle(getString(R.string.add_item_title));
            invalidateOptionsMenu();
        }else{
            setTitle(getString(R.string.edit_item_title));
           getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }



        //finding the views on the xml
        itemNameEditText = (EditText)findViewById(R.id.item_name);
        itemPriceEditText = (EditText)findViewById(R.id.item_price);
        itemQuantityEditText = (EditText)findViewById(R.id.item_quantity);
        itemSupplierNameEditText = (EditText)findViewById(R.id.item_supplier_name);
        itemSupplierContactEditText = (EditText)findViewById(R.id.item_supplier_contact);

        itemNameEditText.setOnTouchListener(mTouchListener);
        itemPriceEditText.setOnTouchListener(mTouchListener);
        itemQuantityEditText.setOnTouchListener(mTouchListener);
        itemSupplierNameEditText.setOnTouchListener(mTouchListener);
        itemSupplierContactEditText.setOnTouchListener(mTouchListener);
    }
    //inserting the data inserted by the user into the database
    private void saveItem () {

        String itemName = itemNameEditText.getText().toString().trim();
        String itemPrice = itemPriceEditText.getText().toString().trim();
        String itemQuantity = itemQuantityEditText.getText().toString().trim();
        String itemSupplierName = itemSupplierNameEditText.getText().toString().trim();
        String itemSupplierContact = itemSupplierContactEditText.getText().toString().trim();
        double price = Double.parseDouble(itemPrice);
        int quantity = Integer.parseInt(itemQuantity);

        if(mCurrentItemUri==null && TextUtils.isEmpty(itemName) && TextUtils.isEmpty(itemPrice)
        && TextUtils.isEmpty(itemQuantity) && TextUtils.isEmpty(itemSupplierName)&& TextUtils.isEmpty(itemSupplierContact)){
                return;
        }
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, itemName);
        values.put(ItemEntry.COLUMN_ITEM_PRICE, price);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(ItemEntry.COLUMN_ITEM_SELLER, itemSupplierName);
        values.put(ItemEntry.COLUMN_ITEM_CONTACT, itemSupplierContact);

        if(mCurrentItemUri == null){
            Uri myNewUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);
            if(myNewUri == null){
                Toast.makeText(this, R.string.cant_save, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, getString(R.string.save_sucessfull), Toast.LENGTH_SHORT).show();
            }
        }else{
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
            if(rowsAffected==0){
                Toast.makeText(this, "Ups... we couldn't save changes", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, "Item Sucessfully updated", Toast.LENGTH_SHORT).show();
            }
        }

    }
    //setting up the menu in the upbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu_editor, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveItem();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                if(!mItemHasChanged){
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(ItemEditorActivity.this);
                    }
                };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mItemHasChanged) {
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String [] myItemProjection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_SELLER,
                ItemEntry.COLUMN_ITEM_CONTACT
        };
        return new CursorLoader(this,
                mCurrentItemUri,
                myItemProjection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (data.moveToFirst()) {
            int nameColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int priceColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
            int quantityColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
            int sellerColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_SELLER);
            int contactColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_CONTACT);


            String name = data.getString(nameColumnIndex);
            double price = data.getDouble(priceColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            String seller = data.getString(sellerColumnIndex);
            float contact = data.getFloat(contactColumnIndex);

            itemNameEditText.setText(name);
            itemPriceEditText.setText(Double.toString(price));
            itemQuantityEditText.setText(Integer.toString(quantity));
            itemSupplierNameEditText.setText(seller);
            itemSupplierContactEditText.setText(Float.toString(contact));


        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        itemNameEditText.setText("");
        itemPriceEditText.setText("");
        itemQuantityEditText.setText("");
        itemSupplierNameEditText.setText("");
        itemSupplierContactEditText.setText("");
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard changes and quit editing");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Item");
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the item.
                deleteItem();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteItem() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentItemUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Error Occurred... deleting unsuccessful",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "Item Deleted",
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}
