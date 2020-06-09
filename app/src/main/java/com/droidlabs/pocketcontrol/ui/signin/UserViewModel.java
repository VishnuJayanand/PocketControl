package com.droidlabs.pocketcontrol.ui.signin;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.db.user.UserRepository;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;

    /**
     * View model constructor.
     * @param application application to be used.
     */
    public UserViewModel(final Application application) {
        super(application);
        this.repository = new UserRepository(application);
    }

    /**
     * Insert a new user in the database.
     * @param user transaction to be added.
     */
    public void insert(final User user) {
        repository.insert(user);
    }

    /**
     * Get all users from the database.
     * @return all id, first_name, last_name and email of users in the database.
     */
    public LiveData<List<User>> getAllUsers() {
        return repository.getAllUsers();
    }

    /**
     * Get user by id.
     * @param userId id.
     * @return user.
     */
    public User getUserById(final long userId) {
        User user = repository.getUserById(userId);

        // TODO: add validation, user should be authenticated

        return user;
    };

    /**
     * Get user by email.
     * @param userEmail email.
     * @return user.
     */
    public User getUserByEmail(final String userEmail) {
        User user = repository.getUserByEmail(userEmail);

        // TODO: add validation, user should be authenticated

        return user;
    };

    /**
     * Get current user from SharedPreferences by email.
     * @return user.
     */
    public User getCurrentUser() {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(getApplication());

        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        User user = repository.getUserById(Long.parseLong(currentUserId));

        // TODO: add validation, user should be authenticated

        return user;
    };
}
