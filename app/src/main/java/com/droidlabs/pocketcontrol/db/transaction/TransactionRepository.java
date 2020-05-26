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
    public List<Transaction> filterTransactionsByCategoryId(final String categoryId) {
        return transactionDao.getTransactionsByCategoryId(categoryId);
    }

    /**
     * Get transactions by date range.
     * @param lb date lower bound.
     * @param ub date upper bound.
     * @return all transactions within date range.
     */
    public List<Transaction> filterTransactionsByDate(final long lb, final long ub) {
        return transactionDao.filterTransactionsByDate(lb, ub);
    }

    /**
     * Get transactions by category and date range.
     * @param catId category id.
     * @param lb date lower bound.
     * @param ub date upper bound.
     * @return all transactions of a certain category and within date range.
     */
    public List<Transaction> filterTransactionsByCategoryAndDate(final String catId, final long lb, final long ub) {
        return transactionDao.filterTransactionsByCategoryAndDate(catId, lb, ub);
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
