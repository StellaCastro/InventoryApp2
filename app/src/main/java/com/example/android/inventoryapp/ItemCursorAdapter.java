package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ItemContract.ItemEntry;
public class ItemCursorAdapter extends CursorAdapter {

    private Context myContext;
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        myContext = context;
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameViewItem = (TextView) view.findViewById(R.id.object_name);
        TextView priceViewItem = (TextView) view.findViewById(R.id.object_price);
        TextView quantityViewItem = (TextView) view.findViewById(R.id.object_number);
        final TextView sellProduct = (TextView) view.findViewById(R.id.object_sale);

        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);

        String objectName = cursor.getString(nameColumnIndex);
        String objectPrice = cursor.getString(priceColumnIndex);
        final String objectQuantity = cursor.getString(quantityColumnIndex);
        final int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(ItemEntry._ID));

        nameViewItem.setText(objectName);
        priceViewItem.setText(objectPrice);
        quantityViewItem.setText(objectQuantity);

        sellProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellItem(itemId, Integer.parseInt(objectQuantity));
            }
        });
    }

    private void sellItem(int itemId, int objectQuantity) {
        if (objectQuantity >= 1) {
            objectQuantity = objectQuantity - 1;
            Uri productUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, itemId);
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLUMN_ITEM_QUANTITY, objectQuantity);
            int rowsUpdated = myContext.getContentResolver().update(
                    productUri,
                    values,
                    null,
                    null);
            if (rowsUpdated == 1) {
                Toast.makeText(myContext, "Sold!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(myContext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(myContext, "Cant sell Item out of stock", Toast.LENGTH_SHORT).show();
        }
    }
}
