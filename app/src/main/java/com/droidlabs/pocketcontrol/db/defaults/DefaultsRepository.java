package com.droidlabs.pocketcontrol.db.defaults;

import android.app.Application;

import com.droidlabs.pocketcontrol.db.PocketControlDB;

import java.util.List;

public class DefaultsRepository {
    private DefaultsDao defaultsDao;
    private List<Defaults> allDefaults;

    /**
     * Currency repository constructor.
     * @param application application to be used.
     */
    DefaultsRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        defaultsDao = db.defaultsDao();
        allDefaults = defaultsDao.getAllDefaults();
    }

    /**
     * Get all currencies.
     * @return all saved currencies.
     */
    public List<Defaults> getAllDefaults() {
        return allDefaults;
    }

    /**
     * Insert single default entity.
     * @param defaults entity to be saved.
     */
    public void insert(final Defaults defaults) {
        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            defaultsDao.insert(defaults);
        });
    }
}
