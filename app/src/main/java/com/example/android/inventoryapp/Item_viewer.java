package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ItemContract.ItemEntry;
import com.example.android.inventoryapp.data.ItemContract;

public class Item_viewer extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private Uri myCurrentItemUri;
    private TextView productName;
    private TextView productPrice;
    private TextView productSeller;
    private TextView productContact;
    private TextView productQuantity;
    private TextView addMore;
    private TextView remove;
    private TextView orderMore;
    private TextView editItem;
    private int quantityVariable;
    private int updatedQuantity;
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewer);

        Intent intent = getIntent();
        myCurrentItemUri = intent.getData();

        editItem = (TextView)findViewById(R.id.edit_item);
        remove = (TextView)findViewById(R.id.remove);
        addMore = (TextView)findViewById(R.id.add_more);
        orderMore = (TextView)findViewById(R.id.order_more);

       editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(Item_viewer.this, ItemEditorActivity.class);
                editIntent.setData(myCurrentItemUri);
                startActivity(editIntent);
                finish();
                }
        });

       addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMethod();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMethod();
            }
        });
      orderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + productContact));
                startActivity(intent);
            }
        });



    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] myProjection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_SELLER,
                ItemEntry.COLUMN_ITEM_CONTACT,
                ItemEntry.COLUMN_ITEM_QUANTITY};

        return new CursorLoader(this,
                myCurrentItemUri,
                myProjection,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        productName = (TextView)findViewById(R.id.product_name);
        productPrice = (TextView)findViewById(R.id.price);
        productSeller = (TextView)findViewById(R.id.seller);
        productContact = (TextView)findViewById(R.id.phone);
        if (data.moveToFirst()) {
            int nameColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int priceColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
            int sellerColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_SELLER);
            int contactColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_CONTACT);
            quantityVariable = data.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
            phoneNumber = data.getString(contactColumnIndex);
            productName.setText(getString(nameColumnIndex));
            productPrice.setText(getString(priceColumnIndex));
            productSeller.setText(getString(sellerColumnIndex));
            productContact.setText(getString(contactColumnIndex));
            productQuantity.setText(getString(quantityVariable));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



    public int addMethod () {
        if (quantityVariable >= 0) {
            quantityVariable = quantityVariable + 1;
        }else if (quantityVariable==30){
            Toast.makeText(this, "You don't have more space for this item", Toast.LENGTH_SHORT).show();
        }
        return quantityVariable;
    }
    public int removeMethod () {
        if (quantityVariable >= 1) {
            quantityVariable = quantityVariable - 1;
        } else if (quantityVariable == 0) {
            Toast.makeText(this, "Ops... there is no more to sell", Toast.LENGTH_SHORT).show();
        }
        return quantityVariable;
    }
}
