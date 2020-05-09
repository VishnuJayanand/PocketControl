package com.droidlabs.pocketcontrol.db.category;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    /**
     * Insert a new category into the database.
     * @param category category to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Category category);

    /**
     * Delete all categories from the database.
     */
    @Query("DELETE FROM categories")
    void deleteAll();

    /**
     * Get all categories from the database.
     * @return list of categories.
     */
    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();
}
