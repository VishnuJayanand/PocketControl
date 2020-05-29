package com.droidlabs.pocketcontrol.db.transaction;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDao {

    /**
     * Insert new transaction into the database.
     * @param transaction transaction to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Transaction transaction);

    /**
     * Delete all transactions from the database.
     */
    @Query("DELETE FROM transactions")
    void deleteAll();

    /**
     * Retrieve all transactions from the database.
     * @return all transactions.
     */
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();

    /**
     * Retrieve all transactions from a specified category.
     * @param categoryId category id
     * @return list of transactions with matching categoryId
     */
    @Query("SELECT * FROM transactions WHERE category=:categoryId ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByCategoryId(String categoryId);

    /**
     * Retrieve all transactions within a specified date range.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @return list of transactions within date range.
     */
    @Query("SELECT * FROM transactions WHERE date BETWEEN :lowerBound AND :upperBound ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByDate(long lowerBound, long upperBound);

    /**
     * Retrieve all transactions from a specified category and within a specified date range.
     * @param categoryId category id.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE category=:categoryId AND date BETWEEN :lowerBound AND :upperBound "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByCategoryAndDate(
            String categoryId,
            long lowerBound,
            long upperBound
    );

    /**
     * Retrieve all transactions from a specified amount  range.
     * @param lowerBoundAmount lower amount bound.
     * @param upperBoundAmount upper amount bound.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions WHERE amount >= :lowerBoundAmount AND amount <= :upperBoundAmount "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByAmount(float lowerBoundAmount, float upperBoundAmount);

    /**
     * Retrieve all transactions from a specified category and within a specified date range and amount.
     * @param categoryId category id.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @param lowerBoundAmount lower amount bound.
     * @param upperBoundAmount upper amount bound.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions WHERE category=:categoryId AND "
            + "(date BETWEEN :lowerBound AND :upperBound) AND "
            + "(amount >= :lowerBoundAmount AND amount <= :upperBoundAmount) ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByCategoryAndDateAndAmount(
            String categoryId, long lowerBound, long upperBound, float lowerBoundAmount, float upperBoundAmount);

    /**
     * Retrieve all transactions within a specified date range and amount.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @param lowerBoundAmount lower amount bound.
     * @param upperBoundAmount upper amount bound.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions WHERE amount >= :lowerBoundAmount AND "
            + "amount <= :upperBoundAmount AND (date BETWEEN :lowerBound AND :upperBound)"
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByAmountAndDate(
            long lowerBound, long upperBound, float lowerBoundAmount, float upperBoundAmount);

    /**
     * Retrieve all transactions from a specified category within a specified amount range.
     * @param categoryId category id.
     * @param lowerBoundAmount lower amount bound.
     * @param upperBoundAmount upper amount bound.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions WHERE category=:categoryId AND "
            + "(amount >= :lowerBoundAmount AND amount <= :upperBoundAmount)"
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByAmountAndCategory(
            String categoryId, float lowerBoundAmount, float upperBoundAmount);

    @Query("UPDATE transactions SET "
            + "is_recurring=:isRecurring, "
            + "recurring_interval_type=:recurringIntervalType, "
            + "recurring_interval_days=:recurringIntervalDays "
            + "WHERE id=:transactionId;")
    void updateTransactionRecurringFields(
            int transactionId,
            Boolean isRecurring,
            Integer recurringIntervalType,
            Integer recurringIntervalDays
    );

    @Query("SELECT COUNT(id) FROM transactions")
    int getTransactionsCount();

}
