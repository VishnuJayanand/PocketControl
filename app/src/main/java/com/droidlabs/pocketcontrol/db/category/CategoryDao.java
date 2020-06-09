package com.droidlabs.pocketcontrol.db.category;

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
    @Query("SELECT * FROM categories WHERE owner_id=:ownerId")
    List<Category> getAllCategories(String ownerId);

    /**
     * Get single category.
     * @param id category ID
     * @return Single category matching the ID
     */
    @Query("SELECT * FROM categories WHERE id=:id")
    Category getSingleCategory(int id);

    /**
     * Get single category.
     * @param name category name
     * @return Single category matching with name
     */
    @Query("SELECT * FROM categories WHERE name=:name")
    Category getSingleCategory(String name);

    /**
     * Get all categories name from the database.
     * @return list of categories name.
     */
    @Query("SELECT name FROM categories")
    String[] getCategoriesName();
}
