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
     * Insert a new budget in the database.
     * @param budget transaction to be added.
     */
    public void insert(final Budget budget) {
        repository.insert(budget);
    }

    /**
     * Fetch the budget for the selected category.
     * @param category transaction to be added.
     * @return Budget.
     */
    public Budget getBudgetForCategory(final String category) {
        return repository.getBudgetForCategory(category);
    }
}