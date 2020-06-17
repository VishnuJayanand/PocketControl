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
     * @param ownerId owner id.
     * @param accountId account id.
     * @return list of budgets saved on db.
     */
    @Query("SELECT * FROM budgets WHERE owner_id=:ownerId AND account=:accountId")
    List<Budget> getAllBudgets(String ownerId, String accountId);

    /**
     * Retrieve all budgets.
     * @param category is the category.
     * @param accountId account id.
     * @param ownerId owner id.
     * @return list of budgets saved on db.
     */
    @Query("SELECT * FROM budgets WHERE category=:category AND owner_id=:ownerId AND account=:accountId")
    Budget getBudgetForCategory(String category, String ownerId, String accountId);
}
