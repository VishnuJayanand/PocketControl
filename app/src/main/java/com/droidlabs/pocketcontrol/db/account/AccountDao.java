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
     * @return account id.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Account account);

    /**
     * Delete all accounts from the database.
     */
    @Query("DELETE FROM accounts")
    void deleteAll();

    /**
     * Retrieve all accounts from the database.
     * @param ownerId account owner.
     * @return all accounts.
     */
    @Query("SELECT * FROM accounts WHERE owner_id=:ownerId")
    LiveData<List<Account>> getAllAccounts(String ownerId);

    /**
     * Get accounts for export csv.
     * @param ownerId owner id.
     * @return list of accounts.
     */
    @Query("SELECT * FROM accounts WHERE owner_id=:ownerId ORDER BY id ASC")
    List<Account> getAccountsForExport(String ownerId);

    /**
     * Get account by id.
     * @param accountId id.
     * @param ownerId owner id.
     * @return account.
     */
    @Query("SELECT * FROM accounts WHERE id=:accountId AND owner_id=:ownerId")
    Account getAccountById(int accountId, String ownerId);

    /**
     * Get account by name.
     * @param accountName name.
     * @param ownerId owner id.
     * @return account.
     */
    @Query("SELECT * FROM accounts WHERE name=:accountName AND owner_id=:ownerId")
    Account getAccountByName(String accountName, String ownerId);

    /**
     * Get account names.
     * @param ownerId owner id.
     * @return array of names.
     */
    @Query("SELECT name FROM accounts WHERE owner_id=:ownerId")
    String[] getAccountNames(String ownerId);
}
