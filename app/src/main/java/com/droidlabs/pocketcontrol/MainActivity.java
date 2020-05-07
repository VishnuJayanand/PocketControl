package com.droidlabs.pocketcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.sql.SQLData;

/**
 * Main activity of the app.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Method to create an app instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        DBHelper _db = new DBHelper(this);

        SQLiteDatabase db = _db.getWritableDatabase();

        checkForTables(db);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void checkForTables(SQLiteDatabase db){

        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            if (mCursor.moveToFirst()) {
                do {
                    Log.i("DB", mCursor.getString(0));
                } while (mCursor.moveToNext());
            }
        }

    }
}
