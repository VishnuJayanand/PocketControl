package com.droidlabs.pocketcontrol.db.budget;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BudgetDao {
    /**
     * Insert one budget into db.
     * @param budget budget to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Budget budget);

    /**
     * Delete all budgets.
     */
    @Query("DELETE FROM budgets")
    void deleteAll();

    /**
     * Retrieve all budgets.
     * @return list of budgets saved on db.
     */
    @Query("SELECT * FROM budgets WHERE owner_id=:ownerId")
    List<Budget> getAllBudgets(String ownerId);

    /**
     * Retrieve all budgets.
     * @param category is the category
     * @return list of budgets saved on db.
     */
    @Query("SELECT * FROM budgets WHERE category=:category AND owner_id=:ownerId")
    Budget getBudgetForCategory(String category, String ownerId);
}
