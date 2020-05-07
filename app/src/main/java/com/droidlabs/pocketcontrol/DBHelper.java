package com.droidlabs.pocketcontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Helper class for DB management
 */
public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    private static final String DB_NAME = "PocketControl.db";
    private static final int DB_VERSION = 1;

    private static final String CATEGORY_TABLE_NAME = "category_table";
    private static final String CATEGORY_ID_COL = "id";
    private static final String CATEGORY_NAME_COL = "name";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table for categories
        Log.v("CREATE DB", "onCreate called");
        db.execSQL(
            "create table " + CATEGORY_TABLE_NAME + "(" +
                CATEGORY_ID_COL + " INTEGER primary key autoincrement," +
                CATEGORY_NAME_COL + " TEXT not null);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
