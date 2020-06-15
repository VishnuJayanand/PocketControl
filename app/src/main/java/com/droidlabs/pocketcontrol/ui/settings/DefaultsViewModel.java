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
}
