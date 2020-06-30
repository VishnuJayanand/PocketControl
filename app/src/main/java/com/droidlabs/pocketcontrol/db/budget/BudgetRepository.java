package com.droidlabs.pocketcontrol.db.budget;

import android.app.Application;

import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.user.UserDao;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Budget repository class.
 */
public class BudgetRepository {

    private BudgetDao budgetDao;
    private UserDao userDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Creates a new budget repository.
     * @param application application to the used.
     */
    public BudgetRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        budgetDao = db.budgetDao();
        userDao = db.userDao();
    }

    /**
     * Get all budgets saved on the DB.
     * @return list of saved budgets.
     */
    public List<Budget> getAllBudgets() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return budgetDao.getAllBudgets(currentUserId, currentAccountId);
    }

    /**
     * Get budgets for export csv.
     * @return list of user budgets.
     */
    public List<Budget> getBudgetsForExport() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return budgetDao.getBudgetsForExport(currentUserId);
    }

    /**
     * Save a new budget to the db.
     * @param budget budget to be saved.
     */
    public void insert(final Budget budget) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return;
        }

        budget.setOwnerId(currentUserId);
        budget.setAccount(currentAccountId);

        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            budgetDao.insert(budget);
        });
    }

    /**
     * Fetch the budget for the selected category.
     * @param category amount lower bound.
     * @return Budget.
     */
    public Budget getBudgetForCategory(final String category) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return budgetDao.getBudgetForCategory(category, currentUserId, currentAccountId);
    }

    /**
     * delete a budget from DB.
     * @param budgetId budget to be saved.
     */
    public void deleteBudget(final int budgetId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return;
        }

        budgetDao.deleteBudget(budgetId, currentUserId, currentAccountId);
    }

    /**
     * Convert budgets.
     * @param conversionRate conversion rate.
     */
    public void updateBudgetAmountsDefaultCurrency(final float conversionRate) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return;
        }

        budgetDao.updateBudgetAmountsDefaultCurrency(conversionRate, currentUserId);
    }
}
