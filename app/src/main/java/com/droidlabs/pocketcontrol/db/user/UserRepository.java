package com.droidlabs.pocketcontrol.db.user;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Constructor.
     * @param application application.
     */
    public UserRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        userDao = db.userDao();
    }

    /**
     * Insert a new user in the database.
     * @param user user to be saved.
     * @return user id.
     */
    public long insert(final User user) {
        return userDao.insert(user);
    }

    /**
     * Get all users from the database.
     * @return all id, first_name, last_name and email of users in the database.
     */
    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    /**
     * Get user by id.
     * @param userId id.
     * @return user.
     */
    public User getUserById(final int userId) {
        User user = userDao.getUserById(userId);

        // TODO: add validation, user should be authenticated

        return user;
    };

    /**
     * Get user by id.
     * @return user.
     */
    public User getCurrentUser() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        User user = userDao.getUserById(Integer.parseInt(currentUserId));

        // TODO: add validation, user should be authenticated

        return user;
    };

    /**
     * Get user by email.
     * @param userEmail email.
     * @return user.
     */
    public User getUserByEmail(final String userEmail) {
        User user = userDao.getUserByEmail(userEmail);

        // TODO: add validation, user should be authenticated

        return user;
    };

    /**
     * Update user selected account.
     * @param selectedAccount account id.
     */
    public void updateUserSelectedAccount(final String selectedAccount) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return;
        }

        userDao.updateUserSelectedAccount(Integer.parseInt(currentUserId), selectedAccount);
    }
}
