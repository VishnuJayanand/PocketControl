package com.droidlabs.pocketcontrol.db.account;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class AccountRepository {

    private AccountDao accountDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Constructor.
     * @param application application.
     */
    public AccountRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        accountDao = db.projectDao();
    }

    /**
     * Get all projects from the database.
     * @return all projects in the database.
     */
    public LiveData<List<Account>> getAllAccounts() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return accountDao.getAllAccounts(currentUserId);
    }

    /**
     * Get account by id.
     * @param accountId id.
     * @return transaction.
     */
    public Account getAccountById(final long accountId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        Account account = accountDao.getAccountById(accountId, currentUserId);

        return account;
    };

    public Long insert(final Account account) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        account.setOwnerId(currentUserId);

        return accountDao.insert(account);
    }
}
