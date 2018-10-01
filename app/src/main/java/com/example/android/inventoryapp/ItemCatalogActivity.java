package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp.data.ItemContract.ItemEntry;
import com.example.android.inventoryapp.data.ItemDbHelper;

public class ItemCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER = 0;
    ItemCursorAdapter mItemCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_catalog);

        // here we are looking for the fab button and setting the intent to take to the editor activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemCatalogActivity.this, ItemEditorActivity.class);
                startActivity(intent);
            }
        });

        ListView itemListView = (ListView)findViewById(R.id.list);

        View view = findViewById(R.id.warehouse);
        itemListView.setEmptyView(view);

        mItemCursorAdapter = new ItemCursorAdapter(this, null);
        itemListView.setAdapter(mItemCursorAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent (ItemCatalogActivity.this, Item_viewer.class);
                Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
                myIntent.setData(currentItemUri);
                startActivity(myIntent);
            }
        });

        getLoaderManager().initLoader(ITEM_LOADER, null, this);

        //mDbHelper = new ItemDbHelper(this);
    }
   /* @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
    //the method helpling to display the info on the screen
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //this is calling the table
        String [] inventoryProjection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_CONTACT,
                ItemEntry.COLUMN_ITEM_SELLER
        };
        Cursor cursor = db.query(
                ItemEntry.TABLE_NAME,
                inventoryProjection,
                null,
                null,
                null,
                null,
                null
        );
        //finding the textview to place that input of the data
        TextView showView = (TextView)findViewById(R.id.display_view);
        try{
            showView.setText("The items table contains " + cursor.getCount() + " items.\n\n");
            showView.append(ItemEntry._ID + " - " +
                    ItemEntry.COLUMN_ITEM_NAME + " - " +
                    ItemEntry.COLUMN_ITEM_PRICE+ " - " +
                    ItemEntry.COLUMN_ITEM_QUANTITY + " - " +
                    ItemEntry.COLUMN_ITEM_CONTACT + " - " +
                    ItemEntry.COLUMN_ITEM_SELLER  + "\n");


            int idColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_SELLER);
            int supplierContactColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_CONTACT);

            while (cursor.moveToNext()){
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                double currentPrice = cursor.getDouble(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierContact = cursor.getString(supplierContactColumnIndex);
                showView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierContact + " - " +
                        currentSupplierName ));

            }
        }finally {
            cursor.close();
        }
    }*/
    //inserting dummy data
    private void insertPet (){

        ContentValues values = new ContentValues ();

        values.put(ItemEntry.COLUMN_ITEM_NAME, "Toto");
        values.put(ItemEntry.COLUMN_ITEM_PRICE, 3.99);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, 2);
        values.put(ItemEntry.COLUMN_ITEM_SELLER, "STELLA");
        values.put(ItemEntry.COLUMN_ITEM_CONTACT, "phone number");

      Uri myNewUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);

    }

    private void deleteAllItems() {
        int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }
    //displaying the upbar menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.item_menu_catalog, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_item:
                insertPet();
                return true;
            // Respond to a click on the "Delete all" menu option
            case R.id.action_delete_all:
                deleteAllItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String [] myProjection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_QUANTITY };

        return new android.content.CursorLoader(ItemCatalogActivity.this,
                ItemEntry.CONTENT_URI,
                myProjection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mItemCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mItemCursorAdapter.swapCursor(null);
    }
}
