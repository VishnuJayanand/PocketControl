package com.droidlabs.pocketcontrol.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.db.account.AccountRepository;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {

    private AccountRepository accountRepository;

    /**
     * Constructor.
     * @param application application.
     */
    public AccountViewModel(final Application application) {
        super(application);

        this.accountRepository = new AccountRepository(application);
    }

    /**
     * Get all accounts.
     * @return all accounts.
     */
    public LiveData<List<Account>> getAccounts() {
        return accountRepository.getAllAccounts();
    }

    /**
     * Get account by id.
     * @param accId id.
     * @return account.
     */
    public Account getAccountById(final int accId) {
        return accountRepository.getAccountById(accId);
    }

    /**
     * Get account by name.
     * @param accName name.
     * @return account.
     */
    public Account getAccountByName(final String accName) {
        return accountRepository.getAccountByName(accName);
    }

    /**
     * Get list of account names.
     * @return account names.
     */
    public String[] getAccountNames() {
        return accountRepository.getAccountNames();
    }

    /**
     * Insert account.
     * @param account account to be saved.
     * @return account id.
     */
    public long insert(final Account account) {
        return accountRepository.insert(account);
    }
}
