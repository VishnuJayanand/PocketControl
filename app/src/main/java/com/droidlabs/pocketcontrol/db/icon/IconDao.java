package com.droidlabs.pocketcontrol.db.icon;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IconDao {
    /**
     * Insert new icon into the database.
     * @param icon icon to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Icon icon);

    /**
     * Delete all icons from the database.
     */
    @Query("DELETE FROM icons")
    void deleteAll();

    /**
     * Retrieve all saved icons.
     * @return list of saved icons.
     */
    @Query("SELECT * FROM icons")
    List<Icon> getAllIcons();
}
