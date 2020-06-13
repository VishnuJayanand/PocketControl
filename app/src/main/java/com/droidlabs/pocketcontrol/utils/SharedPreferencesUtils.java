package com.droidlabs.pocketcontrol.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.droidlabs.pocketcontrol.R;

public final class SharedPreferencesUtils {

    private static SharedPreferences sharedPreferences;
    private static final String CURRENT_USER_ID_KEY = "currentUserId";
    private static final String CURRENT_ACCOUNT_ID_KEY = "currentAccountId";
    private static final String IS_SIGNED_IN_KEY = "isUserSignedIn";

    /**
     * Constructor.
     * @param application application.
     */
    public SharedPreferencesUtils(final Application application) {
        sharedPreferences = application.getSharedPreferences(
                application.getString(R.string.shared_preferences_file_key),
                Context.MODE_PRIVATE
        );
    }

    /**
     * Set current user id.
     * @param userId user id.
     * @return current instance of SharedPreferencesUtils.
     */
    public SharedPreferencesUtils setCurrentUserId(final String userId) {
        sharedPreferences.edit().putString(CURRENT_USER_ID_KEY, userId).commit();

        return this;
    }

    /**
     * Get current user id.
     * @return id.
     */
    public String getCurrentUserId() {
        return sharedPreferences.getString(CURRENT_USER_ID_KEY, "");
    }

    /**
     * Set current account id.
     * @param accountId account id.
     * @return current instance of SharedPreferencesUtils.
     */
    public SharedPreferencesUtils setCurrentAccountId(final String accountId) {
        sharedPreferences.edit().putString(CURRENT_ACCOUNT_ID_KEY, accountId).commit();

        return this;
    }

    /**
     * Get current account id.
     * @return id.
     */
    public String getCurrentAccountIdKey() {
        return sharedPreferences.getString(CURRENT_ACCOUNT_ID_KEY, "");
    }

    /**
     * Set user as signed in or not.
     * @param isSignedIn bool.
     * @return current instance of SharedPreferencesUtils.
     */
    public SharedPreferencesUtils setIsSignedIn(final boolean isSignedIn) {
        sharedPreferences.edit().putBoolean(IS_SIGNED_IN_KEY, isSignedIn).commit();

        return this;
    }

    /**
     * Get whether user is signed in or not.
     * @return boolean.
     */
    public boolean getIsSignedIn() {
        return sharedPreferences.getBoolean(IS_SIGNED_IN_KEY, false);
    }
}
