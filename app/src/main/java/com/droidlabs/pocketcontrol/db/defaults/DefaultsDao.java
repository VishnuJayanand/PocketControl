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
     * @param ownerId owner ID
     */
    @Query("DELETE FROM defaults WHERE owner_id=:ownerId")
    void deleteAll(String ownerId);

    /**
     * Retrieve all defaults values.
     * @param ownerId owner id.
     * @return list of defaults.
     */
    @Query("SELECT * FROM defaults WHERE owner_id=:ownerId")
    List<Defaults> getAllDefaults(String ownerId);

    /**
     * Get defaults for export csv.
     * @param ownerId owner id.
     * @return list of defaults.
     */
    @Query("SELECT * FROM defaults WHERE owner_id=:ownerId ORDER BY defaultId ASC")
    List<Defaults> getDefaultsForExport(String ownerId);

    /**
     * Retrieve all defaults category.
     * @param name entity name.
     * @param ownerId owner id.
     * @return string of defaults.
     */
    @Query("SELECT default_value FROM defaults WHERE default_entity=:name AND owner_id=:ownerId")
    String getDefaultValue(String name, String ownerId);

    /**
     * Retrieve all defaults category.
     * string of defaults.
     */
    @Query("DELETE FROM defaults")
    void deleteAll();

    /**
     * Retrieve default currency symbol.
     * @param currency three letter currency code
     * @return string of currency symbol
     */
    @Query("SELECT symbol from currencies WHERE three_letter_code=:currency")
    String getCurrencySymbol(String currency);

    /**
     * Update default value.
     * @param defaultName default name.
     * @param newDefaultValue new value.
     * @param ownerId owner id.
     */
    @Query("UPDATE defaults SET default_value=:newDefaultValue WHERE default_entity=:defaultName AND owner_id=:ownerId")
    void updateDefaultValue(String defaultName, String newDefaultValue, String ownerId);
}
