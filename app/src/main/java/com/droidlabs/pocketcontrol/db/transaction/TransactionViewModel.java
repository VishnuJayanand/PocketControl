package com.droidlabs.pocketcontrol.db.transaction;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

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
     * Insert a new transaction in the database.
     * @param transaction transaction to be added.
     */
    public void insert(final Transaction transaction) {
        repository.insert(transaction);
    }
}
