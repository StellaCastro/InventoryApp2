package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.android.inventoryapp.data.ItemContract.ItemEntry;
import com.example.android.inventoryapp.data.ItemContract;

public class Item_viewer extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_viewer);
      // getActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();


        productName = (TextView)findViewById(R.id.product_name);
        productPrice = (TextView)findViewById(R.id.price);
        productSeller = (TextView)findViewById(R.id.seller);
        productContact = (TextView)findViewById(R.id.phone);
        productQuantity = (TextView)findViewById(R.id.quantity);
        addMore = (TextView)findViewById(R.id.add);
        remove = (TextView)findViewById(R.id.remove);
        orderMore = (TextView)findViewById(R.id.order_more);
        editItem = (TextView)findViewById(R.id.edit_item);
        myCurrentItemUri = intent.getData();



        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(Item_viewer.this, ItemEditorActivity.class);
                editIntent.setData(myCurrentItemUri);
                startActivity(editIntent);
                finish();
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
        if (data.moveToFirst()) {
            int nameColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int priceColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
            int quantityColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
            int sellerColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_SELLER);
            int contactColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_ITEM_CONTACT);

            productName.setText(getString(nameColumnIndex));
            productPrice.setText(getString(priceColumnIndex));
            productSeller.setText(getString(sellerColumnIndex));
            productContact.setText(getString(contactColumnIndex));
            productQuantity.setText(getString(quantityColumnIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {

    }
}
