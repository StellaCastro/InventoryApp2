package com.example.android.inventoryapp.data;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventoryapp.data.ItemContract.ItemEntry;

public class ItemDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "inventory.db";

    private static final int DATABASE_VERSION = 3;

    public ItemDbHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase itemDb) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + "("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + ItemEntry.COLUMN_ITEM_NAME  + " TEXT NOT NULL , "
                + ItemEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL , "
                + ItemEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL DEFAULT 1 , "
                + ItemEntry.COLUMN_ITEM_CONTACT + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_SELLER + " TEXT NOT NULL " + " )";
        itemDb.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
