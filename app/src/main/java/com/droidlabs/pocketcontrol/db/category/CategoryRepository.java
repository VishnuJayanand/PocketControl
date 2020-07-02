package com.droidlabs.pocketcontrol.db.category;

import android.app.Application;

import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.db.user.UserDao;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private UserDao userDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * Category repository constructor.
     * @param application application to be used.
     */
    public CategoryRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        categoryDao = db.categoryDao();
        userDao = db.userDao();
    }

    /**
     * Get all the saved categories.
     * @return list of saved categories.
     */
    public List<Category> getAllCategories() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();

        if (currentUserId.equals("")) {
            return null;
        }

        return categoryDao.getAllCategories(currentUserId, currentAccountId);
    }

    /**
     * Get categories for export csv.
     * @return list of user categories.
     */
    public List<Category> getCategoriesForExport() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return categoryDao.getCategoriesForExport(currentUserId);
    }

    /**
     * Insert a new category.
     * @param category category to be saved.
     */
    public void insert(final Category category) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        User currentUser = userDao.getUserById(Integer.parseInt(currentUserId));
        String currentAccountId = currentUser.getSelectedAccount();

        if (currentUserId.equals("")) {
            return;
        }

        category.setOwnerId(currentUserId);
        category.setAccount(currentAccountId);

        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            categoryDao.insert(category);
        });
    }

    /**
     * get a  category.
     * @param categoryId category id.
     * @return Category.
     */
    public Category getSingleCategory(final int categoryId) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return categoryDao.getSingleCategory(categoryId, currentUserId);
    }

    /**
     * get a category.
     * @param categoryName category name.
     * @return category.
     */
    public Category getSingleCategory(final String categoryName) {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return categoryDao.getSingleCategory(categoryName, currentUserId);
    }

    /**
     * get a category.
     * @return the category names
     */
    public String[] getCategoriesName() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();
        String currentAccountId = sharedPreferencesUtils.getCurrentAccountIdKey();
        String income = "Income";

        if (currentUserId.equals("")) {
            return null;
        }

        return categoryDao.getCategoriesName(currentUserId, currentAccountId, income);
    }

}
