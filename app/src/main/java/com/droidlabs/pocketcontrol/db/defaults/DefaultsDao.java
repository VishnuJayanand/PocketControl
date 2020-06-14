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
     * @param ownerId owner id.
     * @return list of defaults.
     */
    @Query("SELECT * FROM defaults WHERE owner_id=:ownerId")
    List<Defaults> getAllDefaults(String ownerId);

    /**
     * Retrieve all defaults category.
     * @param name entity name.
     * @param ownerId owner id.
     * @return string of defaults.
     */
    @Query("SELECT default_value FROM defaults WHERE default_entity=:name AND owner_id=:ownerId")
    String getDefaultValue(String name, String ownerId);
}
