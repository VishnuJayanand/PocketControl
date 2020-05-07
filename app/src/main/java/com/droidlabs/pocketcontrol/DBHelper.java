package com.droidlabs.pocketcontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Helper class for DB management
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "PocketControl.db";
    private static final int DB_VERSION = 1;

    private final class TransactionTableContract {

        private static final String TABLE_NAME = "transactions_table";
        private static final String ID_COL = "id";
        private static final String VALUE_COL = "value";
        private static final String TEXT_NOTE_COL = "text_note";
        // private static final String CATEGORY_COL = "category";

    }

    private final class CategoryTableContract {
        private static final String TABLE_NAME = "transactions_table";
        private static final String ID_COL = "id";
        private static final String NAME_COL = "name";
    }

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table for categories
        db.execSQL(
            "create table " + CategoryTableContract.TABLE_NAME + "(" +
                CategoryTableContract.ID_COL + " INTEGER primary key autoincrement," +
                CategoryTableContract.NAME_COL + " TEXT not null);"
        );

        // create table for transactions
        db.execSQL(
            "create table " + TransactionTableContract.TABLE_NAME + "(" +
                TransactionTableContract.ID_COL + " INTEGER primary key autoincrement," +
                TransactionTableContract.VALUE_COL + " REAL not null," +
                TransactionTableContract.TEXT_NOTE_COL + " TEXT);"
                    /*
                TransactionTableContract.CATEGORY_COL + " INTEGER," +
                " foreign key (" + TransactionTableContract.CATEGORY_COL + ") references " +
                CategoryTableContract.TABLE_NAME + "(" + CategoryTableContract.ID_COL + "));"
                     */

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + CategoryTableContract.TABLE_NAME);
        db.execSQL("drop table if exists " + TransactionTableContract.TABLE_NAME);

        onCreate(db);
    }
}
