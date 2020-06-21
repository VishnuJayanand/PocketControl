package com.droidlabs.pocketcontrol.db.transaction;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class TransactionRepository {

    private TransactionDao transactionDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Transaction repository constructor.
     * @param application application to be used.
     */
    public TransactionRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        transactionDao = db.transactionDao();
    }

    /**
     * Get all transactions from the database.
     * @return all transactions in the database.
     */
    public LiveData<List<Transaction>> getAllTransactions() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getAllTransactions(currentUserId, currentAccountId);
    }

    /**
     * Get transaction by id.
     * @param transactionId id.
     * @return transaction.
     */
    public Transaction getTransactionById(final long transactionId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getTransactionById(transactionId, currentUserId);
    };

    /**
     * Get transactions by categoryID.
     * @param categoryId category id.
     * @return all transactions with the corresponding categoryID.
     */
    public LiveData<List<Transaction>> filterTransactionsByCategoryId(final String categoryId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getTransactionsByCategoryId(categoryId, currentUserId, currentAccountId);
    }

    /**
     * Get sum of transaction income amount by categoryID.
     * @param categoryId category id.
     * @return sum of transaction income amount with matching categoryId
     */
    public Float getTotalIncomeByCategoryId(final String categoryId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getTotalIncomeByCategoryId(categoryId, currentUserId, currentAccountId);
    }

    /**
     * Get sum of transaction expense amount by categoryID.
     * @param categoryId category id.
     * @return sum of transaction expense amount with matching categoryId
     */
    public Float getTotalIExpenseByCategoryId(final String categoryId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getTotalIExpenseByCategoryId(categoryId, currentUserId, currentAccountId);
    }

    /**
     * Get sum of transaction income amount by categoryID.
     * @return sum of transaction income amount with matching categoryId
     */
    public Float getTotalIncomeByAccountId() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getTotalIncomeByAccountId(currentUserId, currentAccountId);
    }

    /**
     * Get sum of transaction expense amount by categoryID.
     * @return sum of transaction expense amount with matching categoryId
     */
    public Float getTotalIExpenseByAccountId() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getTotalIExpenseByAccountId(currentUserId, currentAccountId);
    }

    /**
     * Get sum of transaction income amount by categoryID.
     * @return sum of transaction income amount with matching categoryId
     */
    public Float getTotalIncomeByUserId() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getTotalIncomeByUserId(currentUserId);
    }

    /**
     * Get sum of transaction expense amount by categoryID.
     * @return sum of transaction expense amount with matching categoryId
     */
    public Float getTotalIExpenseByUserId() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getTotalIExpenseByUserId(currentUserId);
    }


    /**
     * Get transactions by date range.
     * @param lb date lower bound.
     * @param ub date upper bound.
     * @return all transactions within date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByDate(final long lb, final long ub) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.filterTransactionsByDate(lb, ub, currentUserId, currentAccountId);
    }

    /**
     * Get transactions by date range.
     * @param categoryId date lower bound.
     * @return all transactions within date range.
     */
    public List<Transaction> getAmountByCategoryId(final String categoryId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.getAmountByCategoryId(categoryId, currentUserId, currentAccountId);
    }

    /**
     * Get transactions by amount range.
     * @param lba amount lower bound.
     * @param uba amount upper bound.
     * @return all transactions within date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByAmount(final float lba, final float uba) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.filterTransactionsByAmount(lba, uba, currentUserId, currentAccountId);
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
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.filterTransactionsByCategoryAndDate(catId, lb, ub, currentUserId, currentAccountId);
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
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.filterTransactionsByCategoryAndDateAndAmount(
                catId,
                lb,
                ub,
                lba,
                uba,
                currentUserId,
                currentAccountId
        );
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
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.filterTransactionsByAmountAndDate(lb, ub, lba, uba, currentUserId, currentAccountId);
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
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return transactionDao.filterTransactionsByAmountAndCategory(catId, lba, uba, currentUserId, currentAccountId);
    }

    /**
     * Insert a new transaction in the database.
     * @param transaction transaction to be saved.
     * @return transaction id.
     */
    public Long insert(final Transaction transaction) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        transaction.setOwnerId(currentUserId);
        transaction.setAccount(currentAccountId);

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
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return;
        }

        transactionDao.updateTransactionRecurringFields(
                transactionId,
                isRecurring,
                recurringIntervalType,
                recurringIntervalDays,
                currentUserId
        );
    }

    /**
     * Update transaction amounts.
     * @param conversionRate conversion rate.
     */
    public void updateTransactionAmountsDefaultCurrency(final float conversionRate) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return;
        }

        transactionDao.updateTransactionAmountsDefaultCurrency(conversionRate, currentUserId);
    }

    /**
     * Update transaction recurring fields.
     * @param transactionId id.
     */
    public void deleteTransaction(final int transactionId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return;
        }

        transactionDao.deleteTransaction(
                transactionId, currentUserId, currentAccountId
        );
    }
}
