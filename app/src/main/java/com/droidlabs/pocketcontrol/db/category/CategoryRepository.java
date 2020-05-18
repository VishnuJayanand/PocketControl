package com.droidlabs.pocketcontrol.db.category;

import android.app.Application;

import com.droidlabs.pocketcontrol.db.PocketControlDB;

import java.util.List;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private List<Category> allCategories;

    /**
     * Category repository constructor.
     * @param application application to be used.
     */
    public CategoryRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        categoryDao = db.categoryDao();
        allCategories = categoryDao.getAllCategories();
    }

    /**
     * Get all the saved categories.
     * @return list of saved categories.
     */
    public List<Category> getAllCategories() {
        return allCategories;
    }

    /**
     * Insert a new category.
     * @param category category to be saved.
     */
    public void insert(final Category category) {
        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            categoryDao.insert(category);
        });
    }

    /**
     * Insert a new category.
     * @param categoryId category id.
     */
    public void getSingleCategory(final int categoryId) {
        categoryDao.getSingleCategory(categoryId);
    }
}
