package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.android.inventoryapp.data.ItemContract.ItemEntry;
import com.example.android.inventoryapp.data.ItemDbHelper;

public class ItemCatalogActivity extends AppCompatActivity {
    private ItemDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemCatalogActivity.this, ItemEditorActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new ItemDbHelper(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String [] inventoryProjection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_CONTACT, //if we comment this line, the code will work but will not be able to save data
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
        TextView showView = (TextView)findViewById(R.id.stella);
        try{
            showView.setText("The items table contains " + cursor.getCount() + " items.\n\n");
            showView.append(ItemEntry._ID + " - " +
                    ItemEntry.COLUMN_ITEM_NAME + " - " +
                    ItemEntry.COLUMN_ITEM_PRICE+ " - " +
                    ItemEntry.COLUMN_ITEM_QUANTITY + " - " +
                    //ItemEntry.COLUMN_ITEM_CONTACT + " - " +
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
    }
    private void insertPet (){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues ();

        values.put(ItemEntry.COLUMN_ITEM_NAME, "Toto");
        values.put(ItemEntry.COLUMN_ITEM_PRICE, 3.99);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, 2);
        values.put(ItemEntry.COLUMN_ITEM_SELLER, "STELLA");
        values.put(ItemEntry.COLUMN_ITEM_CONTACT, "phone number");

        long newRowId = db.insert(ItemEntry.TABLE_NAME, null, values);

        Log.v("InvCatalogActivity", "New roe ID "+ newRowId);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
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
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all:
                // Do nothing
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
