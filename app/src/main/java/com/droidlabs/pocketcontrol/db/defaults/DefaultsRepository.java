package com.droidlabs.pocketcontrol.db.defaults;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class DefaultsRepository {
    private DefaultsDao defaultsDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Currency repository constructor.
     * @param application application to be used.
     */
    DefaultsRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        defaultsDao = db.defaultsDao();
    }

    /**
     * Get all currencies.
     * @return all saved currencies.
     */
    public List<Defaults> getAllDefaults() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return defaultsDao.getAllDefaults(currentUserId);
    }

    /**
     * Insert single default entity.
     * @param defaults entity to be saved.
     */
    public void insert(final Defaults defaults) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        defaults.setOwnerId(currentUserId);

        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            defaultsDao.insert(defaults);
        });
    }
}
