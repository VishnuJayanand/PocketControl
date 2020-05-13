package com.droidlabs.pocketcontrol.db.transaction;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.PocketControlDB;

import java.util.List;

public class TransactionRepository {

    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> allTransactions;

    /**
     * Transaction repository constructor.
     * @param application application to be used.
     */
    TransactionRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        transactionDao = db.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
    }

    /**
     * Get all transactions from the database.
     * @return all transactions in the database.
     */
    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    /**
     * Insert a new transaction in the database.
     * @param transaction transaction to be saved.
     */
    public void insert(final Transaction transaction) {
        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            transactionDao.insert(transaction);
        });
    }
}
