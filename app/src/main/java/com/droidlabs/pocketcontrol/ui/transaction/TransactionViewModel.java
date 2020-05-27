package com.droidlabs.pocketcontrol.ui.transaction;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.transaction.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository repository;
    private boolean filterByCategory = false;
    private String filterCategoryId = "";

    private boolean filterByDate = false;
    private long lowerDateBound = Long.MIN_VALUE;
    private long upperDateBound = Long.MAX_VALUE;

    /**
     * View model constructor.
     * @param application application to be used.
     */
    public TransactionViewModel(final Application application) {
        super(application);
        this.repository = new TransactionRepository(application);
    }

    public LiveData<List<Transaction>> getTransactions() {

        if (filterByCategory && !filterCategoryId.equals("")) {
            if (filterByDate) {
                return repository.filterTransactionsByCategoryAndDate(filterCategoryId, lowerDateBound, upperDateBound);
            } else {
                List<Transaction> list = repository.filterTransactionsByCategoryId(filterCategoryId).getValue();
                if (list != null) {
                    Log.v("SOMETHING", String.valueOf(list.size()));
                }
                return repository.filterTransactionsByCategoryId(filterCategoryId);
            }
        } else {
            if (filterByDate) {
                return repository.filterTransactionsByDate(lowerDateBound, upperDateBound);
            } else {
                return repository.getAllTransactions();
            }
        }
    }

    /**
     * Get all transactions.
     */
    public void getAllTransactions() {
        filterByCategory = false;
        filterByDate = false;

        this.getTransactions();
    }

    /**
     * Get transactions by category ID.
     * @param categoryId category ID
     */
    public void filterTransactionsByCategoryId(final String categoryId) {
        Log.v("FILTER", "Filtering by category");
        filterByCategory = true;
        filterCategoryId = categoryId;

        this.getTransactions();
    }

    /**
     * Get transactions by date range.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @return return all transactions within the specified date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByDate(final long lowerBound, final long upperBound) {
        filterByDate = true;
        lowerDateBound = lowerBound;
        upperDateBound = upperBound;

        return this.getTransactions();
    }

    /**
     * Get transactions by category ID.
     * @param catId category ID
     * @param lb lower date bound.
     * @param ub upper date bound.
     * @return return all transactions with the specified category ID and within the date range.
     */
    public LiveData<List<Transaction>> filterTransactionsByCategoryAndDate(final String catId, final long lb, final long ub) {
        filterByDate = true;
        filterByCategory = true;

        filterCategoryId = catId;
        lowerDateBound = lb;
        upperDateBound = ub;

        return this.getTransactions();
    }

    /**
     * Insert a new transaction in the database.
     * @param transaction transaction to be added.
     */
    public void insert(final Transaction transaction) {
        repository.insert(transaction);
    }

}
