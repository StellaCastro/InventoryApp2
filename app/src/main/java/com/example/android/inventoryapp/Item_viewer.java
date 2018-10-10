package com.example.android.inventoryapp;


import android.content.ContentProvider;
import android.content.ContentValues;

import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ItemContract.ItemEntry;
import com.example.android.inventoryapp.data.ItemContract;

public class Item_viewer extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    //loader and uri variables
    private static final int ITEM_LOADER =1;
    private Uri myCurrentItemUri;
    private String uri_item_key = "uri";

    //text vies on the screen
    private TextView productName;
    private TextView productPrice;
    private TextView productSeller;
    private TextView productContact;
    private TextView productQuantity;

    //clickables on the screen
    private TextView addMore;
    private TextView remove;
    private TextView orderMore;
    private TextView editItem;
    private TextView deleteItem;

    //variables
    private String quantityVariable;
    private String nameVariable;
    private String priceVariable;
    private String supplierVariable;
    private String contactVariable;
    private long idVariable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewer);


        //finding the view on the screen
        productName = (TextView)findViewById(R.id.product_name);
        productPrice = (TextView)findViewById(R.id.price);
        productSeller = (TextView)findViewById(R.id.seller);
        productContact = (TextView)findViewById(R.id.phone);
        productQuantity = (TextView)findViewById(R.id.quantity);
        editItem = (TextView)findViewById(R.id.edit_item);
        remove = (TextView)findViewById(R.id.remove);
        addMore = (TextView)findViewById(R.id.add_more);
        orderMore = (TextView)findViewById(R.id.order_more);
        deleteItem = (TextView)findViewById(R.id.delete_item);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            myCurrentItemUri = intent.getData();
            getSupportLoaderManager().initLoader(ITEM_LOADER, null, this);
        } else {
            myCurrentItemUri = Uri.parse(savedInstanceState.getString(uri_item_key));
            getSupportLoaderManager().initLoader(ITEM_LOADER, null, this);
        }

       editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(Item_viewer.this, ItemEditorActivity.class);
                editIntent.putExtra(ItemEntry._ID, idVariable);
                editIntent.putExtra(ItemEntry.COLUMN_ITEM_NAME, nameVariable);
                editIntent.putExtra(ItemEntry.COLUMN_ITEM_PRICE, priceVariable);
                editIntent.putExtra(ItemEntry.COLUMN_ITEM_QUANTITY, quantityVariable);
                editIntent.putExtra(ItemEntry.COLUMN_ITEM_SELLER, supplierVariable);
                editIntent.putExtra(ItemEntry.COLUMN_ITEM_CONTACT, contactVariable);
                startActivity(editIntent);
                finish();
                }
        });

       addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityVariable = String.valueOf(Integer.parseInt(quantityVariable)+1);
                ContentValues values = new ContentValues();
                values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantityVariable);
                int rowsChanged = getContentResolver().update(
                        myCurrentItemUri,
                        values,
                        null,
                        null);
                if (rowsChanged == 1){
                    productQuantity.setText(quantityVariable);
                }else{
                    Toast.makeText(Item_viewer.this, "ups cant change quantity", Toast.LENGTH_SHORT).show();
                }

            }
       });

       remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(quantityVariable)>=1){
                    quantityVariable = String.valueOf(Integer.parseInt(quantityVariable) - 1);
                    ContentValues values = new ContentValues();
                    values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantityVariable);
                    int rowsChanged = getContentResolver().update(
                            myCurrentItemUri,
                            values,
                            null,
                            null);
                    if(rowsChanged == 1){
                        productQuantity.setText(quantityVariable);
                    }else{
                        Toast.makeText(Item_viewer.this, "ups... cant change quantity", Toast.LENGTH_SHORT).show();
                    }
                }
                if (Integer.parseInt(quantityVariable)==0){
                    Toast.makeText(Item_viewer.this, "Item out of stock", Toast.LENGTH_SHORT).show();
                }
            }
        });
      orderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contactVariable));
                startActivity(intent);
            }
        });
      deleteItem.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              deleteThisItem();
          }
      });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, myCurrentItemUri, null, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        data.moveToFirst();

        idVariable = data.getLong(data.getColumnIndex(ItemEntry._ID));
        nameVariable = data.getString(data.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME));
        priceVariable = data.getString(data.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE));
        quantityVariable = data.getString(data.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY));
        supplierVariable = data.getString(data.getColumnIndex(ItemEntry.COLUMN_ITEM_SELLER));
        contactVariable = data.getString(data.getColumnIndex(ItemEntry.COLUMN_ITEM_CONTACT));

        placeData();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productName.setText("");
        productPrice.setText("");
        productQuantity.setText("");
        productSeller.setText("");
        productContact.setText("");

    }
//populating the views on the screen
    private void placeData(){
        productName.setText(nameVariable);
        productPrice.setText(priceVariable);
        productQuantity.setText(quantityVariable);
        productSeller.setText(supplierVariable);
        productContact.setText(contactVariable);
    }
//delete method
    private void deleteThisItem (){
        int itemDeleteRow = getContentResolver().delete(myCurrentItemUri, null, null);

        if (itemDeleteRow == 0){
            Toast.makeText(this, "Error deleting Item", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
        }
        finish();
    }



}
