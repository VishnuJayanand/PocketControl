package com.droidlabs.pocketcontrol.ui.budget;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.db.budget.BudgetRepository;

import java.util.List;

public class BudgetViewModel extends AndroidViewModel {
    private BudgetRepository repository;
    private List<Budget> allBudgets;
    /**
     * View model constructor.
     * @param application application to be used.
     */
    public BudgetViewModel(final Application application) {
        super(application);
        this.repository = new BudgetRepository(application);
        allBudgets = repository.getAllBudgets();
    }

    /**
     * Get all budgets.
     * @return return all transactions from the database.
     */
    public List<Budget> getAllBudgets() {
        return allBudgets;
    }

    /**
     * Get budgets for export csv.
     * @return list of user budgets.
     */
    public List<Budget> getBudgetsForExport() {
        return repository.getBudgetsForExport();
    }

    /**
     * Insert a new budget in the database.
     * @param budget transaction to be added.
     */
    public void insert(final Budget budget) {
        repository.insert(budget);
    }

    /**
     * delete a budget in the database.
     * @param budgetId transaction to be added.
     */
    public void deleteBudget(final int budgetId) {

        repository.deleteBudget(budgetId);
    }

    /**
     * Fetch the budget for the selected category.
     * @param category transaction to be added.
     * @return Budget.
     */
    public Budget getBudgetForCategory(final String category) {
        return repository.getBudgetForCategory(category);
    }

    /**
     * Update budget amounts.
     * @param conversionRate conversion rate.
     */
    public void updateBudgetAmountsDefaultCurrency(final float conversionRate) {
        repository.updateBudgetAmountsDefaultCurrency(conversionRate);
    }
}
