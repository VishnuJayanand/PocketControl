package com.droidlabs.pocketcontrol.db.recurrent;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RecurrentDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Recurrent recurrent);

    /**
     * Delete all transactions from the database.
     */
    @Query("DELETE FROM recurrents")
    void deleteAll();

    /**
     * Retrieve all transactions from the database.
     * @return all transactions.
     */
    @Query("SELECT * FROM recurrents WHERE date=:date")
    Recurrent getRecurrentByDate(Long date);
}
