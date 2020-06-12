package com.droidlabs.pocketcontrol.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.droidlabs.pocketcontrol.R;

public final class SharedPreferencesUtils {

    private static SharedPreferences sharedPreferences;
    private static String CURRENT_USER_ID_KEY = "currentUserId";
    private static String CURRENT_ACCOUNT_ID_KEY = "currentAccountId";

    public SharedPreferencesUtils(final Application application) {
        sharedPreferences = application.getSharedPreferences(application.getString(R.string.shared_preferences_file_key), Context.MODE_PRIVATE);
    }

    public SharedPreferencesUtils setCurrentUserId(String userId) {
        sharedPreferences.edit().putString(CURRENT_USER_ID_KEY, userId).commit();

        return this;
    }

    public String getCurrentUserId() {
        return sharedPreferences.getString(CURRENT_USER_ID_KEY, "");
    }

    public SharedPreferencesUtils setCurrentAccountId(String accountId) {
        sharedPreferences.edit().putString(CURRENT_ACCOUNT_ID_KEY, accountId).commit();

        return this;
    }

    public String getCurrentProjectIdKey() {
        return sharedPreferences.getString(CURRENT_ACCOUNT_ID_KEY, "");
    }
}
