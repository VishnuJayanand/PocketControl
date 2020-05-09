package com.droidlabs.pocketcontrol.db.transaction;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Transaction transaction);

    @Query("DELETE FROM transactions")
    void deleteAll();

    @Query("SELECT * FROM transactions")
    LiveData<List<Transaction>> getAllTransactions();
}
