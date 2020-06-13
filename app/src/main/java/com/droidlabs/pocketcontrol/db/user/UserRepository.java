package com.droidlabs.pocketcontrol.db.user;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.PocketControlDB;

import java.util.List;

public class UserRepository {

    private UserDao userDao;

    /**
     * Constructor.
     * @param application application.
     */
    public UserRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
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
    public User getUserById(final long userId) {
        User user = userDao.getUserById(userId);

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
}
