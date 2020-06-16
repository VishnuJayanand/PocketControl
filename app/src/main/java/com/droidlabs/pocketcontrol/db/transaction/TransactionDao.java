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
     * @return new transaction id.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Transaction transaction);

    /**
     * Delete all transactions from the database.
     */
    @Query("DELETE FROM transactions")
    void deleteAll();

    /**
     * Retrieve all transactions from the database.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return all transactions.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE owner_id=:ownerId "
            + "AND account=:accountId "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions(String ownerId, String accountId);

    /**
     * Get transaction by id.
     * @param transactionId id.
     * @param ownerId owner id.
     * @return transaction.
     */
    @Query("SELECT * FROM transactions WHERE id=:transactionId AND owner_id=:ownerId")
    Transaction getTransactionById(long transactionId, String ownerId);

    /**
     * Retrieve all transactions from a specified category.
     * @param categoryId category id.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return list of transactions with matching categoryId
     */
    @Query("SELECT * FROM transactions "
            + "WHERE category=:categoryId "
            + "AND owner_id=:ownerId "
            + "AND account=:accountId "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> getTransactionsByCategoryId(String categoryId, String ownerId, String accountId);

    /**
     * Retrieve all transactions from a specified category.
     * @param categoryId category id.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return list of transactions with matching categoryId
     */
    @Query("SELECT * FROM transactions "
            + "WHERE category=:categoryId "
            + "AND owner_id=:ownerId "
            + "AND account=:accountId "
            + "ORDER BY date DESC")
    List<Transaction> getAmountByCategoryId(String categoryId, String ownerId, String accountId);

    /**
     * Retrieve sum of transaction income amount for a specified category.
     * @param categoryId category id.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return sum of transaction income amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions "
            + "WHERE category=:categoryId "
            + "AND owner_id=:ownerId "
            + "AND account=:accountId "
            + "AND type = 2")
    float getTotalIncomeByCategoryId(String categoryId, String ownerId, String accountId);

    /**
     * Retrieve sum of transaction expense amount for a specified category.
     * @param categoryId category id.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return sum of transaction expense amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions "
            + "WHERE category=:categoryId "
            + "AND owner_id=:ownerId "
            + "AND account=:accountId "
            + "AND type = 1")
    float getTotalIExpenseByCategoryId(String categoryId, String ownerId, String accountId);

    /**
     * Retrieve sum of transaction income amount for a specified category.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return sum of transaction income amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions "
            + "WHERE owner_id=:ownerId "
            + "AND account=:accountId "
            + "AND type = 2")
    float getTotalIncomeByAccountId(String ownerId, String accountId);

    /**
     * Retrieve sum of transaction expense amount for a specified category.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return sum of transaction expense amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions "
            + "WHERE owner_id=:ownerId "
            + "AND account=:accountId "
            + "AND type = 1")
    float getTotalIExpenseByAccountId(String ownerId, String accountId);

    /**
     * Retrieve sum of transaction income amount for a specified category.
     * @param ownerId owner id.
     * @return sum of transaction income amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions WHERE owner_id=:ownerId AND type = 2")
    float getTotalIncomeByUserId(String ownerId);

    /**
     * Retrieve sum of transaction expense amount for a specified category.
     * @param ownerId owner id.
     * @return sum of transaction expense amount with matching categoryId
     */
    @Query("SELECT SUM(amount) FROM transactions WHERE owner_id=:ownerId AND type = 1")
    float getTotalIExpenseByUserId(String ownerId);

    /**
     * Retrieve all transactions within a specified date range.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return list of transactions within date range.
     */
    @Query("SELECT * FROM transactions WHERE "
            + "owner_id=:ownerId AND account=:accountId "
            + "AND date BETWEEN :lowerBound AND :upperBound "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByDate(
            long lowerBound,
            long upperBound,
            String ownerId,
            String accountId);

    /**
     * Retrieve all transactions from a specified category and within a specified date range.
     * @param categoryId category id.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE category=:categoryId "
            + "AND owner_id=:ownerId "
            + "AND account=:accountId "
            + "AND date BETWEEN :lowerBound AND :upperBound "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByCategoryAndDate(
            String categoryId,
            long lowerBound,
            long upperBound,
            String ownerId,
            String accountId
    );

    /**
     * Retrieve all transactions from a specified amount  range.
     * @param lowerBoundAmount lower amount bound.
     * @param upperBoundAmount upper amount bound.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions WHERE "
            + "owner_id=:ownerId AND account=:accountId "
            + "AND amount >= :lowerBoundAmount "
            + "AND amount <= :upperBoundAmount "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByAmount(
            float lowerBoundAmount,
            float upperBoundAmount,
            String ownerId,
            String accountId
    );

    /**
     * Retrieve all transactions from a specified category and within a specified date range and amount.
     * @param categoryId category id.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @param lowerBoundAmount lower amount bound.
     * @param upperBoundAmount upper amount bound.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE category=:categoryId "
            + "AND owner_id=:ownerId "
            + "AND account=:accountId "
            + "AND (date BETWEEN :lowerBound AND :upperBound) "
            + "AND (amount >= :lowerBoundAmount AND amount <= :upperBoundAmount) "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByCategoryAndDateAndAmount(
            String categoryId,
            long lowerBound,
            long upperBound,
            float lowerBoundAmount,
            float upperBoundAmount,
            String ownerId,
            String accountId
    );

    /**
     * Retrieve all transactions within a specified date range and amount.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @param lowerBoundAmount lower amount bound.
     * @param upperBoundAmount upper amount bound.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE owner_id=:ownerId "
            + "AND account=:accountId "
            + "AND amount >= :lowerBoundAmount "
            + "AND amount <= :upperBoundAmount "
            + "AND (date BETWEEN :lowerBound AND :upperBound) "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByAmountAndDate(
            long lowerBound,
            long upperBound,
            float lowerBoundAmount,
            float upperBoundAmount,
            String ownerId,
            String accountId
    );

    /**
     * Retrieve all transactions from a specified category within a specified amount range.
     * @param categoryId category id.
     * @param lowerBoundAmount lower amount bound.
     * @param upperBoundAmount upper amount bound.
     * @param ownerId owner id.
     * @param accountId account id.
     * @return list of transactions matching the filter.
     */
    @Query("SELECT * FROM transactions "
            + "WHERE category=:categoryId "
            + "AND owner_id=:ownerId "
            + "AND account=:accountId "
            + "AND (amount >= :lowerBoundAmount AND amount <= :upperBoundAmount) "
            + "ORDER BY date DESC")
    LiveData<List<Transaction>> filterTransactionsByAmountAndCategory(
            String categoryId, float lowerBoundAmount, float upperBoundAmount, String ownerId, String accountId);

    /**
     * Update transaction recurring params.
     * @param transactionId id.
     * @param isRecurring flag.
     * @param recurringIntervalType type.
     * @param recurringIntervalDays recurring interval in days.
     * @param ownerId owner id.
     */
    @Query("UPDATE transactions SET "
            + "is_recurring=:isRecurring, "
            + "recurring_interval_type=:recurringIntervalType, "
            + "recurring_interval_days=:recurringIntervalDays "
            + "WHERE id=:transactionId AND owner_id=:ownerId;")
    void updateTransactionRecurringFields(
            int transactionId,
            Boolean isRecurring,
            Integer recurringIntervalType,
            Integer recurringIntervalDays,
            String ownerId
    );
}
