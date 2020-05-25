package com.droidlabs.pocketcontrol.db.defaults;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface DefaultsDao {
    /**
     * Insert new default.
     * @param defaults value to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Defaults defaults);

    /**
     * Delete all defaults.
     */
    @Query("DELETE FROM defaults")
    void deleteAll();

    /**
     * Retrieve all defaults values.
     * @return list of defaults.
     */
    @Query("SELECT * FROM defaults")
    List<Defaults> getAllDefaults();
}
