package com.droidlabs.pocketcontrol.db.category;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private List<Category> allCategories;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Category repository constructor.
     * @param application application to be used.
     */
    public CategoryRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        categoryDao = db.categoryDao();
    }

    /**
     * Get all the saved categories.
     * @return list of saved categories.
     */
    public List<Category> getAllCategories() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        Log.v("USERINFO", currentUserId);

        if (currentUserId.equals("")) {
            return null;
        }

        return categoryDao.getAllCategories(currentUserId);
    }

    /**
     * Insert a new category.
     * @param category category to be saved.
     */
    public void insert(final Category category) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        category.setOwnerId(currentUserId);

        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            categoryDao.insert(category);
        });
    }

    /**
     * get a  category.
     * @param categoryId category id.
     */
    public void getSingleCategory(final int categoryId) {
        categoryDao.getSingleCategory(categoryId);
    }

    /**
     * get a category.
     * @param categoryName category id.
     */
    public void getSingleCategory(final String categoryName) {
        categoryDao.getSingleCategory(categoryName);
    }
}
