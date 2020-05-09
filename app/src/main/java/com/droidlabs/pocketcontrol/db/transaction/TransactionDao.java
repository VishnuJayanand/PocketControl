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
    @Query("SELECT * FROM transactions")
    LiveData<List<Transaction>> getAllTransactions();
}
