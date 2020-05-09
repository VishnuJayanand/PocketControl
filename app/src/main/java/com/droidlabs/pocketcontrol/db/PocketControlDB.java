package com.droidlabs.pocketcontrol.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.db.budget.BudgetDao;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;
import com.droidlabs.pocketcontrol.db.currency.Currency;
import com.droidlabs.pocketcontrol.db.currency.CurrencyDao;
import com.droidlabs.pocketcontrol.db.icon.Icon;
import com.droidlabs.pocketcontrol.db.icon.IconDao;
import com.droidlabs.pocketcontrol.db.paymentmode.PaymentMode;
import com.droidlabs.pocketcontrol.db.paymentmode.PaymentModeDao;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.transaction.TransactionDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper Room class for DB management.
 */
@Database(entities = {
        Budget.class,
        Category.class,
        Currency.class,
        Icon.class,
        PaymentMode.class,
        Transaction.class
}, version = 1, exportSchema = false)
public abstract class PocketControlDB extends RoomDatabase {

    /**
     * Budget Dao.
     * @return Budget Dao
     */
    public abstract BudgetDao budgetDao();

    /**
     * Category Dao.
     * @return Category Dao
     */
    public abstract CategoryDao categoryDao();

    /**
     * Currency Dao.
     * @return Currency Dao
     */
    public abstract CurrencyDao currencyDao();

    /**
     * Icon Dao.
     * @return Icon Dao
     */
    public abstract IconDao iconDao();

    /**
     * Payment Mode Dao.
     * @return Payment Mode Dao
     */
    public abstract PaymentModeDao paymentModeDao();

    /**
     * Transaction Dao.
     * @return Transaction Dao
     */
    public abstract TransactionDao transactionDao();

    private static final String DB_NAME = "PocketControl.db";
    private static final int DB_VERSION = 1;

    private static volatile PocketControlDB dbInstance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService DATABASE_WRITE_EXECUTOR =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(final @NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateBudgets);
            DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateCategories);
            DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateCurrencies);
            DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateIcons);
            DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populatePaymentModes);
            DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateTransactions);
        }
    };

    /**
     * Database getter.
     * @param context application context.
     * @return database instance.
     */
    public static PocketControlDB getDatabase(final Context context) {
        if (dbInstance == null) {
            synchronized (PocketControlDB.class) {
                if (dbInstance == null) {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                            PocketControlDB.class, DB_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return dbInstance;
    }

    /**
     * Populates the database at startup.
     */
    public static void populateDatabase() {
        DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateBudgets);
        DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateCategories);
        DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateCurrencies);
        DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateIcons);
        DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populatePaymentModes);
        DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateTransactions);
    }

    /**
     * Populates budgets at startup.
     */
    private static void populateBudgets() {
        BudgetDao dao = dbInstance.budgetDao();
        dao.deleteAll();

        Budget budget = new Budget(0f, "", true);
        dao.insert(budget);
    }

    /**
     * Populates categories at startup.
     */
    private static void populateCategories() {
        CategoryDao dao = dbInstance.categoryDao();
        dao.deleteAll();

        Category category = new Category("Category 1");
        dao.insert(category);
    }

    /**
     * Populates currencies at startup.
     */
    private static void populateCurrencies() {
        CurrencyDao dao = dbInstance.currencyDao();
        dao.deleteAll();

        Currency currency = new Currency("EUR");
        dao.insert(currency);
    }

    /**
     * Populates icons at startup.
     */
    private static void populateIcons() {
        IconDao dao = dbInstance.iconDao();
        dao.deleteAll();

        Icon icon = new Icon("icon_1");
        dao.insert(icon);
    }

    /**
     * Populates payment modes at startup.
     */
    private static void populatePaymentModes() {
        PaymentModeDao dao = dbInstance.paymentModeDao();
        dao.deleteAll();

        PaymentMode paymentMode = new PaymentMode("Credit card");
        dao.insert(paymentMode);
    }

    /**
     * Populates transactions at startup.
     */
    private static void populateTransactions() {
        TransactionDao dao = dbInstance.transactionDao();
        dao.deleteAll();

        Transaction transaction = new Transaction(0, "Transaction 1");
        dao.insert(transaction);
        transaction = new Transaction(0, "Transaction 2");
        dao.insert(transaction);
    }

}
