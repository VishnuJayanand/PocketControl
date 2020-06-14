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
}