package com.droidlabs.pocketcontrol.db.recurrent;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RecurrentDao {

    /**
     * Add new recurrent.
     * @param recurrent recurrent.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Recurrent recurrent);

    /**
     * Delete all recurrents.
     */
    @Query("DELETE FROM recurrents")
    void deleteAll();

    /**
     * Retrieve recurrent by date.
     * @param date current date.
     * @param ownerId owner id.
     * @return recurrent.
     */
    @Query("SELECT * FROM recurrents WHERE date=:date AND owner_id=:ownerId")
    Recurrent getRecurrentByDate(Long date, String ownerId);
}
