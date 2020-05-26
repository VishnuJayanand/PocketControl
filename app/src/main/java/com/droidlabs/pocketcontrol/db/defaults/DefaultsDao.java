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

    /**
     * Retrieve all defaults category.
     * @return string of defaults.
     * @param name entity name
     */
    @Query("SELECT default_value FROM defaults WHERE default_entity=:name")
    String getDefaultValue(String name);
}
