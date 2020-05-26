package com.droidlabs.pocketcontrol.ui.transaction;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.transaction.TransactionRepository;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository repository;
    private List<Transaction> allTransactions;

    /**
     * View model constructor.
     * @param application application to be used.
     */
    public TransactionViewModel(final Application application) {
        super(application);
        this.repository = new TransactionRepository(application);
        allTransactions = repository.getAllTransactions();
    }

    /**
     * Get all transactions.
     * @return return all transactions from the database.
     */
    public List<Transaction> getAllTransactions() {
        return allTransactions;
    }

    /**
     * Get transactions by category ID.
     * @param categoryId category ID
     * @return return all transactions with the specified category ID.
     */
    public List<Transaction> filterTransactionsByCategoryId(final String categoryId) {
        return repository.filterTransactionsByCategoryId(categoryId);
    }

    /**
     * Get transactions by date range.
     * @param lowerBound lower date bound.
     * @param upperBound upper date bound.
     * @return return all transactions within the specified date range.
     */
    public List<Transaction> filterTransactionsByDate(final long lowerBound, final long upperBound) {
        return repository.filterTransactionsByDate(lowerBound, upperBound);
    }

    /**
     * Get transactions by category ID.
     * @param catId category ID
     * @param lb lower date bound.
     * @param ub upper date bound.
     * @return return all transactions with the specified category ID and within the date range.
     */
    public List<Transaction> filterTransactionsByCategoryAndDate(final String catId, final long lb, final long ub) {
        return repository.filterTransactionsByCategoryAndDate(catId, lb, ub);
    }

    /**
     * Insert a new transaction in the database.
     * @param transaction transaction to be added.
     */
    public void insert(final Transaction transaction) {
        repository.insert(transaction);
    }

}
