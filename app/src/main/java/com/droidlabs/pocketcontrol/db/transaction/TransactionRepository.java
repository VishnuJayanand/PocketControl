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
    public TransactionRepository(final Application application) {
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
     * Get transaction by id.
     * @param transactionId id.
     * @return transaction.
     */
    public Transaction getTransactionById(final long transactionId) {
        return transactionDao.getTransactionById(transactionId);
    };

    /**
     * Get transactions by categoryID.
     * @param categoryId category id.
     * @return all transactions with the corresponding categoryID.
     */
    public LiveData<List<Transaction>> filterTransactionsByCategoryId(final String categoryId) {
        return transactionDao.getTransactionsByCategoryId(categoryId);
    }

    /**
     * Get sum of transaction income amount by categoryID.
     * @param categoryId category id.
     * @return sum of transaction income amount with matching categoryId
     */
    public Float getTotalIncomeByCategoryId(final String categoryId) {
        return transactionDao.getTotalIncomeByCategoryId(categoryId);
    }

    /**
     * Get sum of transaction expense amount by categoryID.
     * @param categoryId category id.
     * @return sum of transaction expense amount with matching categoryId
     */
    public Float getTotalIExpenseByCategoryId(final String categoryId) {
        return transactionDao.getTotalIExpenseByCategoryId(categoryId);
    }


    /**
     * Get transactions by date range.
     * @param lb date lower bound.
     * @param ub date upper bound.
     * @return all transactions within date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByDate(final long lb, final long ub) {
        return transactionDao.filterTransactionsByDate(lb, ub);
    }

    /**
     * Get transactions by date range.
     * @param categoryId date lower bound.
     * @return all transactions within date range.
     */
    public List<Transaction> getAmountByCategoryId(final String categoryId) {
        return transactionDao.getAmountByCategoryId(categoryId);
    }

    /**
     * Get transactions by amount range.
     * @param lba amount lower bound.
     * @param uba amount upper bound.
     * @return all transactions within date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByAmount(final float lba, final float uba) {
        return transactionDao.filterTransactionsByAmount(lba, uba);
    }

    /**
     * Get transactions by category and date range.
     * @param catId category id.
     * @param lb date lower bound.
     * @param ub date upper bound.
     * @return all transactions of a certain category and within date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByCategoryAndDate(
            final String catId,
            final long lb,
            final long ub
    ) {
        return transactionDao.filterTransactionsByCategoryAndDate(catId, lb, ub);
    }

    /**
     * Get transactions by category and date range along with amunt range.
     * @param catId category id.
     * @param lb date lower bound.
     * @param ub date upper bound.
     * @param lba amount lower bound.
     * @param uba amount upper bound.
     * @return all transactions of a certain category and within date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByCategoryAndDateAndAmount(
            final String catId, final long lb, final long ub, final float lba, final float uba) {
        return transactionDao.filterTransactionsByCategoryAndDateAndAmount(catId, lb, ub, lba, uba);
    }

    /**
     * Get transactions by amount range and the selected time period.
     * @param lb lower date bound.
     * @param ub upper date bound.
     * @param lba lower amount bound.
     * @param uba upper amount bound.
     * @return return all transactions with the specified category ID and within the date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByAmountAndDate(
            final long lb, final long ub, final float lba, final float uba) {
        return transactionDao.filterTransactionsByAmountAndDate(lb, ub, lba, uba);
    }

    /**
     * Get transactions by amount range and the selected category id.
     * @param catId category id
     * @param lba lower amount bound.
     * @param uba upper amount bound.
     * @return return all transactions with the specified category ID and within the date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByAmountAndCategory(
            final String catId, final float lba, final float uba) {
        return transactionDao.filterTransactionsByAmountAndCategory(catId, lba, uba);
    }

    /**
     * Insert a new transaction in the database.
     * @param transaction transaction to be saved.
     * @return transaction id.
     */
    public long insert(final Transaction transaction) {
        return transactionDao.insert(transaction);
    }

    /**
     * Update transaction recurring fields.
     * @param transactionId id.
     * @param isRecurring flag.
     * @param recurringIntervalType type.
     * @param recurringIntervalDays interval in days.
     */
    public void updateTransactionRecurringFields(
            final int transactionId,
            final Boolean isRecurring,
            final Integer recurringIntervalType,
            final Integer recurringIntervalDays
    ) {
        transactionDao.updateTransactionRecurringFields(
                transactionId,
                isRecurring,
                recurringIntervalType,
                recurringIntervalDays
        );
    }

}
