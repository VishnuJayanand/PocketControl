package com.droidlabs.pocketcontrol.db.budget;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.db.user.UserDao;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Budget repository class.
 */
public class BudgetRepository {

    private BudgetDao budgetDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Creates a new budget repository.
     * @param application application to the used.
     */
    public BudgetRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        budgetDao = db.budgetDao();
    }

    /**
     * Get all budgets saved on the DB.
     * @return list of saved budgets.
     */
    public List<Budget> getAllBudgets() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return budgetDao.getAllBudgets(currentUserId);
    }

    /**
     * Save a new budget to the db.
     * @param budget budget to be saved.
     */
    public void insert(final Budget budget) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        budget.setOwnerId(currentUserId);

        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            budgetDao.insert(budget);
        });
    }
}
