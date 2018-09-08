package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public class ItemContract {
    private ItemContract (){}

    public static final class ItemEntry implements BaseColumns {
        public final static String TABLE_NAME = "items";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ITEM_NAME = "name";
        public final static String COLUMN_ITEM_PRICE = "price";
        public final static String COLUMN_ITEM_QUANTITY = "quantity";

        public final static String COLUMN_ITEM_SELLER = "seller";
        public final static String COLUMN_ITEM_CONTACT = "love";

    }
}
