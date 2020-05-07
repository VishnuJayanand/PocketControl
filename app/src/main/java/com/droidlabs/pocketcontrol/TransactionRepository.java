package com.droidlabs.pocketcontrol;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TransactionRepository {

    private TransactionDao mTransactionDao;
    private LiveData<List<Transaction>> mAllTransactions;

    TransactionRepository(Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        mTransactionDao = db.transactionDao();
        mAllTransactions = mTransactionDao.getAllTransactions();
    }

    LiveData<List<Transaction>> getAllTransactions() {
        return mAllTransactions;
    }

    void insert(final Transaction transaction) {
        PocketControlDB.databaseWriteExecutor.execute(() -> {
            mTransactionDao.insert(transaction);
        });
    }
}
