package com.droidlabs.pocketcontrol.db.transaction;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository mRepository;
    private LiveData<List<Transaction>> mAllTransactions;

    public TransactionViewModel(Application application) {
        super(application);
        mRepository = new TransactionRepository(application);
        mAllTransactions = mRepository.getAllTransactions();
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return mAllTransactions;
    }

    public void insert(Transaction transaction) {
        mRepository.insert(transaction);
    }
}
