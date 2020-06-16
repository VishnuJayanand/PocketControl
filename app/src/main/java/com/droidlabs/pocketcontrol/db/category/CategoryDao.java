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
     * @param ownerId owner id.
     * @return list of categories.
     */
    @Query("SELECT * FROM categories WHERE owner_id=:ownerId AND (account=:accountId OR is_public = 1)")
    List<Category> getAllCategories(String ownerId, String accountId);

    /**
     * Get single category.
     * @param id category ID.
     * @param ownerId owner id.
     * @return Single category matching the ID
     */
    @Query("SELECT * FROM categories WHERE id=:id AND owner_id=:ownerId")
    Category getSingleCategory(int id, String ownerId);

    /**
     * Get single category.
     * @param name category name.
     * @param ownerId owner id.
     * @return Single category matching with name
     */
    @Query("SELECT * FROM categories WHERE name=:name AND owner_id=:ownerId")
    Category getSingleCategory(String name, String ownerId);

    /**
     * Get all categories name from the database.
     * @param ownerId owner id.
     * @return list of categories name.
     */
    @Query("SELECT name FROM categories WHERE owner_id=:ownerId AND (account=:accountId OR is_public = 1)")
    String[] getCategoriesName(String ownerId, String accountId);
}
