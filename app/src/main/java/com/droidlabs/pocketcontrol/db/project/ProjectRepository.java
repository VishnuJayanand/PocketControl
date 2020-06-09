package com.droidlabs.pocketcontrol.db.project;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public class ProjectRepository {

    private ProjectDao projectDao;
    private LiveData<List<Project>> allProjects;
    private SharedPreferencesUtils sharedPreferencesUtils;

    public ProjectRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        sharedPreferencesUtils = new SharedPreferencesUtils(application);
        projectDao = db.projectDao();
    }

    /**
     * Get all projects from the database.
     * @return all projects in the database.
     */
    public LiveData<List<Project>> getAllProjects() {
        String currentUserId = sharedPreferencesUtils.getCurrentUserId();

        if (currentUserId.equals("")) {
            return null;
        }

        return projectDao.getAllProjects(currentUserId);
    }

    /**
     * Get project by id.
     * @param projectId id.
     * @return transaction.
     */
    public Project getProjectById(final long projectId) {
        Project project = projectDao.getProjectById(projectId);

        if (project.getIsPublic() != null && project.getIsPublic()) {
            return project;
        }

        // TODO: add validation, user should be owner of project
        return project;
    };
}
