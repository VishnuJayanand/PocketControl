package com.droidlabs.pocketcontrol.db.defaults;

import android.app.Application;

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
    public DefaultsRepository(final Application application) {
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
     * Get default value.
     * @param name default name.
     * @return default value.
     */
    public String getDefaultValue(final String name) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return defaultsDao.getDefaultValue(name, currentUserId);
    }

    /**
     * Insert single default entity.
     * @param defaults entity to be saved.
     */
    public void insert(final Defaults defaults) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return;
        }

        defaults.setOwnerId(currentUserId);

        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            defaultsDao.insert(defaults);
        });
    }
}
