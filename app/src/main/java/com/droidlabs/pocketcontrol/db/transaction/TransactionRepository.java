package com.droidlabs.pocketcontrol.db.transaction;

import android.app.Application;

import com.droidlabs.pocketcontrol.db.PocketControlDB;

import java.util.List;

public class TransactionRepository {

    private TransactionDao transactionDao;
    private List<Transaction> allTransactions;

    /**
     * Transaction repository constructor.
     * @param application application to be used.
     */
    public TransactionRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        transactionDao = db.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
    }

    /**
     * Get all transactions from the database.
     * @return all transactions in the database.
     */
    public List<Transaction> getAllTransactions() {
        return allTransactions;
    }

    /**
     * Get transactions by categoryID.
     * @param categoryId category id.
     * @return all transactions with the corresponding categoryID.
     */
    public List<Transaction> getTransactionsByCategoryId(final String categoryId) {
        return transactionDao.getTransactionsByCategoryId(categoryId);
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
