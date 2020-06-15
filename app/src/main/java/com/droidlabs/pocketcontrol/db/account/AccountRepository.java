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
        Account account = accountDao.getAccountById(accountId);

        if (account.getIsPublic() != null && account.getIsPublic()) {
            return account;
        }

        // TODO: add validation, user should be owner of account
        return account;
    };
}
