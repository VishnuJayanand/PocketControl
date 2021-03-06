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
import com.droidlabs.pocketcontrol.db.defaults.Defaults;
import com.droidlabs.pocketcontrol.db.defaults.DefaultsDao;
import com.droidlabs.pocketcontrol.db.icon.Icon;
import com.droidlabs.pocketcontrol.db.icon.IconDao;
import com.droidlabs.pocketcontrol.db.paymentmode.PaymentMode;
import com.droidlabs.pocketcontrol.db.paymentmode.PaymentModeDao;
import com.droidlabs.pocketcontrol.db.account.AccountDao;
import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.db.recurrent.Recurrent;
import com.droidlabs.pocketcontrol.db.recurrent.RecurrentDao;
import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.transaction.TransactionDao;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.db.user.UserDao;

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
        Transaction.class,
        Defaults.class,
        Recurrent.class,
        User.class,
        Account.class
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

    /**
     * Defaults Dao.
     * @return Defaults Dao
     */
    public abstract DefaultsDao defaultsDao();

    /**
     * Recurrent Dao.
     * @return Recurrent Dao
     */
    public abstract RecurrentDao recurrentDao();

    /**
     * Project Dao.
     * @return Project Dao
     */
    public abstract AccountDao projectDao();

    /**
     * User Dao.
     * @return User Dao
     */
    public abstract UserDao userDao();

    private static final String DB_NAME = "PocketControl.db";
    private static final int DB_VERSION = 1;

    private static volatile PocketControlDB dbInstance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService DATABASE_WRITE_EXECUTOR =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(final @NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            DATABASE_WRITE_EXECUTOR.execute(PocketControlDB::populateDatabase);
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
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return dbInstance;
    }

    /**
     * Populates the database at startup.
     */
    private static void populateDatabase() {
        cleanDB();

        CurrencyDao currencyDao = dbInstance.currencyDao();
        PaymentModeDao paymentModeDao = dbInstance.paymentModeDao();

        Currency euro = new Currency("EUR", "€");
        Currency uDollar = new Currency("USD", "$");
        Currency yen = new Currency("JPY", "¥");
        Currency pound = new Currency("GBP", "£");
        Currency rupee = new Currency("INR", "₹");
        Currency ruble = new Currency("RUB", "₽");
        Currency renminbi = new Currency("CNY", "¥");
        Currency won = new Currency("KRW", "₩");
        currencyDao.insert(euro);
        currencyDao.insert(uDollar);
        currencyDao.insert(yen);
        currencyDao.insert(pound);
        currencyDao.insert(rupee);
        currencyDao.insert(ruble);
        currencyDao.insert(renminbi);
        currencyDao.insert(won);

        PaymentMode card = new PaymentMode("Credit Card");
        PaymentMode cash = new PaymentMode("Cash");
         PaymentMode wallet = new PaymentMode("Digital Wallet");
        paymentModeDao.insert(card);
        paymentModeDao.insert(cash);
         paymentModeDao.insert(wallet);

        /*
        BudgetDao budgetDao = dbInstance.budgetDao();
        CategoryDao categoryDao = dbInstance.categoryDao();
        CurrencyDao currencyDao = dbInstance.currencyDao();
        DefaultsDao defaultsDao = dbInstance.defaultsDao();
        IconDao iconDao = dbInstance.iconDao();
        PaymentModeDao paymentModeDao = dbInstance.paymentModeDao();
        TransactionDao transactionDao = dbInstance.transactionDao();
        UserDao userDao = dbInstance.userDao();
        AccountDao accountDao = dbInstance.projectDao();

        User newUser = new User();

        newUser.setAccessPin("1234");
        newUser.setEmail("laurofmuller@gmail.com");
        newUser.setFirstName("Lauro");
        newUser.setLastName("Müller");
        newUser.setPassword("123456");

        long userId = userDao.insert(newUser);

        Calendar someDay = DateUtils.getStartOfCurrentDay();
        someDay.add(Calendar.DAY_OF_YEAR, -8);

        Long today = DateUtils.getStartOfCurrentDay().getTimeInMillis();

        Category health = new Category(1, "Health", R.drawable.health);
        Category transport = new Category(2, "Transport", R.drawable.transport);
        Category shopping = new Category(3, "Shopping", R.drawable.shopping);
        Category categoryIcons_food = new Category(4, "Food", R.drawable.categoryIcons_food);
        Category study = new Category(5, "Study", R.drawable.study);
        Category rent = new Category(6, "Rent", R.drawable.rent);

        health.setOwnerId(String.valueOf(userId));
        transport.setOwnerId(String.valueOf(userId));
        shopping.setOwnerId(String.valueOf(userId));
<<<<<<< HEAD
<<<<<<< HEAD
        food.setOwnerId(String.valueOf(userId));
=======
        categoryIcons_food.setOwnerId(String.valueOf(userId));
>>>>>>> develop
=======
        categoryIcons_food.setOwnerId(String.valueOf(userId));
>>>>>>> develop
        study.setOwnerId(String.valueOf(userId));
        rent.setOwnerId(String.valueOf(userId));

        long healthCatId = categoryDao.insert(health);
        long transportCatId = categoryDao.insert(transport);
        long shoppingCatId = categoryDao.insert(shopping);
        long foodId = categoryDao.insert(categoryIcons_food);
        long studyCatId = categoryDao.insert(study);
        long rentCatId = categoryDao.insert(rent);

        Account account1 = new Account();
        account1.setName("Account 1");
        account1.setColor(R.color.projectColorBlue);
        account1.setOwnerId(String.valueOf(userId));
<<<<<<< HEAD
<<<<<<< HEAD

        Account account2 = new Account();
        account2.setName("Account 2");
        account2.setColor(R.color.projectColorPurple);
        account2.setOwnerId(String.valueOf(userId));

        Account account3 = new Account();
        account3.setName("Account 3");
        account3.setOwnerId(String.valueOf(userId));

=======

        Account account2 = new Account();
        account2.setName("Account 2");
        account2.setColor(R.color.projectColorPurple);
        account2.setOwnerId(String.valueOf(userId));

=======

        Account account2 = new Account();
        account2.setName("Account 2");
        account2.setColor(R.color.projectColorPurple);
        account2.setOwnerId(String.valueOf(userId));

>>>>>>> develop
        Account account3 = new Account();
        account3.setName("Account 3");
        account3.setOwnerId(String.valueOf(userId));

<<<<<<< HEAD
>>>>>>> develop
=======
>>>>>>> develop
        accountDao.insert(account1);
        accountDao.insert(account2);
        accountDao.insert(account3);

        Icon icon = new Icon("icon_1");
        iconDao.insert(icon);

        Budget budget = new Budget(700f, "Monthly budget", true);
        budgetDao.insert(budget);

        Transaction transactionA = new Transaction(300f, 2, String.valueOf(rentCatId), today, "", 1);
        Transaction transactionB = new Transaction(200f, 1, String.valueOf(shoppingCatId), today, "", 1);
        Transaction transactionC = new Transaction(100f, 2, String.valueOf(studyCatId), today, "", 1);
        Transaction transactionD = new Transaction(250f, 1, String.valueOf(transportCatId), today, "", 1);
        Transaction transactionE = new Transaction(250f, 1, String.valueOf(healthCatId), today, "", 1);
        Transaction transactionF = new Transaction(250f, 1, String.valueOf(foodId), today, "", 1);

        Transaction recurringTransaction = new Transaction(
                500f,
                1,
                String.valueOf(rentCatId),
                someDay.getTimeInMillis(),
                "",
                1
        );

        recurringTransaction.setRecurring(true);
        recurringTransaction.setRecurringIntervalType(4);
        recurringTransaction.setRecurringIntervalDays(3);
        recurringTransaction.setFlagIconRecurring(true);

        transactionDao.insert(transactionA);
        transactionDao.insert(transactionB);
        transactionDao.insert(transactionC);
        transactionDao.insert(transactionD);
        transactionDao.insert(transactionE);
        transactionDao.insert(transactionF);

        transactionDao.insert(recurringTransaction);
        */
    }

    /**
     * Clean current DB.
     */
    private static void cleanDB() {
        BudgetDao budgetDao = dbInstance.budgetDao();
        CategoryDao categoryDao = dbInstance.categoryDao();
        CurrencyDao currencyDao = dbInstance.currencyDao();
        DefaultsDao defaultsDao = dbInstance.defaultsDao();
        IconDao iconDao = dbInstance.iconDao();
        PaymentModeDao paymentModeDao = dbInstance.paymentModeDao();
        TransactionDao transactionDao = dbInstance.transactionDao();
        UserDao userDao = dbInstance.userDao();
        AccountDao accountDao = dbInstance.projectDao();

        budgetDao.deleteAll();
        categoryDao.deleteAll();
        currencyDao.deleteAll();
        iconDao.deleteAll();
        paymentModeDao.deleteAll();
        transactionDao.deleteAll();
        defaultsDao.deleteAll();
        userDao.deleteAll();
        accountDao.deleteAll();
    }
}
