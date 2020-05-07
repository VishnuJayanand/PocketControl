package com.droidlabs.pocketcontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Helper class for DB management.
 */
public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DB_NAME = "PocketControl.db";
    private static final int DB_VERSION = 1;

    private static final String ICON_TABLE_NAME = "icons";
    private static final String ICON_ID_COL = "id";
    private static final String ICON_NAME_COL = "name";
    private static final String ICON_COLOR_COL = "color";

    private static final String BUDGET_TABLE_NAME = "budgets";
    private static final String BUDGET_ID_COL = "id";
    private static final String BUDGET_MAX_AMOUNT_COL = "max_amount";
    private static final String BUDGET_DESCRIPTION_COL = "description";
    private static final String BUDGET_IS_GLOBAL_COL = "is_global";
    private static final String BUDGET_CATEGORY_COL = "category";

    private static final String CATEGORY_TABLE_NAME = "categories";
    private static final String CATEGORY_NAME_COL = "name";
    private static final String CATEGORY_ICON_COL = "icon";

    private static final String CURRENCY_TABLE_NAME = "currencies";
    private static final String CURRENCY_THREE_LETTER_CODE_COL = "three_letter_code";
    private static final String CURRENCY_IS_DEFAULT_COL = "is_default";
    private static final String CURRENCY_TO_DEFAULT_RATE_COL = "to_default_rate";

    private static final String PAYMENT_MODE_TABLE_NAME = "payment_modes";
    private static final String PAYMENT_MODE_NAME_COL = "name";
    private static final String PAYMENT_MODE_ICON_COL = "icon";

    private static final String TRANSACTION_TABLE_NAME = "transactions";
    private static final String TRANSACTION_ID_COL = "id";
    private static final String TRANSACTION_AMOUNT_COL = "amount";
    private static final String TRANSACTION_TEXT_NOTE_COL = "text_note";
    private static final String TRANSACTION_CATEGORY_COL = "category";
    private static final String TRANSACTION_IS_RECURRING_COL = "is_recurring";
    private static final String TRANSACTION_RECURRING_INTERVAL_DAYS_COL = "recurring_interval_days";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + ICON_TABLE_NAME + "("
                        + ICON_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + ICON_NAME_COL+ " TEXT NOT NULL,"
                        + ICON_COLOR_COL + " TEXT)"
        );

        db.execSQL(
                "CREATE TABLE " + CURRENCY_TABLE_NAME + "("
                        + CURRENCY_THREE_LETTER_CODE_COL + " CHAR(3) PRIMARY KEY,"
                        + CURRENCY_IS_DEFAULT_COL + " BOOLEAN,"
                        + CURRENCY_TO_DEFAULT_RATE_COL + " REAL)"
        );

        db.execSQL(
                "CREATE TABLE " + PAYMENT_MODE_TABLE_NAME + "("
                        + PAYMENT_MODE_NAME_COL + " TEXT PRIMARY KEY,"
                        + PAYMENT_MODE_ICON_COL + " INTEGER,"
                        + buildForeignKeyFragment(PAYMENT_MODE_ICON_COL, ICON_TABLE_NAME, ICON_ID_COL) + ")"
        );

        db.execSQL(
                "CREATE TABLE " + CATEGORY_TABLE_NAME + "("
                        + CATEGORY_NAME_COL + " TEXT PRIMARY KEY,"
                        + CATEGORY_ICON_COL + " INTEGER,"
                        + buildForeignKeyFragment(CATEGORY_ICON_COL, ICON_TABLE_NAME, ICON_ID_COL) + ")"
        );

        db.execSQL(
                "CREATE TABLE " + BUDGET_TABLE_NAME + "("
                        + BUDGET_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + BUDGET_MAX_AMOUNT_COL + " REAL NOT NULL,"
                        + BUDGET_DESCRIPTION_COL + " TEXT,"
                        + BUDGET_IS_GLOBAL_COL + " BOOLEAN,"
                        + BUDGET_CATEGORY_COL + " TEXT,"
                        + buildForeignKeyFragment(BUDGET_CATEGORY_COL, CATEGORY_TABLE_NAME, CATEGORY_NAME_COL) + ")"
        );

        db.execSQL(
                "CREATE TABLE " + TRANSACTION_TABLE_NAME + "("
                        + TRANSACTION_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + TRANSACTION_AMOUNT_COL + " REAL NOT NULL,"
                        + TRANSACTION_TEXT_NOTE_COL + " TEXT,"
                        + TRANSACTION_CATEGORY_COL + " TEXT,"
                        + TRANSACTION_IS_RECURRING_COL + " BOOLEAN,"
                        + TRANSACTION_RECURRING_INTERVAL_DAYS_COL + " INTEGER,"
                        + buildForeignKeyFragment(TRANSACTION_CATEGORY_COL, CATEGORY_TABLE_NAME, CATEGORY_NAME_COL) + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS icons");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(db);
    }

    private String buildForeignKeyFragment(String fkCol, String targetTable, String targetTableIdCol) {
        return "FOREIGN KEY(" + fkCol + ") REFERENCES " + targetTable + "(" + targetTableIdCol + ")";
    }
}
