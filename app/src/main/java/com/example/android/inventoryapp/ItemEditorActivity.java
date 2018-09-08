package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ItemDbHelper;
import com.example.android.inventoryapp.data.ItemContract.ItemEntry;

public class ItemEditorActivity extends AppCompatActivity {

    private EditText itemNameEditText;
    private EditText itemPriceEditText;
    private EditText itemQuantityEditText;
    private EditText itemSupplierNameEditText;
    private EditText itemSupplierContactEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editor);
        itemNameEditText = (EditText)findViewById(R.id.item_name);
        itemPriceEditText = (EditText)findViewById(R.id.item_price);
        itemQuantityEditText = (EditText)findViewById(R.id.item_quantity);
        itemSupplierNameEditText = (EditText)findViewById(R.id.item_supplier_name);
        itemSupplierContactEditText = (EditText)findViewById(R.id.item_supplier_contact);
    }
    private void insertItem () {
        String itemName = itemNameEditText.getText().toString().trim();
        String itemPrice = itemPriceEditText.getText().toString().trim();
        String itemQuantity = itemQuantityEditText.getText().toString().trim();
        String itemSupplierName = itemSupplierNameEditText.getText().toString().trim();
        String itemSupplierContact = itemSupplierContactEditText.getText().toString().trim();
        double price = Double.parseDouble(itemPrice);
        int quantity = Integer.parseInt(itemQuantity);

        ItemDbHelper mDbHelper = new ItemDbHelper(this);
        SQLiteDatabase myDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

           values.put(ItemEntry.COLUMN_ITEM_NAME, itemName);
        values.put(ItemEntry.COLUMN_ITEM_PRICE, price);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(ItemEntry.COLUMN_ITEM_SELLER, itemSupplierName);
        values.put(ItemEntry.COLUMN_ITEM_CONTACT, itemSupplierContact);

        long newRowId = myDb.insert(ItemEntry.TABLE_NAME, null, values);

        if(newRowId == -1){
            Toast.makeText(this, "Ups... we couldn't save data", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Item successfully save with id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertItem();
                finish();
                return true;
            case R.id.action_delete:
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
