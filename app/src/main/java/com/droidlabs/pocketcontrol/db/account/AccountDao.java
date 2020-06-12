package com.droidlabs.pocketcontrol.db.account;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AccountDao {
    /**
     * Insert new account into the database.
     * @param account project to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Account account);

    /**
     * Delete all accounts from the database.
     */
    @Query("DELETE FROM accounts")
    void deleteAll();

    /**
     * Retrieve all accounts from the database.
     * @return all accounts.
     */
    @Query("SELECT * FROM accounts WHERE is_public=1 OR owner_id=:userId")
    LiveData<List<Account>> getAllAccounts(String userId);

    /**
     * Get account by id.
     * @param accountId id.
     * @return account.
     */
    @Query("SELECT * FROM accounts WHERE id=:accountId")
    Account getAccountById(long accountId);
}
