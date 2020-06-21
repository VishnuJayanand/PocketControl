package com.droidlabs.pocketcontrol.ui.settings;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.droidlabs.pocketcontrol.db.defaults.Defaults;
import com.droidlabs.pocketcontrol.db.defaults.DefaultsRepository;

import java.util.List;

public class DefaultsViewModel extends AndroidViewModel {

    private DefaultsRepository defaultsRepository;

    /**
     * Constructor.
     * @param application application.
     */
    public DefaultsViewModel(final Application application) {
        super(application);
        this.defaultsRepository = new DefaultsRepository(application);
    }

    /**
     * Insert new default.
     * @param defaults default.
     */
    public void insert(final Defaults defaults) {
        defaultsRepository.insert(defaults);
    }

    /**
     * Get all defaults.
     * @return all defaults.
     */
    public List<Defaults> getAllDefaults() {
        return defaultsRepository.getAllDefaults();
    }

    /**
     * Get single default value.
     * @param name default name.
     * @return default value.
     */
    public String getDefaultValue(final String name) {
        return  defaultsRepository.getDefaultValue(name);
    }

    /**
     * Get symbol from currency code.
     * @param currency code
     * @return currency symbol
     */
    public String getCurrencySymbol(final String currency) {
        return defaultsRepository.getCurrencySymbol(currency);
    }

    /**
     * Get single default value.
     * deletes all values
     */
    public void deleteAll() {
        defaultsRepository.deleteAll();
    }

    /**
     * Update default value on DB.
     * @param defaultName default name.
     * @param newDefaultValue default value.
     */
    public void updateDefaultValue(final String defaultName, final String newDefaultValue) {
        defaultsRepository.updateDefaultValue(defaultName, newDefaultValue);
    }
}
