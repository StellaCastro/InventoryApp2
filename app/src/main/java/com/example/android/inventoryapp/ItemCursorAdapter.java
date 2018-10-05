package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.android.inventoryapp.data.ItemContract.ItemEntry;
public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor c){
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameViewItem = (TextView)view.findViewById(R.id.object_name);
        TextView priceViewItem = (TextView)view.findViewById(R.id.object_price);
        TextView quantityViewItem = (TextView)view.findViewById(R.id.object_number);

        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);

        String objectName = cursor.getString(nameColumnIndex);
        String objectPrice = cursor.getString(priceColumnIndex);
        String objectQuantity = cursor.getString(quantityColumnIndex);

        nameViewItem.setText(objectName);
        priceViewItem.setText(objectPrice);
        quantityViewItem.setText(objectQuantity);
    }
}
