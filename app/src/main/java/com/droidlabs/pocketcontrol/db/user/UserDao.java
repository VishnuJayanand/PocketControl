package com.droidlabs.pocketcontrol.db.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    /**
     * Insert new user into the database.
     * @param user user to be saved.
     * @return user id.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(User user);

    /**
     * Delete all users from the database.
     */
    @Query("DELETE FROM users")
    void deleteAll();

    /**
     * Retrieve all users from the database.
     * @return all users.
     */
    @Query("SELECT id, first_name, last_name, email FROM users")
    LiveData<List<User>> getAllUsers();

    /**
     * Retrieve a specific user from the database.
     * @param userId user id.
     * @return user.
     */
    @Query("SELECT * FROM users WHERE id=:userId")
    User getUserById(int userId);

    /**
     * Retrieve a specific user from the database.
     * @param userEmail user email.
     * @return user.
     */
    @Query("SELECT * FROM users WHERE email=:userEmail")
    User getUserByEmail(String userEmail);

    /**
     * Update user selected account.
     * @param userId id.
     * @param selectedAccount new selected account.
     */
    @Query("UPDATE users SET "
            + "selected_account=:selectedAccount "
            + "WHERE id=:userId;")
    void updateUserSelectedAccount(
            int userId,
            String selectedAccount
    );
}
