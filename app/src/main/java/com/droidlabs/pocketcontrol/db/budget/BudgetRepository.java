package com.droidlabs.pocketcontrol.db.budget;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.PocketControlDB;

import java.util.List;

/**
 * Budget repository class.
 */
public class BudgetRepository {

    private BudgetDao budgetDao;
    private LiveData<List<Budget>> allBudgets;

    /**
     * Creates a new budget repository.
     * @param application application to the used.
     */
    BudgetRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        budgetDao = db.budgetDao();
        allBudgets = budgetDao.getAllBudgets();
    }

    /**
     * Get all budgets saved on the DB.
     * @return list of saved budgets.
     */
    public LiveData<List<Budget>> getAllBudgets() {
        return allBudgets;
    }

    /**
     * Save a new budget to the db.
     * @param budget budget to be saved.
     */
    public void insert(final Budget budget) {
        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            budgetDao.insert(budget);
        });
    }
}
