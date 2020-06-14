package com.droidlabs.pocketcontrol.ui.signin;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.db.user.UserRepository;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * View model constructor.
     * @param application application to be used.
     */
    public UserViewModel(final Application application) {
        super(application);
        this.repository = new UserRepository(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
    }

    /**
     * Insert a new user in the database.
     * @param user transaction to be added.
     * @return user id.
     */
    public long insert(final User user) {
        return repository.insert(user);
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
    public User getUserById(final int userId) {
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
        User user = repository.getCurrentUser();

        // TODO: add validation, user should be authenticated

        return user;
    };

    public void updateUserSelectedAccount(String selectedAccount) {
        repository.updateUserSelectedAccount(selectedAccount);
    }
}
