package com.droidlabs.pocketcontrol;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.transaction.TransactionDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper Room class for DB management.
 */
@Database(entities = {Transaction.class}, version = 1, exportSchema = false)
public abstract class PocketControlDB extends RoomDatabase {

    public abstract TransactionDao transactionDao();

    private static final String DB_NAME = "PocketControl.db";
    private static final int DB_VERSION = 1;

    private static volatile PocketControlDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                TransactionDao dao = INSTANCE.transactionDao();
                dao.deleteAll();

                Transaction transaction = new Transaction("Transaction 1", 700);
                dao.insert(transaction);
                transaction = new Transaction("Transaction 2", 800);
                dao.insert(transaction);
            });
        }
    };

    static PocketControlDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PocketControlDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PocketControlDB.class, DB_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

/*
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

 */