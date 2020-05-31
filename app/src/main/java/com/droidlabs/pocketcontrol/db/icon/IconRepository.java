package com.droidlabs.pocketcontrol.db.icon;

import android.app.Application;

import com.droidlabs.pocketcontrol.db.PocketControlDB;

import java.util.List;

public class IconRepository {

    private IconDao iconDao;
    private List<Icon> allIcons;

    /**
     * Icon repository constructor.
     * @param application application to be used.
     */
    IconRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        iconDao = db.iconDao();
        allIcons = iconDao.getAllIcons();
    }

    /**
     * Return all saved icons.
     * @return all saved icons.
     */
    public List<Icon> getAllIcons() {
        return allIcons;
    }

    /**
     * Insert new icon in the database.
     * @param icon icon to be saved.
     */
    public void insert(final Icon icon) {
        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            iconDao.insert(icon);
        });
    }
}
