package com.droidlabs.pocketcontrol.db.category;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository repository;
    private LiveData<List<Category>> allCategories;

    /**
     * View model constructor.
     * @param application application to be used.
     */
    public CategoryViewModel(final Application application) {
        super(application);
        this.repository = new CategoryRepository(application);
        allCategories = repository.getAllCategories();
    }

    /**
     * Get all categories.
     * @return return all transactions from the database.
     */
    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    /**
     * Insert a new category in the database.
     * @param category transaction to be added.
     */
    public void insert(final Category category) {
        repository.insert(category);
    }
}
