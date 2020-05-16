package com.droidlabs.pocketcontrol.db.category;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {

    // TODO: move to @Query decorator to allow for default values;
    /**
     * Insert a new category into the database.
     * @param category category to be saved.
     * @return category ID
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Category category);

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

    /**
     * Get single category.
     * @param id category ID
     * @return Single category matching the ID
     */
    @Query("SELECT * FROM categories WHERE id=:id")
    Category getSingleCategory(int id);
}
