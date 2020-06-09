package com.droidlabs.pocketcontrol.db.project;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.PocketControlDB;

import java.util.List;

public class ProjectRepository {

    private ProjectDao projectDao;
    private LiveData<List<Project>> allProjects;

    public ProjectRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        projectDao = db.projectDao();
    }

    /**
     * Get all projects from the database.
     * @return all projects in the database.
     */
    public LiveData<List<Project>> getAllProjects(String userId) {
        return projectDao.getAllProjects(userId);
    }

    /**
     * Get project by id.
     * @param projectId id.
     * @return transaction.
     */
    public Project getProjectById(final long projectId) {
        Project project = projectDao.getProjectById(projectId);

        if (project.getPublic() != null) {
            return project;
        }

        // TODO: add validation, user should be owner of project
        return project;
    };
}
